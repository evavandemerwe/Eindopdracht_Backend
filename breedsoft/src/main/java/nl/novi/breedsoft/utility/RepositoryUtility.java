package nl.novi.breedsoft.utility;

import nl.novi.breedsoft.exception.RecordNotFoundException;
import nl.novi.breedsoft.model.management.DomesticatedDog;
import nl.novi.breedsoft.model.management.Person;
import nl.novi.breedsoft.repository.DomesticatedDogRepository;
import nl.novi.breedsoft.repository.PersonRepository;

import java.util.Optional;

public class RepositoryUtility {

    private final DomesticatedDogRepository domesticatedDogRepository;
    private final PersonRepository personRepository;

    public RepositoryUtility(
        DomesticatedDogRepository domesticatedDogRepository,
        PersonRepository personRepository
    ) {
        this.domesticatedDogRepository = domesticatedDogRepository;
        this.personRepository = personRepository;
    }
    //Look for dog by ID in dogrespository.
    //When nu dog ID is given, the get dog method returns 0 and an error is thrown.
    //When dog ID is found, dog is returned. If there is no dog found in the repository, null is returned.
    public DomesticatedDog getCompleteDogId(Long dogId){
        if(dogId == 0){
            throw new RecordNotFoundException("Missing Dog ID");
        }
        Optional<DomesticatedDog> domesticatedDog = domesticatedDogRepository.findById(dogId);
        return domesticatedDog.orElse(null);
    }

    /**
     * Look for person by id in person repository.
     * When no person ID is given, the get person method returns 0 and an error is thrown.
     * When person ID is found, person is returned.
     * If there is no person found in the repository, null is returned.
     * @param personId ID of the person for which information is requested
     * @return person or null if not present.
     */
    public Person getCompletePersonById(Long personId) {
        if (personId == 0) {
            throw new RecordNotFoundException("Missing Person ID");
        }
        Optional<Person> person = personRepository.findById(personId);
        return person.orElse(null);
    }
}
