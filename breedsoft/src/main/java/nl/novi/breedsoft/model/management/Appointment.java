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
    @JoinColumn(name = "appointer_id")
    private Person appointer;

    @ManyToOne
    @JoinColumn(name = "appointee_id")
    private Person appointee;

    public Long getId() {
        return id;
    }

    public Person getAppointee() {
        return appointee;
    }

    public void setAppointee(Person appointee) {
        this.appointee = appointee;
    }

    public Person getAppointer() {
        return appointer;
    }

    public void setAppointer(Person appointer) {
        this.appointer = appointer;
    }
}
