package nl.novi.breedsoft.repository;

import nl.novi.breedsoft.model.management.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
}
