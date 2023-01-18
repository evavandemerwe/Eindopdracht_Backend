package nl.novi.breedsoft.service;

import nl.novi.breedsoft.dto.medicalDataDtos.MedicalDataInputDto;
import nl.novi.breedsoft.dto.medicalDataDtos.MedicalDataOutputDto;
import nl.novi.breedsoft.dto.medicalDataDtos.MedicalDataPatchDto;
import nl.novi.breedsoft.exception.RecordNotFoundException;
import nl.novi.breedsoft.model.management.DomesticatedDog;
import nl.novi.breedsoft.model.management.MedicalData;
import nl.novi.breedsoft.repository.DomesticatedDogRepository;
import nl.novi.breedsoft.repository.MedicalDataRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MedicalDataService {

    private final MedicalDataRepository medicalDataRepository;
    private final DomesticatedDogRepository domesticatedDogRepository;
    public MedicalDataService(MedicalDataRepository medicalDataRepository, DomesticatedDogRepository domesticatedDogRepository) {
        this.medicalDataRepository = medicalDataRepository;
        this.domesticatedDogRepository = domesticatedDogRepository;
    }

    /**
     * A method for retrieval of all medical data from the database
     * @return a list of medical data in output dto format
     */
    public List<MedicalDataOutputDto> getAllMedicalData(){
        List<MedicalData> allMedicalDataList = medicalDataRepository.findAll();
        return transferMedicalDataToOutputDtoList(allMedicalDataList);
    }

    /**
     * A method for retrieval of one medical data record from the database by id
     * @param domesticatedDogId ID of the dog for which information is requested
     * @return medical data in output dto format
     * @throws RecordNotFoundException throws an exception when no medical data is found for dog with given id
     */
    public List<MedicalDataOutputDto> getMedicaLDataByDogId(Long domesticatedDogId){
        List<MedicalData> allMedicalData = medicalDataRepository.findAll();
        List<MedicalData> dogMedicalData = new ArrayList<>();

        for(MedicalData medicalData : allMedicalData){
            Long dogId = medicalData.getDomesticatedDog().getId();
            if(dogId.equals(domesticatedDogId)){
                dogMedicalData.add(medicalData);
            }
        }
        if(dogMedicalData.isEmpty()){
            throw new RecordNotFoundException("There is no medical data for this dog");
        }
        return transferMedicalDataToOutputDtoList(dogMedicalData);
    }

    /**
     * A method to create a new medical data record in the database
     * @param medicalDataInputDto Data Transfer Objects that carries data between processes in order to reduce the number of methods calls
     * @return the ID of the medical data found in the database
     * @throws RecordNotFoundException throws an exception when the provided dog does not exist
     */
    public Long createMedicalData(MedicalDataInputDto medicalDataInputDto){
        //Check if a dog is given
        if(medicalDataInputDto.getDomesticatedDog() != null){
            DomesticatedDog dog = getCompleteDogById(medicalDataInputDto.getDomesticatedDog().getId());
            if(dog == null){
                throw new RecordNotFoundException("Provided dog does not exist");
            }
            medicalDataInputDto.setDomesticatedDog(dog);
        }
        MedicalData medicalData = transferToMedicalData(medicalDataInputDto);
        medicalDataRepository.save(medicalData);

        return medicalData.getId();
    }

    /**
     * A method (PUT) sends an enclosed entity of a resource to the server.
     * If the entity already exists, the server overrides the existing object,
     * otherwise the server creates a new entity.
     * @param medicalDataId ID of the medical data for which an update or creation is requested
     * @param medicalDataInputDto Data Transfer Objects that carries data between processes in order to reduce the number of methods calls
     * @return a new or updated medical data record in output dto format
     * @throws RecordNotFoundException throws an exception when domesticated dog is not found by id
     */
    public Object updateMedicalData(Long medicalDataId, MedicalDataInputDto medicalDataInputDto){
        if(medicalDataRepository.findById(medicalDataId).isPresent()){
            MedicalData medicalData = medicalDataRepository.findById(medicalDataId).get();
            MedicalData updateMedicalData = transferToMedicalData(medicalDataInputDto);
            if(medicalDataInputDto.getDomesticatedDog() != null) {
                DomesticatedDog dog = getCompleteDogById(medicalDataInputDto.getDomesticatedDog().getId());
                if(dog == null){
                    throw new RecordNotFoundException("Provided dog does not exist");
                }
                updateMedicalData.setDomesticatedDog(dog);
            }
            //Keeping the former id, as we will update the existing appointment
            updateMedicalData.setId(medicalData.getId());
            medicalDataRepository.save(updateMedicalData);
            return transferMedicalDataToOutputDto(medicalData);
        } else {
            Long newMedicalDataId = createMedicalData(medicalDataInputDto);
            MedicalData newMedicalData = medicalDataRepository.getReferenceById(newMedicalDataId);

            return transferMedicalDataToOutputDto(newMedicalData);
        }
    }

    /**
     * A method (PATCH) will only update an existing object,
     * with the properties mapped in the request body (that are not null).
     * We do NOT update veterinarian appointment and medical data here.
     * @param medicalDataId ID of the medical data for which an update is requested
     * @param medicalDataPatchDto Data Transfer Objects that carries data between processes in order to reduce the number of methods calls
     * @return the updated medical data in output dto format
     * @throws RecordNotFoundException throws an exception when provided domesticated dog does not exist,
     * or when medical data is not found based on medicalDataId
     */
    public MedicalDataOutputDto patchMedicalData(Long medicalDataId, MedicalDataPatchDto medicalDataPatchDto){
        if(medicalDataRepository.findById(medicalDataId).isPresent()){
            MedicalData updateMedicalData = medicalDataRepository.getReferenceById(medicalDataId);
            if(medicalDataPatchDto.getDateOfMedicalTreatment() != null){
                updateMedicalData.setDateOfMedicalTreatment(medicalDataPatchDto.getDateOfMedicalTreatment());
            }
            if(medicalDataPatchDto.getDiagnose() != null){
                updateMedicalData.setDiagnose(medicalDataPatchDto.getDiagnose());
            }
            if(medicalDataPatchDto.getTreatment() != null){
                updateMedicalData.setTreatment(medicalDataPatchDto.getTreatment());
            }
            if(medicalDataPatchDto.getMedicine() != null){
                updateMedicalData.setMedicine(medicalDataPatchDto.getMedicine());
            }
            if(
                    medicalDataPatchDto.getDomesticatedDog().getId() != null &&
                    medicalDataPatchDto.getDomesticatedDog().getId() != 0
            ){
                DomesticatedDog dog = getCompleteDogById(medicalDataPatchDto.getDomesticatedDog().getId());
                if (dog == null) {
                    throw new RecordNotFoundException("Provided dog does not exist.");
                }
                updateMedicalData.setDomesticatedDog(dog);
            }
            medicalDataRepository.save(updateMedicalData);
            return transferMedicalDataToOutputDto(updateMedicalData);
        } else {
            throw new RecordNotFoundException("Medical data not found");
        }
    }

    /**
     * A method for deleting medical data from the database by id
     * @param medicalDataId ID of the medical data for which information is requested
     * @throws RecordNotFoundException throws an exception if no medical data is found by medicalDataId
     */
    public void deleteMedicalData(Long medicalDataId){

        if(medicalDataRepository.findById(medicalDataId).isPresent()){
            // If an appointment is deleted from the database, this appointment is detached from dogs.
            MedicalData medicalDataFound = medicalDataRepository.getReferenceById(medicalDataId);

            if(medicalDataFound.getDomesticatedDog() != null){
                medicalDataFound.setDomesticatedDog(null);
            }
            medicalDataRepository.deleteById(medicalDataId);
        } else {
            throw new RecordNotFoundException("No medical data with given ID found.");
        }
    }

    //DTO Helper classes

    /**
     * A method to transform medical data in input dto format to medical data format
     * @param medicalDataInputDto Data Transfer Objects that carries data between processes in order to reduce the number of methods calls
     * @return Medical data
     */
    private MedicalData transferToMedicalData(MedicalDataInputDto medicalDataInputDto){
        MedicalData medicalData = new MedicalData();

        medicalData.setDateOfMedicalTreatment(medicalDataInputDto.getDateOfMedicalTreatment());
        medicalData.setDiagnose(medicalDataInputDto.getDiagnose());
        medicalData.setId(medicalDataInputDto.getId());
        medicalData.setMedicine(medicalDataInputDto.getMedicine());
        medicalData.setTreatment(medicalDataInputDto.getTreatment());
        medicalData.setDiagnose(medicalDataInputDto.getDiagnose());
        medicalData.setDomesticatedDog(medicalDataInputDto.getDomesticatedDog());

        return medicalData;
    }

    /**
     * A method to transform medical data list to medical data list in output dto format
     * @param medicalDataList a list with medical data in medical data format
     * @return a list with medical data in output dto format
     */
    private List<MedicalDataOutputDto> transferMedicalDataToOutputDtoList(List <MedicalData> medicalDataList){
        List<MedicalDataOutputDto> medicalDataOutputDtoList = new ArrayList<>();
        for(MedicalData data : medicalDataList){
            MedicalDataOutputDto dto = transferMedicalDataToOutputDto(data);
            medicalDataOutputDtoList.add(dto);
        }
        return medicalDataOutputDtoList;
    }

    /**
     * A method to transform medical data to medical data in output dto format
     * @param medicalData medical data in medical data format
     * @return medical data in output dto format
     */

    private MedicalDataOutputDto transferMedicalDataToOutputDto(MedicalData medicalData){
        MedicalDataOutputDto medicalDataOutputDto = new MedicalDataOutputDto();
        medicalDataOutputDto.setId(medicalData.getId());
        medicalDataOutputDto.setDateOfMedicalTreatment(medicalData.getDateOfMedicalTreatment());
        medicalDataOutputDto.setDiagnose(medicalData.getDiagnose());
        medicalDataOutputDto.setTreatment(medicalData.getTreatment());
        medicalDataOutputDto.setMedicine(medicalData.getMedicine());
        medicalDataOutputDto.setDomesticatedDog(medicalData.getDomesticatedDog());

        return medicalDataOutputDto;
    }

    /**
     * Look for domesticated dog by id in domesticated dog repository.
     * When no domesticated dog ID is given, the get domesticated dog method returns 0 and an error is thrown.
     * When domesticated dog ID is found, domesticated dog is returned.
     * If there is no domesticated dog found in the repository, null is returned.
     * @param dogId ID of the domesticated dog for which information is requested
     * @return domesticated dog or null if not present.
     * @throws RecordNotFoundException throws an exception when domesticated dog ID is missing
     */
    private DomesticatedDog getCompleteDogById(Long dogId){
        if(dogId == 0){
            throw new RecordNotFoundException("Missing dog ID");
        }
        Optional<DomesticatedDog> domesticatedDogOptional = domesticatedDogRepository.findById(dogId);
        return domesticatedDogOptional.orElse(null);
    }
}
