package nl.novi.breedsoft.model.management;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "medical_data")
public class MedicalData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate dateOfMedicalTreatment;
    private String medicine;
    private String diagnose;
    private String treatment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMedicine() {
        return medicine;
    }

    public void setMedicine(String medicine) {
        this.medicine = medicine;
    }

    public LocalDate getDateOfMedicalTreatment() {
        return dateOfMedicalTreatment;
    }

    public void setDateOfMedicalTreatment(LocalDate dateOfMedicalTreatment) {
        this.dateOfMedicalTreatment = dateOfMedicalTreatment;
    }
}
