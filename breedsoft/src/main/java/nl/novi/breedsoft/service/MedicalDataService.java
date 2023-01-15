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

    public List<MedicalDataOutputDto> getAllMedicalData(){
        List<MedicalData> allMedicalDataList = medicalDataRepository.findAll();
        List<MedicalDataOutputDto> medicalDataOutputDtoList = transferMedicalDataToOutputDtoList(allMedicalDataList);
        return medicalDataOutputDtoList;
    }

    public List<MedicalDataOutputDto> getMedicaLDataByDogId(Long id){
        List<MedicalData> allMedicalData = medicalDataRepository.findAll();
        List<MedicalData> dogMedicalData = new ArrayList<>();

        for(MedicalData medicalData : allMedicalData){
            Long dogId = medicalData.getDomesticatedDog().getId();
            if(dogId.equals(id)){
                dogMedicalData.add(medicalData);
            }
        }
        if(dogMedicalData.isEmpty()){
            throw new RecordNotFoundException("There is no medical data for this dog");
        }
        return transferMedicalDataToOutputDtoList(dogMedicalData);
    }

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

    //PUT sends an enclosed entity of a resource to the server.
    //If the entity already exists, the server overrides the existing object.
    //Otherwise, the server creates a new entity
    public Object updateMedicalData(Long id, MedicalDataInputDto medicalDataInputDto){
        if(medicalDataRepository.findById(id).isPresent()){
            MedicalData medicalData = medicalDataRepository.findById(id).get();
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

    //PATCH will only update an existing object,
    //with the properties mapped in the request body (that are not null).
    public MedicalDataOutputDto patchMedicalData(Long id, MedicalDataPatchDto medicalDataPatchDto){
        if(medicalDataRepository.findById(id).isPresent()){
            MedicalData updateMedicalData = medicalDataRepository.getReferenceById(id);
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

    public void deleteMedicalData(Long id){

        if(medicalDataRepository.findById(id).isPresent()){
            // If an appointment is deleted from the database, this appointment is detached from dogs.
            MedicalData medicalDataFound = medicalDataRepository.getReferenceById(id);

            if(medicalDataFound.getDomesticatedDog() != null){
                medicalDataFound.setDomesticatedDog(null);
            }
            medicalDataRepository.deleteById(id);
        } else {
            throw new RecordNotFoundException("No medical data with given ID found.");
        }
    }

    //DTO Helper classes

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
    private List<MedicalDataOutputDto> transferMedicalDataToOutputDtoList(List <MedicalData> medicalData){
        List<MedicalDataOutputDto> medicalDataOutputDtoList = new ArrayList<>();
        for(MedicalData data : medicalData){
            MedicalDataOutputDto dto = transferMedicalDataToOutputDto(data);
            medicalDataOutputDtoList.add(dto);
        }
        return medicalDataOutputDtoList;
    }

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


    //Look for dog by ID in dogrespository.
    //When nu dog ID is given, the get dog method returns 0 and an error is thrown.
    //When dog ID is found, dog is returned. If there is no dog found in the repository, null is returned.

    private DomesticatedDog getCompleteDogById(Long dogId){
        if(dogId == 0){
            throw new RecordNotFoundException("Missing dog ID");
        }
        Optional<DomesticatedDog> domesticatedDogOptional = domesticatedDogRepository.findById(dogId);
        return domesticatedDogOptional.orElse(null);
    }
}
