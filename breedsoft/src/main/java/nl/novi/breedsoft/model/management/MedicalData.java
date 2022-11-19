package nl.novi.breedsoft.model.management;

import java.time.LocalDate;

public abstract class MedicalData {
    private LocalDate dateOfMedicalTreatment;
    private String medicine;
    private String diagnose;
    private String treatMent;

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
