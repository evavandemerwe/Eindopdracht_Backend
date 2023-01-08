package nl.novi.breedsoft.model.management;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "appointments")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private LocalDate appointmentDate;
    private String subject;

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

    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDate appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
