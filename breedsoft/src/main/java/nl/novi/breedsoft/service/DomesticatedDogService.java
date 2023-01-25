package nl.novi.breedsoft.service;

import nl.novi.breedsoft.dto.domesticatedDogDtos.DomesticatedDogInputDto;
import nl.novi.breedsoft.dto.domesticatedDogDtos.DomesticatedDogOutputDto;
import nl.novi.breedsoft.dto.domesticatedDogDtos.DomesticatedDogPatchDto;
import nl.novi.breedsoft.exception.EnumValueNotFoundException;
import nl.novi.breedsoft.exception.IncorrectInputException;
import nl.novi.breedsoft.exception.RecordNotFoundException;
import nl.novi.breedsoft.model.management.VeterinarianAppointment;
import nl.novi.breedsoft.model.management.DomesticatedDog;
import nl.novi.breedsoft.model.management.MedicalData;
import nl.novi.breedsoft.model.management.Person;
import nl.novi.breedsoft.model.management.enumerations.Status;
import nl.novi.breedsoft.repository.VeterinarianAppointmentRepository;
import nl.novi.breedsoft.repository.MedicalDataRepository;
import nl.novi.breedsoft.model.management.enumerations.Breed;
import nl.novi.breedsoft.model.management.enumerations.BreedGroup;
import nl.novi.breedsoft.model.animal.enumerations.Sex;
import nl.novi.breedsoft.repository.DomesticatedDogRepository;
import nl.novi.breedsoft.utility.EnumValidator;
import nl.novi.breedsoft.utility.RepositoryUtility;
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
    private final VeterinarianAppointmentRepository veterinarianAppointmentRepository;
    private final MedicalDataRepository medicalDataRepository;

    private final RepositoryUtility repositoryUtility;

    //Constructor injection
    public DomesticatedDogService(
            DomesticatedDogRepository domesticatedDogRepository,
            VeterinarianAppointmentRepository veterinarianAppointmentRepository,
            MedicalDataRepository medicalDataRepository,
            RepositoryUtility repositoryUtility
    ){
        this.domesticatedDogRepository = domesticatedDogRepository;
        this.veterinarianAppointmentRepository = veterinarianAppointmentRepository;
        this.medicalDataRepository = medicalDataRepository;
        this.repositoryUtility = repositoryUtility;
    }

    /**
     * A method for retrieval of all domesticated dogs from the database
     * @return a list of all domesticated dogs in output dto format
     */
    public List<DomesticatedDogOutputDto> getAllDomesticatedDogs() {
        List<DomesticatedDog> domesticatedDogList = domesticatedDogRepository.findAll();
        return transferDomesticatedDogListToOutputDtoList(domesticatedDogList);
    }

    /**
     * A method for retrieval of one domesticated dog from the database by id
     * @param domesticatedDogId ID of the domesticated dog for which information is requested
     * @return a domesticated dog in output dto format
     * @throws RecordNotFoundException throws an exception when no domesticated dog is found by given id
     */
    public DomesticatedDogOutputDto getDomesticatedDogById(Long domesticatedDogId) {
        if (domesticatedDogRepository.findById(domesticatedDogId).isPresent()){
            DomesticatedDog domesticatedDog = domesticatedDogRepository.findById(domesticatedDogId).get();
            return transferToOutputDto(domesticatedDog);
        } else {
            throw new RecordNotFoundException("Dog not found in database.");
        }
    }

    /**
     * A method to retrieve a list of domesticated dogs based on name
     * @param domesticatedDogName name of the person for which information is requested
     * @return a list of all domesticated dogs in output dto format
     * @throws RecordNotFoundException throws an exception when no domesticated dog is found by name
     */
    public List<DomesticatedDogOutputDto> getDomesticatedDogByName(String domesticatedDogName) {
        if (domesticatedDogRepository.findByNameContaining(domesticatedDogName).isEmpty()){
            throw new RecordNotFoundException("No dog with this name found in database.");
        } else {
            List<DomesticatedDog> domesticatedDogList = domesticatedDogRepository.findByNameContaining(domesticatedDogName);
            List<DomesticatedDogOutputDto> domesticatedDogOutputDtoList = new ArrayList<>();
            for(DomesticatedDog domesticatedDog : domesticatedDogList){
                domesticatedDogOutputDtoList.add(transferToOutputDto(domesticatedDog));
            }
            return domesticatedDogOutputDtoList;
        }
    }

    /**
     * A method to retrieve al children from a domesticated dog by domesticated dog ID
     * @param domesticatedDogId ID of the domesticated dog for which information is requested
     * @return a list of domesticated dogs in output dto format
     * @throws RecordNotFoundException throws an exception when no domesticated dog is found by ID,
     * or no children are found for domesticated dog
     */
    public List<DomesticatedDogOutputDto> getAllChildren(Long domesticatedDogId){
        List<DomesticatedDog> children = new ArrayList<>();

        if(domesticatedDogRepository.findById(domesticatedDogId).isEmpty()){
            throw new RecordNotFoundException("No dog with this id found in database.");
        } else {
            List<DomesticatedDog> allDomesticatedDogs = domesticatedDogRepository.findAll();
            for(DomesticatedDog domesticatedDog : allDomesticatedDogs){
                if(Objects.equals(domesticatedDog.getParentId(), domesticatedDogId)){
                    children.add(domesticatedDog);
                }
            }
            if(children.isEmpty()){
                throw new RecordNotFoundException("This dog doesn't have children.");
            }
        }

        return transferDomesticatedDogListToOutputDtoList(children);
    }

    /**
     * A method that retrieves a list of available domesticated dogs from the database
     * @return a list of available domesticated dogs (based on status) in output dto format
     * @throws RecordNotFoundException throws an exception when no available domesticated dogs are found
     */
    public List<DomesticatedDogOutputDto> getAvailableDomesticatedDogs(){
        List<DomesticatedDog> domesticatedDogList = domesticatedDogRepository.findAll();
        List<DomesticatedDog> availableDogs = new ArrayList<>();
        for(DomesticatedDog domesticatedDog : domesticatedDogList){
            Status domesticatedDogStatus = domesticatedDog.getDogStatus();
            if(domesticatedDogStatus ==  Status.availablePup || domesticatedDogStatus == Status.availablePreOwned){
                availableDogs.add(domesticatedDog);
            }
        }
        if(availableDogs.isEmpty()){
            throw new RecordNotFoundException("There are no available dogs found.");
        }
        return transferDomesticatedDogListToOutputDtoList(availableDogs);
    }

    /**
     * A method to retrieve a list of domesticated breed dogs from the database
     * @return a list of domesticated breed dogs in output dto format
     */
    public List<DomesticatedDogOutputDto> getDomesticatedBreedDogs(){
        List<DomesticatedDog> domesticatedDogList = domesticatedDogRepository.findAll();
        List<DomesticatedDog> breedDogs = new ArrayList<>();
        for(DomesticatedDog domesticatedDog : domesticatedDogList){
            if(domesticatedDog.getDogStatus() == Status.breedDog){
                breedDogs.add(domesticatedDog);
            }
        }
        if(breedDogs.isEmpty()){
            throw new RecordNotFoundException("There are no breed dogs found.");
        }
        return transferDomesticatedDogListToOutputDtoList(breedDogs);
    }

    /**
     * A method that retrieves the parent of a domesticated dog based on ID
     * @param domesticatedDogId ID of the domesticated dog for which information is requested
     * @return a domesticated dog in output dto format
     * @throws RecordNotFoundException throws an exception when no parent information is found,
     * or when there is no domesticated dog found based on given domesticated dog ID
     */
    public DomesticatedDogOutputDto getParentDog(Long domesticatedDogId){
        DomesticatedDog parentDog = new DomesticatedDog();
        if(domesticatedDogRepository.existsById(domesticatedDogId)) {
            DomesticatedDog domesticatedDog = domesticatedDogRepository.getReferenceById(domesticatedDogId);
            Long parentId = domesticatedDog.getParentId();
            if(parentId != null) {
                if (domesticatedDogRepository.existsById(parentId)) {
                    parentDog = domesticatedDogRepository.getReferenceById(parentId);
                }
            }else{
                throw new RecordNotFoundException("No information about parent found.");
            }
        }else{
            throw new RecordNotFoundException("Dog with id " + domesticatedDogId + " is not found.");
        }

        return transferToOutputDto(parentDog);
    }

    /**
     * A method to create a new domesticated dog in the database
     * @param domesticatedDogInputDto Data Transfer Objects that carries data between processes in order to reduce the number of methods calls
     * @return the ID of the domesticated dog created in the database
     * @throws RecordNotFoundException throws an exception when the provided dog owner does not exist
     */
    public Long createDomesticatedDog(DomesticatedDogInputDto domesticatedDogInputDto){
        //Check if a dog owner is given
        if(domesticatedDogInputDto.getPerson() != null) {
            Person dogOwner = repositoryUtility.getCompletePersonById(domesticatedDogInputDto.getPerson().getId());
            if (dogOwner == null) {
                throw new RecordNotFoundException("Provided dog owner does not exist.");
            }
        }
        DomesticatedDog domesticatedDog = transferToDomesticatedDog(domesticatedDogInputDto);
        domesticatedDog = domesticatedDogRepository.save(domesticatedDog);
        return domesticatedDog.getId();
    }

    /**
     * A method that creates a litter of domesticated dogs,
     * creates multiple domesticated dogs in the database from a list of domesticated dogs
     * @param domesticatedDogInputDtoList Data Transfer Objects that carries data between processes in order to reduce the number of methods calls
     * @param domesticatedDogId ID of the domesticated dog for which information is requested
     * @return a list of domesticated dogs in output dto format
     * @throws RecordNotFoundException throws an exception when no domesticated dog is found based on id,
     * @throws IllegalStateException throws an exception when list is empty
     */
    public List<DomesticatedDogOutputDto> createLitterList(
            List<DomesticatedDogInputDto> domesticatedDogInputDtoList,
            Long domesticatedDogId
    ){
        List<DomesticatedDogOutputDto> createdDogsFromLitterArray = new ArrayList<>();
        if(domesticatedDogRepository.findById(domesticatedDogId).isEmpty()){
            throw new RecordNotFoundException("No dog with this id found in database");
        } else {
            for(DomesticatedDogInputDto child : domesticatedDogInputDtoList){
                DomesticatedDog transferredDog = transferToDomesticatedDog(child);
                transferredDog.setParentId(domesticatedDogId);

                domesticatedDogRepository.save(transferredDog);
                DomesticatedDogOutputDto createdDog = transferToOutputDto(transferredDog);
                createdDogsFromLitterArray.add(createdDog);
            }
            if (createdDogsFromLitterArray.isEmpty()) {
                throw new IncorrectInputException("Empty array. No dogs are created");
            }
        }

        return createdDogsFromLitterArray;
    }

    /**
     * A method (PUT) sends an enclosed entity of a resource to the server.
     * If the entity already exists, the server overrides the existing object,
     * otherwise the server creates a new entity.
     * @param domesticatedDogId ID of the domesticated for which an update or creation is requested
     * @param domesticatedDogInputDto Data Transfer Objects that carries data between processes in order to reduce the number of methods calls
     * @return a new or updated domesticated dog in output dto format
     * @throws RecordNotFoundException throws an exception when dog owner is not found based on ID
     */
    public DomesticatedDogOutputDto updateDomesticatedDog(Long domesticatedDogId, DomesticatedDogInputDto domesticatedDogInputDto) {
        Optional<DomesticatedDog> optionalDomesticatedDog = domesticatedDogRepository.findById(domesticatedDogId);
        if (optionalDomesticatedDog.isPresent()){
            DomesticatedDog updatedDog = transferToDomesticatedDog(domesticatedDogInputDto);
            if (domesticatedDogInputDto.getPerson() != null) {
                Person dogOwner = repositoryUtility.getCompletePersonById(domesticatedDogInputDto.getPerson().getId());
                if (dogOwner == null) {
                    throw new RecordNotFoundException("Provided dog owner does not exist");
                }
                updatedDog.setPerson(dogOwner);
            }
            //Keeping the former id, as we will update the existing dog
            updatedDog.setId(domesticatedDogId);
            domesticatedDogRepository.save(updatedDog);
            return transferToOutputDto(updatedDog);
        } else {
            Long newDogId = createDomesticatedDog(domesticatedDogInputDto);
            DomesticatedDog newDomesticatedDog = domesticatedDogRepository.getReferenceById(newDogId);
            return transferToOutputDto(newDomesticatedDog);
        }
    }

    /**
     * A method (PATCH) will only update an existing object,
     * with the properties mapped in the request body (that are not null).
     * We do NOT update veterinarian appointment and medical data here.
     * @param domesticatedDogId ID of the domesticated dog for which an update is requested
     * @param domesticatedDogPatchDto Data Transfer Objects that carries data between processes in order to reduce the number of methods calls
     * @return a message stating that the domesticated dog is successfully updated,
     * plus an optional String warning based on the weight of the domesticated dog
     * @throws EnumValueNotFoundException throws an exception when the provided enum value is not found in enum
     * @throws RecordNotFoundException throws an exception when domesticated dog / dog owner is not found based on ID
     */
    public String patchDomesticatedDog(Long domesticatedDogId, DomesticatedDogPatchDto domesticatedDogPatchDto) {
        Optional<DomesticatedDog> domesticatedDogOptional = domesticatedDogRepository.findById(domesticatedDogId);

        if (domesticatedDogOptional.isPresent()) {
            DomesticatedDog foundDog = domesticatedDogOptional.get();
            DomesticatedDog updatedDog = domesticatedDogRepository.getReferenceById(domesticatedDogId);
            if (domesticatedDogPatchDto.getName() != null) {
                updatedDog.setName(domesticatedDogPatchDto.getName());
            }
            if (domesticatedDogPatchDto.getHairColor() != null) {
                updatedDog.setHairColor(domesticatedDogPatchDto.getHairColor());
            }
            if (domesticatedDogPatchDto.getFood() != null) {
                updatedDog.setFood(domesticatedDogPatchDto.getFood());
            }
            if (EnumValidator.validateEnumValue(Sex.class, domesticatedDogPatchDto.getSex())) {
                updatedDog.setSex(Sex.valueOf(domesticatedDogPatchDto.getSex()));
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
            if (EnumValidator.validateEnumValue(Breed.class, domesticatedDogPatchDto.getBreed())) {
                updatedDog.setBreed(Breed.valueOf(domesticatedDogPatchDto.getBreed()));
            }
            if (EnumValidator.validateEnumValue(BreedGroup.class, domesticatedDogPatchDto.getBreedGroup())) {
                updatedDog.setBreedGroup(BreedGroup.valueOf(domesticatedDogPatchDto.getBreedGroup()));
            }
            if (domesticatedDogPatchDto.getChipNumber() != null) {
                updatedDog.setChipNumber(domesticatedDogPatchDto.getChipNumber());
            }
            if (domesticatedDogPatchDto.getPerson() != null) {
                Person dogOwner = repositoryUtility.getCompletePersonById(domesticatedDogPatchDto.getPerson().getId());
                if (dogOwner == null) {
                    throw new RecordNotFoundException("Provided dog owner does not exist");
                }
                updatedDog.setPerson(dogOwner);
            }
            if (EnumValidator.validateEnumValue(Status.class, domesticatedDogPatchDto.getDogStatus())) {
                updatedDog.setDogStatus(Status.valueOf(domesticatedDogPatchDto.getDogStatus()));
            }
            domesticatedDogRepository.save(updatedDog);

            return "Your dog has been updated. " + message;
        } else {
            throw new RecordNotFoundException("Dog is not found.");
        }
    }

    /**
     * A method that stores an image in the database
     * @param domesticatedDogId ID of the domesticated dog for which the image is stored
     * @param multipartFile the image of the domesticated dog
     * @return the ID of the domesticated dog for which an image was stored
     * @throws IOException throws an exception when the I/O operation failed or is interrupted
     * @throws RecordNotFoundException throws an exception when domesticated dog is not found by id
     */
    public Long storeDogImage(Long domesticatedDogId, MultipartFile multipartFile) throws IOException {
        if(multipartFile.isEmpty()) {
            throw new IOException();
        }
        Optional<DomesticatedDog> optionalDog = domesticatedDogRepository.findById(domesticatedDogId);
        if(optionalDog.isPresent()) {
            DomesticatedDog domesticatedDog = optionalDog.get();
            domesticatedDog.setDogImage(multipartFile.getBytes());
            domesticatedDogRepository.save(domesticatedDog);
            return domesticatedDog.getId();
        } else {
            throw new RecordNotFoundException("Dog is not found.");
        }
    }

    /**
     * A method for deleting an image of a domesticated dog from the database by id
     * @param domesticatedDogId ID of the domesticated dog for which deletion is requested
     * @throws RecordNotFoundException throws an exception when the domesticated dog is not found,
     * or when the domesticated dog does not have a picture
     */
    public void deleteDogImage(Long domesticatedDogId) {
        Optional<DomesticatedDog> optionalDog = domesticatedDogRepository.findById(domesticatedDogId);
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

    /**
     * A method for deleting a domesticated dog from the database by id
     * @param domesticatedDogId ID of the domesticated dog for which deletion is requested
     * @throws RecordNotFoundException throws an exception when the domesticated dog is not found
     */
    public void deleteDomesticatedDog(Long domesticatedDogId) {

        if (domesticatedDogRepository.findById(domesticatedDogId).isPresent()){
            DomesticatedDog dogToDelete = domesticatedDogRepository.getReferenceById(domesticatedDogId);
            //Delete appointments for domesticated dog
            List<VeterinarianAppointment> dogVeterinarianAppointments = dogToDelete.getVeterinarianAppointments();
            for(VeterinarianAppointment veterinarianAppointment : dogVeterinarianAppointments){
                    veterinarianAppointmentRepository.delete(veterinarianAppointment);
            }
            //delete medical data for domesticated dog
            List<MedicalData> dogMedicalData = dogToDelete.getMedicalData();
            for(MedicalData medicalData : dogMedicalData){
                medicalDataRepository.delete(medicalData);
            }
            //delete domesticated dog
            domesticatedDogRepository.deleteById(domesticatedDogId);
        } else {
            throw new  RecordNotFoundException("No dog with given ID found.");
        }
    }

    //DTO helper classes
    /**
     * A method to transform a list with domesticated dog to a list of domesticated dogs in output dto format
     * @param domesticatedDogList list of domesticated dogs to be transformed
     * @return a list of domesticated dogs in output dto format
     */
    private List<DomesticatedDogOutputDto> transferDomesticatedDogListToOutputDtoList(List<DomesticatedDog> domesticatedDogList){
        List<DomesticatedDogOutputDto> domesticatedDogDtoList = new ArrayList<>();

        for(DomesticatedDog domesticatedDog : domesticatedDogList) {
            DomesticatedDogOutputDto dto = transferToOutputDto(domesticatedDog);
            domesticatedDogDtoList.add(dto);
        }
        return domesticatedDogDtoList;
    }

    /**
     * A method to transform a domesticated dog to a domesticated dog in output dto format
     * @param domesticatedDog domesticated dog to be transformed
     * @return a domesticated dogs in output dto format
     * @throws RecordNotFoundException throws an exception when parent id is not found
     */
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

    /**
     * A method to transform a domesticated dog in input dto format to a domesticated dog format
     * @param domesticatedDogInputDto Data Transfer Objects that carries data between processes in order to reduce the number of methods calls
     * @return domesticatedDog in domesticated dog format
     */
    private DomesticatedDog transferToDomesticatedDog(DomesticatedDogInputDto domesticatedDogInputDto){
        DomesticatedDog domesticatedDog = new DomesticatedDog();

        domesticatedDog.setName(domesticatedDogInputDto.getName());
        domesticatedDog.setHairColor(domesticatedDogInputDto.getHairColor());
        domesticatedDog.setFood(domesticatedDogInputDto.getFood());
        if (domesticatedDogInputDto.getSex() != null) {
            String newSexString = domesticatedDogInputDto.getSex();
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
        domesticatedDog.setWeightInGrams(domesticatedDogInputDto.getWeightInGrams());
        domesticatedDog.setKindOfHair(domesticatedDogInputDto.getKindOfHair());
        domesticatedDog.setDogYears(domesticatedDogInputDto.getDogYears());
        domesticatedDog.setDateOfBirth(domesticatedDogInputDto.getDateOfBirth());
        domesticatedDog.setDateOfDeath(domesticatedDogInputDto.getDateOfDeath());
        domesticatedDog.setChipNumber(domesticatedDogInputDto.getChipNumber());
        domesticatedDog.setDogYears(domesticatedDogInputDto.getDogYears());
        domesticatedDog.setCanSee(domesticatedDogInputDto.isCanSee());
        domesticatedDog.setCanHear(domesticatedDogInputDto.isCanHear());
        if (domesticatedDogInputDto.getBreed() != null) {
            String newBreedString = domesticatedDogInputDto.getBreed();
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
        if (domesticatedDogInputDto.getBreedGroup() != null) {
            String newBreedGroupString = domesticatedDogInputDto.getBreedGroup();
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
        domesticatedDog.setPerson(domesticatedDogInputDto.getPerson());

        Long parentId = domesticatedDogInputDto.getParentId();
        if(parentId != null) {
            Optional<DomesticatedDog> parentDog = domesticatedDogRepository.findById(parentId);
            if (parentDog.isPresent()) {
                domesticatedDog.setParentId(domesticatedDogInputDto.getParentId());
            } else {
                throw new RecordNotFoundException("Parent ID not found.");
            }
        }
        if(domesticatedDogInputDto.getDogStatus() != null) {
            String newStatusString = domesticatedDogInputDto.getDogStatus();
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

            List<DomesticatedDog> litters = domesticatedDogInputDto.getLitters();
            if(litters != null){
                domesticatedDog.setLitter(domesticatedDogInputDto.getLitters());
            }
            List<VeterinarianAppointment> veterinarianAppointments = domesticatedDogInputDto.getVeterinarianAppointments();
            if(veterinarianAppointments != null){
                domesticatedDog.setVeterinarianAppointments(domesticatedDogInputDto.getVeterinarianAppointments());
            }

            List<MedicalData> medicalData = domesticatedDogInputDto.getMedicalData();
            if(medicalData != null){
                domesticatedDog.setMedicalData(domesticatedDogInputDto.getMedicalData());
            }
        }
        return domesticatedDog;
    }
}
