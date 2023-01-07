package nl.novi.breedsoft.model.management;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private LocalDateTime appointmentDateTime;
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

    public LocalDateTime getAppointmentDateTime() {
        return appointmentDateTime;
    }

    public void setAppointmentDateTime(LocalDateTime appointmentDateTime) {
        this.appointmentDateTime = appointmentDateTime;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
