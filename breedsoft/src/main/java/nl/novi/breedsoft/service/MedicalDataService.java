package nl.novi.breedsoft.service;

import nl.novi.breedsoft.model.management.MedicalData;
import nl.novi.breedsoft.repository.MedicalDataRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MedicalDataService {

    private final MedicalDataRepository medicalDataRepository;

    public MedicalDataService(MedicalDataRepository medicalDataRepository) {
        this.medicalDataRepository = medicalDataRepository;
    }

    public List<MedicalData> getAllMedicatData(){
        List<MedicalData> allMedicalDataList = medicalDataRepository.findAll();
        return allMedicalDataList;
    }
}
