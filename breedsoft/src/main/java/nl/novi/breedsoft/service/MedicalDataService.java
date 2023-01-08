package nl.novi.breedsoft.service;

import nl.novi.breedsoft.dto.medicalDataDtos.MedicalDataInputDto;
import nl.novi.breedsoft.dto.medicalDataDtos.MedicalDataOutputDto;
import nl.novi.breedsoft.exception.RecordNotFoundException;
import nl.novi.breedsoft.model.management.MedicalData;
import nl.novi.breedsoft.repository.MedicalDataRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MedicalDataService {

    private final MedicalDataRepository medicalDataRepository;

    public MedicalDataService(MedicalDataRepository medicalDataRepository) {
        this.medicalDataRepository = medicalDataRepository;
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
        MedicalData medicalData = transferToMedicalData(medicalDataInputDto);
        medicalDataRepository.save(medicalData);

        return medicalData.getId();
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
}
