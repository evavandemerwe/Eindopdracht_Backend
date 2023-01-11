package nl.novi.breedsoft.service;

import nl.novi.breedsoft.dto.domesticatedDogDtos.DomesticatedDogInputDto;
import nl.novi.breedsoft.dto.domesticatedDogDtos.DomesticatedDogOutputDto;
import nl.novi.breedsoft.dto.domesticatedDogDtos.DomesticatedDogPatchDto;
import nl.novi.breedsoft.exception.EnumValueNotFoundException;
import nl.novi.breedsoft.exception.RecordNotFoundException;
import nl.novi.breedsoft.model.management.VeterinarianAppointment;
import nl.novi.breedsoft.model.management.DomesticatedDog;
import nl.novi.breedsoft.model.management.MedicalData;
import nl.novi.breedsoft.model.management.Person;
import nl.novi.breedsoft.model.management.enumerations.Status;
import nl.novi.breedsoft.repository.VeterinarianAppointmentRepository;
import nl.novi.breedsoft.repository.MedicalDataRepository;
import nl.novi.breedsoft.repository.PersonRepository;
import nl.novi.breedsoft.model.management.enumerations.Breed;
import nl.novi.breedsoft.model.management.enumerations.BreedGroup;
import nl.novi.breedsoft.model.animal.enumerations.Sex;
import nl.novi.breedsoft.repository.DomesticatedDogRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
public class DomesticatedDogService {
    private final DomesticatedDogRepository domesticatedDogRepository;
    private final PersonRepository personRepository;
    private final VeterinarianAppointmentRepository veterinarianAppointmentRepository;
    private final MedicalDataRepository medicalDataRepository;

    //Constructor injection
    public DomesticatedDogService(
            DomesticatedDogRepository domesticatedDogRepository,
            PersonRepository personRepository,
            VeterinarianAppointmentRepository veterinarianAppointmentRepository,
            MedicalDataRepository medicalDataRepository){
        this.domesticatedDogRepository = domesticatedDogRepository;
        this.personRepository = personRepository;
        this.veterinarianAppointmentRepository = veterinarianAppointmentRepository;
        this.medicalDataRepository = medicalDataRepository;
    }

    //Output Dto is used for representing data from the database to the user
    //Get all dogs
    public List<DomesticatedDogOutputDto> getAllDomesticatedDogs() {
        List<DomesticatedDog> domesticatedDogList = domesticatedDogRepository.findAll();
        return transferDomesticatedDogListToDtoList(domesticatedDogList);
    }

    //Get one dog by ID
    public DomesticatedDogOutputDto getDomesticatedDogById(Long id) {
        if (domesticatedDogRepository.findById(id).isPresent()){
            DomesticatedDog domesticatedDog = domesticatedDogRepository.findById(id).get();
            return transferToOutputDto(domesticatedDog);
        } else {
            throw new RecordNotFoundException("Dog not found in database");
        }
    }

    //Get dog list by Name
    public List<DomesticatedDogOutputDto> getDomesticatedDogByName(String name) {
        if (domesticatedDogRepository.findByNameContaining(name).isEmpty()){
            throw new RecordNotFoundException("No dog with this name found in database");
        } else {
            List<DomesticatedDog> domesticatedDogList = domesticatedDogRepository.findByNameContaining(name);
            List<DomesticatedDogOutputDto> domesticatedDogOutputDtoList = new ArrayList<>();
            for(DomesticatedDog domesticatedDog : domesticatedDogList){
                domesticatedDogOutputDtoList.add(transferToOutputDto(domesticatedDog));
            }
            return domesticatedDogOutputDtoList;
        }
    }

    public List<DomesticatedDogOutputDto> getAllChildren(Long id){
        List<DomesticatedDog> children = new ArrayList<>();

        if(domesticatedDogRepository.findById(id).isEmpty()){
            throw new RecordNotFoundException("No dog with this id found in database");
        } else {
            List<DomesticatedDog> allDomesticatedDogs = domesticatedDogRepository.findAll();
            for(DomesticatedDog domesticatedDog : allDomesticatedDogs){
                if(Objects.equals(domesticatedDog.getParentId(), id)){
                    children.add(domesticatedDog);
                }
            }
            if(children.isEmpty()){
                throw new RecordNotFoundException("This dog doesn't have children");
            }
        }

        return transferDomesticatedDogListToDtoList(children);
    }

