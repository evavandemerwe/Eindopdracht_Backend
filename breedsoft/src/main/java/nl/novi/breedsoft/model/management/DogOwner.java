package nl.novi.breedsoft.model.management;
import nl.novi.breedsoft.model.animal.Person;

import java.util.List;

public class DogOwner extends Person {
    private List<DomesticatedDog> dogs;
    private List<Appointment> appointments;
}