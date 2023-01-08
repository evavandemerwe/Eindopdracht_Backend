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

    @ManyToOne
    @JoinColumn(name = "domesticated_dog_id")
    private DomesticatedDog domesticatedDog;

    public DomesticatedDog getDomesticatedDog() {
        return domesticatedDog;
    }

    public void setDomesticatedDog(DomesticatedDog domesticatedDog) {
        this.domesticatedDog = domesticatedDog;
    }

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

    public String getDiagnose() {
        return diagnose;
    }

    public void setDiagnose(String diagnose) {
        this.diagnose = diagnose;
    }

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }
}