    public List<DomesticatedDogOutputDto> getAvailableDomesticatedDogs(){
        List<DomesticatedDog> domesticatedDogList = domesticatedDogRepository.findAll();
        List<DomesticatedDog> availableDogs = new ArrayList<>();
        for(DomesticatedDog domesticatedDog : domesticatedDogList){
            if(domesticatedDog.getDogStatus().toString().equals("availablePup") || domesticatedDog.getDogStatus().toString().equals("availableDog")){
                availableDogs.add(domesticatedDog);
            }
        }
        if(availableDogs.isEmpty()){
            throw new RecordNotFoundException("There are no available dogs found");
        }
        return transferDomesticatedDogListToDtoList(availableDogs);
    }

    public List<DomesticatedDogOutputDto> getDomesticatedBreedDogs(){
        List<DomesticatedDog> domesticatedDogList = domesticatedDogRepository.findAll();
        List<DomesticatedDog> breedDogs = new ArrayList<>();
        for(DomesticatedDog domesticatedDog : domesticatedDogList){
            if(domesticatedDog.getDogStatus().toString().equals("breedDog")){
                breedDogs.add(domesticatedDog);
            }
        }
        if(breedDogs.isEmpty()){
            throw new RecordNotFoundException("There are no breed dogs found");
        }
        return transferDomesticatedDogListToDtoList(breedDogs);
    }

    public DomesticatedDogOutputDto getParentDog(Long id){
        DomesticatedDog parentDog = new DomesticatedDog();
        if(domesticatedDogRepository.existsById(id)) {
            DomesticatedDog domesticatedDog = domesticatedDogRepository.getReferenceById(id);
            Long parentId = domesticatedDog.getParentId();
            if(parentId != null) {
                if (domesticatedDogRepository.existsById(parentId)) {
                    parentDog = domesticatedDogRepository.getReferenceById(parentId);
                }
            }else{
                throw new RecordNotFoundException("No information about parent found.");
            }
        }else{
            throw new RecordNotFoundException("Dog with id " + id + " is not found.");
        }

        return transferToOutputDto(parentDog);
    }

    //Create creates a new entity in the database
    public Long createDomesticatedDog(DomesticatedDogInputDto domesticatedDogInputDto){
        //Check if a dog owner is given
        if(domesticatedDogInputDto.getPerson() != null) {
            Person dogOwner = getCompletePersonById(domesticatedDogInputDto.getPerson().getId());
            if (dogOwner == null) {
                throw new RecordNotFoundException("Provided dog owner does not exist");
            }
        }
        DomesticatedDog domesticatedDog = transferToDomesticatedDog(domesticatedDogInputDto);
        domesticatedDogRepository.save(domesticatedDog);

        return domesticatedDog.getId();
    }

    public List<DomesticatedDogOutputDto> createLitterList(List<DomesticatedDogInputDto> domesticatedDogInputDtoList, Long id){
        List<DomesticatedDogOutputDto> createdDogsFromLitterArray = new ArrayList<>();
        if(domesticatedDogRepository.findById(id).isEmpty()){
            throw new RecordNotFoundException("No dog with this id found in database");
        } else {
            for(DomesticatedDogInputDto child : domesticatedDogInputDtoList){
                    DomesticatedDog transferredDog = transferToDomesticatedDog(child);
                    transferredDog.setParentId(id);
                try {
                    domesticatedDogRepository.save(transferredDog);
                }catch(Exception ex){
                    throw new RecordNotFoundException("Adding dog to database failed");
                }
                    DomesticatedDogOutputDto createdDog = transferToOutputDto(transferredDog);
                    createdDogsFromLitterArray.add(createdDog);
            }
        }
        if (createdDogsFromLitterArray.isEmpty()) {
            throw new RecordNotFoundException("No dogs are created");
        }
        return createdDogsFromLitterArray;
    }

