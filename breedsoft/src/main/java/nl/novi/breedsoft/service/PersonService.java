package nl.novi.breedsoft.service;
import nl.novi.breedsoft.dto.PersonInputDto;
import nl.novi.breedsoft.dto.PersonOutputDto;
import nl.novi.breedsoft.model.animal.Person;
import org.springframework.stereotype.Service;

@Service
public class PersonService {

    public Person transferToPerson(PersonInputDto dto){
        Person person = new Person();

        person.setFirstName(dto.getFirstName());
        person.setLastName(dto.getLastName());
        //person.setSex(dto.getSex());
        person.setDateOfBirth(dto.getDateOfBirth());
        person.setStreet(dto.getStreet());
        person.setHouseNumber(dto.getHouseNumber());
        person.setHouseNumberExtension(dto.getHouseNumberExtension());
        person.setZipCode(dto.getZipCode());
        person.setCity(dto.getCity());
        person.setCountry(dto.getCountry());

        return person;
    }
}
