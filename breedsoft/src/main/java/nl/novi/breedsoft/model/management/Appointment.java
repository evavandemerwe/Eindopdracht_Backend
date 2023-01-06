package nl.novi.breedsoft.model.management;

import nl.novi.breedsoft.model.animal.Animal;

import java.time.LocalDateTime;

public class Appointment {
    private LocalDateTime appointmentDateTime;
    private String subject;
    private Person appointer;
    private Person appointtee;
    private Animal relatedAnimal;
}