    //PUT sends an enclosed entity of a resource to the server.
    //If the entity already exists, the server overrides the existing object.
    //Otherwise, the server creates a new entity
    public Object updateDomesticatedDog(Long id, DomesticatedDogInputDto domesticatedDogInputDto) {
        if (domesticatedDogRepository.findById(id).isPresent()){
            DomesticatedDog domesticatedDog = domesticatedDogRepository.findById(id).get();
            DomesticatedDog updatedDog = transferToDomesticatedDog(domesticatedDogInputDto);
            if (domesticatedDogInputDto.getPerson() != null) {
                Person dogOwner = getCompletePersonById(domesticatedDogInputDto.getPerson().getId());
                if (dogOwner == null) {
                    throw new RecordNotFoundException("Provided dog owner does not exist");
                }
                updatedDog.setPerson(dogOwner);
            }
            //Keeping the former id, as we will update the existing dog
            updatedDog.setId(domesticatedDog.getId());
            domesticatedDogRepository.save(updatedDog);
            return transferToOutputDto(updatedDog);
        } else {
            Long newDogId = createDomesticatedDog(domesticatedDogInputDto);
            DomesticatedDog newDomesticatedDog = domesticatedDogRepository.getReferenceById(newDogId);
            return transferToOutputDto(newDomesticatedDog);
        }
    }

    //PATCH will only update an existing object,
    //with the properties mapped in the request body (that are not null).
    public String patchDomesticatedDog(Long id, DomesticatedDogPatchDto domesticatedDogPatchDto) {
        Optional<DomesticatedDog> domesticatedDogOptional = domesticatedDogRepository.findById(id);

        if (domesticatedDogOptional.isPresent()) {
            DomesticatedDog foundDog = domesticatedDogOptional.get();
            DomesticatedDog updatedDog = domesticatedDogRepository.getReferenceById(id);
            if (domesticatedDogPatchDto.getName() != null) {
                updatedDog.setName(domesticatedDogPatchDto.getName());
            }
            if (domesticatedDogPatchDto.getHairColor() != null) {
                updatedDog.setHairColor(domesticatedDogPatchDto.getHairColor());
            }
            if (domesticatedDogPatchDto.getFood() != null) {
                updatedDog.setFood(domesticatedDogPatchDto.getFood());
            }
            if (domesticatedDogPatchDto.getSex() != null) {
                String newSexString = domesticatedDogPatchDto.getSex();
                Sex newSex;
                //Check if given value exists in enumeration
                //Because patchDTO has no checks on enumerations,
                //We have to throw an exception if the value does not exist in the enum
                try {
                    newSex = Sex.valueOf(newSexString);
                } catch (IllegalArgumentException ex) {
                    throw new EnumValueNotFoundException("Sex is not found");
                }
                updatedDog.setSex(newSex);
            }
            String message= "";
            if (domesticatedDogPatchDto.getWeightInGrams() > 0.0) {
                double oldWeight = foundDog.getWeightInGrams();
                updatedDog.setWeightInGrams(domesticatedDogPatchDto.getWeightInGrams());
                if(domesticatedDogPatchDto.getWeightInGrams() < oldWeight){
                    message =  "WARNING: Your dog has lost weight!";
                }
            }
            if (domesticatedDogPatchDto.getKindOfHair() != null) {
                updatedDog.setKindOfHair(domesticatedDogPatchDto.getKindOfHair());
            }
            if (domesticatedDogPatchDto.getDateOfBirth() != null) {
                updatedDog.setDateOfBirth(domesticatedDogPatchDto.getDateOfBirth());
            }
            if (domesticatedDogPatchDto.getDateOfDeath() != null) {
                updatedDog.setDateOfDeath(domesticatedDogPatchDto.getDateOfDeath());
            }
            if (domesticatedDogPatchDto.getFood() != null) {
                updatedDog.setFood(domesticatedDogPatchDto.getFood());
            }
            if (domesticatedDogPatchDto.getBreed() != null) {
                String newBreedString = domesticatedDogPatchDto.getBreed();
                Breed newBreed;
                //Check if given value exists in enumeration
                //Because patchDTO has no checks on enumerations,
                //We have to throw an exception if the value does not exist in the enum
                try {
                    newBreed = Breed.valueOf(newBreedString);
                } catch (IllegalArgumentException ex) {
                    throw new EnumValueNotFoundException("Breed is not found");
                }
                updatedDog.setBreed(newBreed);
            }
            if (domesticatedDogPatchDto.getBreedGroup() != null) {
                String newBreedGroupString = domesticatedDogPatchDto.getBreedGroup();
                BreedGroup newBreedGroup;
                //Check if given value exists in enumeration
                //Because patchDTO has no checks on enumerations,
                //We have to throw an exception if the value does not exist in the enum
                try {
                    newBreedGroup = BreedGroup.valueOf(newBreedGroupString);
                } catch (IllegalArgumentException ex) {
                    throw new EnumValueNotFoundException("Breedgroup is not found");
                }
                updatedDog.setBreedGroup(newBreedGroup);
            }
            if (domesticatedDogPatchDto.getChipNumber() != null) {
                updatedDog.setChipNumber(domesticatedDogPatchDto.getChipNumber());
            }
            if (domesticatedDogPatchDto.getPerson() != null) {
                Person dogOwner = getCompletePersonById(domesticatedDogPatchDto.getPerson().getId());
                if (dogOwner == null) {
                    throw new RecordNotFoundException("Provided dog owner does not exist");
                }
                updatedDog.setPerson(dogOwner);
            }
            if (domesticatedDogPatchDto.getDogStatus() != null) {
                String newStatusString = domesticatedDogPatchDto.getDogStatus();
                Status newStatus;
                //Check if given value exists in enumeration
                //Because patchDTO has no checks on enumerations,
                //We have to throw an exception if the value does not exist in the enum
                try {
                    newStatus = Status.valueOf(newStatusString);
                } catch (IllegalArgumentException ex) {
                    throw new EnumValueNotFoundException("Status is not found");
                }
                updatedDog.setDogStatus(newStatus);
            }
            domesticatedDogRepository.save(updatedDog);
            return "Your dog has been updated. " + message;

        } else {
            throw new RecordNotFoundException("Dog is not found.");
        }
    }

