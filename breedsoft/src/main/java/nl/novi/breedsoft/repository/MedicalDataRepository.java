package nl.novi.breedsoft.repository;

import nl.novi.breedsoft.model.management.MedicalData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicalDataRepository extends JpaRepository<MedicalData, Long> {

}