    public Long storeDogImage(Long id, MultipartFile file) throws IOException {
        if(file.isEmpty()) {
            throw new IOException();
        }
        Optional<DomesticatedDog> optionalDog = domesticatedDogRepository.findById(id);
        if(optionalDog.isPresent()) {
            DomesticatedDog domesticatedDog = optionalDog.get();
            domesticatedDog.setDogImage(file.getBytes());
            domesticatedDogRepository.save(domesticatedDog);
            return domesticatedDog.getId();
        } else {
            throw new RecordNotFoundException("Dog is not found.");
        }
    }

    public void deleteDogImage(Long id) {
        Optional<DomesticatedDog> optionalDog = domesticatedDogRepository.findById(id);
        if(optionalDog.isPresent()) {
            DomesticatedDog domesticatedDog = optionalDog.get();
            if(domesticatedDog.getDogImage() != null) {
                domesticatedDog.setDogImage(null);
                domesticatedDogRepository.save(domesticatedDog);
            }else{
                throw new  RecordNotFoundException("This dog does not have a picture.");
            }
        } else {
            throw new  RecordNotFoundException("No dog with given ID found.");
        }
    }
    //DELETE deletes an entity from the database if found
    public void deleteDomesticatedDog(Long id) {

        if (domesticatedDogRepository.findById(id).isPresent()){
            DomesticatedDog dogToDelete = domesticatedDogRepository.getReferenceById(id);
            //Delete appointments for dog
            List<VeterinarianAppointment> dogVeterinarianAppointments = dogToDelete.getVeterinarianAppointments();
            for(VeterinarianAppointment veterinarianAppointment : dogVeterinarianAppointments){
                veterinarianAppointmentRepository.delete(veterinarianAppointment);
            }
            //delete medical data for dog
            List<MedicalData> dogMedicalData = dogToDelete.getMedicalData();
            for(MedicalData medicalData : dogMedicalData){
                medicalDataRepository.delete(medicalData);
            }
            domesticatedDogRepository.deleteById(id);
        } else {
            throw new  RecordNotFoundException("No dogs with given ID found.");
        }
    }

    //DTO helper classes
    private List<DomesticatedDogOutputDto> transferDomesticatedDogListToDtoList(List<DomesticatedDog> domesticatedDogs){
        List<DomesticatedDogOutputDto> domesticatedDogDtoList = new ArrayList<>();

        for(DomesticatedDog domesticatedDog : domesticatedDogs) {
            DomesticatedDogOutputDto dto = transferToOutputDto(domesticatedDog);
            domesticatedDogDtoList.add(dto);
        }
        return domesticatedDogDtoList;
    }

    private DomesticatedDogOutputDto transferToOutputDto(DomesticatedDog domesticatedDog){

        DomesticatedDogOutputDto domesticatedDogDto = new DomesticatedDogOutputDto();
        domesticatedDogDto.setId(domesticatedDog.getId());
        domesticatedDogDto.setName(domesticatedDog.getName());
        domesticatedDogDto.setHairColor(domesticatedDog.getHairColor());
        domesticatedDogDto.setFood(domesticatedDog.getFood());
        domesticatedDogDto.setSex(domesticatedDog.getSex());
        domesticatedDogDto.setWeightInGrams(domesticatedDog.getWeightInGrams());
        domesticatedDogDto.setKindOfHair(domesticatedDog.getKindOfHair());
        domesticatedDogDto.setDogYears(domesticatedDog.getDogYears());
        domesticatedDogDto.setDateOfBirth(domesticatedDog.getDateOfBirth());
        domesticatedDogDto.setDateOfDeath(domesticatedDog.getDateOfDeath());
        domesticatedDogDto.setChipNumber(domesticatedDog.getChipNumber());
        domesticatedDogDto.setBreed(domesticatedDog.getBreed());
        domesticatedDogDto.setBreedGroup(domesticatedDog.getBreedGroup());
        domesticatedDogDto.setPerson(domesticatedDog.getPerson());
        domesticatedDogDto.setDogImage(domesticatedDog.getDogImage());
        domesticatedDogDto.setCanSee(domesticatedDog.canSee());
        domesticatedDogDto.setCanHear(domesticatedDog.canHear());
        Long parentId = domesticatedDog.getParentId();
        if(parentId != null) {
            Optional<DomesticatedDog> parentDog = domesticatedDogRepository.findById(parentId);
            if (parentDog.isPresent()) {
                domesticatedDogDto.setParentId(domesticatedDog.getParentId());
            } else {
                throw new RecordNotFoundException("Parent ID not found");
            }
        }
        //Because of recursion when getting a list of domesticated dogs from entity DomesticatedDog,
        //we limit the litter to fields that are of interest for a dog owner
        List<DomesticatedDog> litters = domesticatedDog.getLitter();
        List<DomesticatedDog> newLitters = new ArrayList<>();
        if(litters != null){
                for (DomesticatedDog dog : litters) {
                    DomesticatedDog newDog = new DomesticatedDog();
                    newDog.setId(dog.getId());
                    newDog.setName(dog.getName());
                    newDog.setKindOfHair(dog.getKindOfHair());
                    newDog.setBreed(dog.getBreed());
                    newDog.setSex(dog.getSex());
                    newDog.setDateOfBirth(dog.getDateOfBirth());
                    newLitters.add(newDog);
                }
        }
        domesticatedDogDto.setLitters(newLitters);
        List<VeterinarianAppointment> veterinarianAppointments = domesticatedDog.getVeterinarianAppointments();
        if(veterinarianAppointments != null) {
            domesticatedDogDto.setVeterinarianAppointments(domesticatedDog.getVeterinarianAppointments());
        }
        List<MedicalData> medicalData = domesticatedDog.getMedicalData();
        if(medicalData != null){
            domesticatedDogDto.setMedicalData(domesticatedDog.getMedicalData());
        }

        domesticatedDogDto.setDogStatus(domesticatedDog.getDogStatus());
        return domesticatedDogDto;
    }

    private DomesticatedDog transferToDomesticatedDog(DomesticatedDogInputDto dto){
        DomesticatedDog domesticatedDog = new DomesticatedDog();

        domesticatedDog.setName(dto.getName());
        domesticatedDog.setHairColor(dto.getHairColor());
        domesticatedDog.setFood(dto.getFood());
        if (dto.getSex() != null) {
            String newSexString = dto.getSex();
            Sex newSex;
            //Check if given value exists in enumeration
            //Because patchDTO has no checks on enumerations,
            //We have to throw an exception if the value does not exist in the enum
            try {
                newSex = Sex.valueOf(newSexString);
            } catch (IllegalArgumentException ex) {
                throw new EnumValueNotFoundException("Sex is not found for dog");
            }
            domesticatedDog.setSex(newSex);
        }
        domesticatedDog.setWeightInGrams(dto.getWeightInGrams());
        domesticatedDog.setKindOfHair(dto.getKindOfHair());
        domesticatedDog.setDogYears(dto.getDogYears());
        domesticatedDog.setDateOfBirth(dto.getDateOfBirth());
        domesticatedDog.setDateOfDeath(dto.getDateOfDeath());
        domesticatedDog.setChipNumber(dto.getChipNumber());
        domesticatedDog.setDogYears(dto.getDogYears());
        domesticatedDog.setCanSee(dto.isCanSee());
        domesticatedDog.setCanHear(dto.isCanHear());
        if (dto.getBreed() != null) {
            String newBreedString = dto.getBreed();
            Breed newBreed;
            //Check if given value exists in enumeration
            //Because patchDTO has no checks on enumerations,
            //We have to throw an exception if the value does not exist in the enum
            try {
                newBreed = Breed.valueOf(newBreedString);
            } catch (IllegalArgumentException ex) {
                throw new EnumValueNotFoundException("Breed is not found");
            }
            domesticatedDog.setBreed(newBreed);
        }
        if (dto.getBreedGroup() != null) {
            String newBreedGroupString = dto.getBreedGroup();
            BreedGroup newBreedGroup;
            //Check if given value exists in enumeration
            //Because patchDTO has no checks on enumerations,
            //We have to throw an exception if the value does not exist in the enum
            try {
                newBreedGroup = BreedGroup.valueOf(newBreedGroupString);
            } catch (IllegalArgumentException ex) {
                throw new EnumValueNotFoundException("Breedgroup is not found");
            }
            domesticatedDog.setBreedGroup(newBreedGroup);
        }
        domesticatedDog.setPerson(dto.getPerson());

        Long parentId = dto.getParentId();
        if(parentId != null) {
            Optional<DomesticatedDog> parentDog = domesticatedDogRepository.findById(parentId);
            if (parentDog.isPresent()) {
                domesticatedDog.setParentId(dto.getParentId());
            } else {
                throw new RecordNotFoundException("Parent ID not found");
            }
        }
        if(dto.getDogStatus() != null) {
            String newStatusString = String.valueOf(dto.getDogStatus());
            Status newStatus;
            //Check if given value exists in enumeration
            //Because patchDTO has no checks on enumerations,
            //We have to throw an exception if the value does not exist in the enum
            try {
                newStatus = Status.valueOf(newStatusString);
            } catch (IllegalArgumentException ex) {
                throw new EnumValueNotFoundException("Status is not found");
            }
            domesticatedDog.setDogStatus(newStatus);

            List<DomesticatedDog> litters = dto.getLitters();
            if(litters != null){
                domesticatedDog.setLitter(dto.getLitters());
            }
            List<VeterinarianAppointment> veterinarianAppointments = dto.getVeterinarianAppointments();
            if(veterinarianAppointments != null){
                domesticatedDog.setVeterinarianAppointments(dto.getVeterinarianAppointments());
            }

            List<MedicalData> medicalData = dto.getMedicalData();
            if(medicalData != null){
                domesticatedDog.setMedicalData(dto.getMedicalData());
            }
        }
        return domesticatedDog;
    }

    /**
     * Get all person information recorded by give ID.
     * @param personId ID of the person information requested
     * @return Person or null if not present.
     */
    private Person getCompletePersonById(Long personId) {
        if (personId == 0) {
            throw new RecordNotFoundException("Missing Person ID");
        }
        Optional<Person> person = personRepository.findById(personId);
        return person.orElse(null);
    }

}
