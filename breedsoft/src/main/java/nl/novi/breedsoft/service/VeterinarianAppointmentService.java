package nl.novi.breedsoft.service;

import nl.novi.breedsoft.dto.domesticatedDogDtos.DomesticatedDogOutputDto;
import nl.novi.breedsoft.dto.veterinarianAppointmentDtos.VeterinarianAppointmentInputDto;
import nl.novi.breedsoft.dto.veterinarianAppointmentDtos.VeterinarianAppointmentOutputDto;
import nl.novi.breedsoft.dto.veterinarianAppointmentDtos.VeterinarianAppointmentPatchDto;
import nl.novi.breedsoft.exception.IncorrectInputException;
import nl.novi.breedsoft.exception.RecordNotFoundException;
import nl.novi.breedsoft.model.management.DomesticatedDog;
import nl.novi.breedsoft.model.management.VeterinarianAppointment;
import nl.novi.breedsoft.repository.VeterinarianAppointmentRepository;
import nl.novi.breedsoft.repository.DomesticatedDogRepository;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class VeterinarianAppointmentService {

    private final VeterinarianAppointmentRepository veterinarianAppointmentRepository;
    private final DomesticatedDogRepository domesticatedDogRepository;

    public VeterinarianAppointmentService(
            VeterinarianAppointmentRepository veterinarianAppointmentRepository,
            DomesticatedDogRepository domesticatedDogRepository
    ) {
        this.veterinarianAppointmentRepository = veterinarianAppointmentRepository;
        this.domesticatedDogRepository = domesticatedDogRepository;
    }

    /**

     * A method for retrieval of all veterinarian appointments from the database
     * @return a list of all veterinarian appointments in output dto format
     */
    public List<VeterinarianAppointmentOutputDto> getAllAppointments(){
        List<VeterinarianAppointment> allAppointmentsList = veterinarianAppointmentRepository.findAll();
        return transferAppointmentListToOutputDtoList(allAppointmentsList);
    }

    /**
     * A method for retrieval of one veterinarian appointment from the database by id
     * @param domesticatedDogId ID of the veterinarian appointment for which information is requested
     * @return a veterinarian appointment in output dto format
     * @throws RecordNotFoundException throws an exception when veterinarian appointment is not found by id
     */
    public List <VeterinarianAppointmentOutputDto> getAllAppointmentsByDogId(Long domesticatedDogId){
        List<VeterinarianAppointment> allAppointmentsList = veterinarianAppointmentRepository.findAll();
        List<VeterinarianAppointment> dogVeterinarianAppointmentList = new ArrayList<>();

        for(VeterinarianAppointment veterinarianAppointment : allAppointmentsList){
            Long dogId = veterinarianAppointment.getDomesticatedDog().getId();

            if(dogId.equals(domesticatedDogId)){
              dogVeterinarianAppointmentList.add(veterinarianAppointment);
            }
        }
        if(dogVeterinarianAppointmentList.isEmpty()){
            throw new RecordNotFoundException("There are no appointments found for this dog");
        }
        return transferAppointmentListToOutputDtoList(dogVeterinarianAppointmentList);
    }

    /**
     * A method to create a new veterinarian appointment in the database
     * @param veterinarianAppointmentInputDto Data Transfer Objects that carries data between processes in order to reduce the number of methods calls
     * @return the ID of the veterinarian appointment created in the database
     * @throws RecordNotFoundException throws an exception when provided dog ID is not found in the database
     */
    public Long createVeterinarianAppointment(VeterinarianAppointmentInputDto veterinarianAppointmentInputDto){
        //Check if a dog is given
        if(veterinarianAppointmentInputDto.getDomesticatedDog() != null) {
            DomesticatedDog dog = getCompleteDogById(veterinarianAppointmentInputDto.getDomesticatedDog().getId());
            if (dog == null) {
                throw new RecordNotFoundException("Provided dog does not exist in the database");
            }
            veterinarianAppointmentInputDto.setDomesticatedDog(dog);
        }
        VeterinarianAppointment veterinarianAppointment = transferToVeterinarianAppointment(veterinarianAppointmentInputDto);
        veterinarianAppointmentRepository.save(veterinarianAppointment);

        return veterinarianAppointment.getId();
    }

    /**
     * A method (PUT) sends an enclosed entity of a resource to the server.
     * If the entity already exists, the server overrides the existing object,
     * otherwise the server creates a new entity.
     * @param veterinarianAppointmentId ID of the veterinarian appointment for which an update is requested
     * @param veterinarianAppointmentInputDto Data Transfer Objects that carries data between processes in order to reduce the number of methods calls
     * @return a new or updated veterinarian appointment in output dto format
     * @throws RecordNotFoundException throws an exception when domesticated dog is not found by dog ID
     */
    public Object updateVeterinarianAppointment(
            Long veterinarianAppointmentId,
            VeterinarianAppointmentInputDto veterinarianAppointmentInputDto
    ){
        if(veterinarianAppointmentRepository.findById(veterinarianAppointmentId).isPresent()){
            VeterinarianAppointment veterinarianAppointment = veterinarianAppointmentRepository.findById(veterinarianAppointmentId).get();
            VeterinarianAppointment updateVeterinarianAppointment = transferToVeterinarianAppointment(veterinarianAppointmentInputDto);
            if(veterinarianAppointmentInputDto.getDomesticatedDog() != null) {
                DomesticatedDog dog = getCompleteDogById(veterinarianAppointmentInputDto.getDomesticatedDog().getId());
                if(dog == null){
                    throw new RecordNotFoundException("Provided dog does not exist");
                }
                updateVeterinarianAppointment.setDomesticatedDog(dog);
            }
            //Keeping the former id, as we will update the existing appointment
            updateVeterinarianAppointment.setId(veterinarianAppointment.getId());
            veterinarianAppointmentRepository.save(updateVeterinarianAppointment);
            return transferVeterinarianAppointmentToOutputDto(veterinarianAppointment);
        } else {
            Long newAppointmentId = createVeterinarianAppointment(veterinarianAppointmentInputDto);
            VeterinarianAppointment newVeterinarianAppointment = veterinarianAppointmentRepository.getReferenceById(newAppointmentId);

            return transferVeterinarianAppointmentToOutputDto(newVeterinarianAppointment);
        }
    }

    /**
     * A method (PATCH) will only update an existing object,
     * with the properties mapped in the request body (that are not null).
     * We do NOT update veterinarian appointment and medical data here.
     * @param veterinarianAppointmentId ID of the appointment to be patched
     * @param veterinarianAppointmentPatchDto Data Transfer Objects that carries data between processes in order to reduce the number of methods calls
     * @return an updated veterinarian appointment in output dto format
     * @throws IncorrectInputException throws an exception when subject is less than 3 characters long
     * @throws RecordNotFoundException throws an exception when dog is not found by ID
     */
    public VeterinarianAppointmentOutputDto patchVeterinarianAppointment(
            Long veterinarianAppointmentId,
            VeterinarianAppointmentPatchDto veterinarianAppointmentPatchDto
    ){
        if(veterinarianAppointmentRepository.findById(veterinarianAppointmentId).isPresent()){
            VeterinarianAppointment updatedVeterinarianAppointment = veterinarianAppointmentRepository.getReferenceById(veterinarianAppointmentId);
            if(veterinarianAppointmentPatchDto.getAppointmentDate() != null){
                updatedVeterinarianAppointment.setAppointmentDate(veterinarianAppointmentPatchDto.getAppointmentDate());
            }
            if(veterinarianAppointmentPatchDto.getSubject() != null){
                String subject = veterinarianAppointmentPatchDto.getSubject();

                if(subject.length() <= 2){
                    throw new IncorrectInputException("Subject must at be least 3 characters long.");
                }
                updatedVeterinarianAppointment.setSubject(subject);
            }
            if(
                    veterinarianAppointmentPatchDto.getDomesticatedDog().getId() != null &&
                    veterinarianAppointmentPatchDto.getDomesticatedDog().getId() != 0
            ){
                    DomesticatedDog dog = getCompleteDogById(veterinarianAppointmentPatchDto.getDomesticatedDog().getId());
                    if (dog == null) {
                        throw new RecordNotFoundException("Provided dog does not exist.");
                    }
                    updatedVeterinarianAppointment.setDomesticatedDog(dog);
            }
            veterinarianAppointmentRepository.save(updatedVeterinarianAppointment);
            return transferVeterinarianAppointmentToOutputDto(updatedVeterinarianAppointment);
        }
        else {
            throw new RecordNotFoundException("Appointment is not found.");
        }
    }

    /**
     * A method for deleting a veterinarian appointment from the database by id
     * @param veterinarianAppointmentId ID of the veterinarian appointment for which information is requested
     * @throws RecordNotFoundException throws an exception when no veterinarian appointment is found by ID
     */
    public void deleteAppointment(Long veterinarianAppointmentId) {

        if (veterinarianAppointmentRepository.findById(veterinarianAppointmentId).isPresent()){
            // If an appointment is deleted from the database, this appointment is detached from dogs.
            VeterinarianAppointment veterinarianAppointmentFound = veterinarianAppointmentRepository.getReferenceById(veterinarianAppointmentId);

            if(veterinarianAppointmentFound.getDomesticatedDog() != null){
                veterinarianAppointmentFound.setDomesticatedDog(null);
            }
            veterinarianAppointmentRepository.deleteById(veterinarianAppointmentId);
        } else {
            throw new RecordNotFoundException("No appointment with given ID found.");
        }
    }


    //DTO helper classes
    /**
     * A method to transfer a veterinarian appointment in input dto format to a veterinarian appointment
     * @param veterinarianAppointmentInputDto veterinarian appointment in input dto format
     * @return a veterinarian appointment
     */
    private VeterinarianAppointment transferToVeterinarianAppointment(
            VeterinarianAppointmentInputDto veterinarianAppointmentInputDto
    ){
        VeterinarianAppointment veterinarianAppointment = new VeterinarianAppointment();
        veterinarianAppointment.setDomesticatedDog(veterinarianAppointmentInputDto.getDomesticatedDog());
        veterinarianAppointment.setAppointmentDate(veterinarianAppointmentInputDto.getAppointmentDate());
        veterinarianAppointment.setSubject(veterinarianAppointmentInputDto.getSubject());

        return veterinarianAppointment;
    }
    /**
     * A method to transfer a list of veterinarian appointments to a list of veterinarian appointments in output dto format
     * @param veterinarianAppointments a list of veterinarian appointments to be transformed
     * @return a list of veterinarian appointments in output dto format
     */
    private List<VeterinarianAppointmentOutputDto> transferAppointmentListToOutputDtoList(
            List<VeterinarianAppointment> veterinarianAppointments
    ){
        List<VeterinarianAppointmentOutputDto> appointmentOutputDtoList = new ArrayList<>();

        for(VeterinarianAppointment veterinarianAppointment : veterinarianAppointments) {
            VeterinarianAppointmentOutputDto dto = transferVeterinarianAppointmentToOutputDto(veterinarianAppointment);
            appointmentOutputDtoList.add(dto);
        }
        return appointmentOutputDtoList;
    }

    /**
     * A method to transfer a veterinarian appointment to a veterinarian appointment in output dto format
     * @param veterinarianAppointment the veterinarian appointment to be transferred to output dto
     * @return veterinarian appointment in output dto format
     */
    private VeterinarianAppointmentOutputDto transferVeterinarianAppointmentToOutputDto(
            VeterinarianAppointment veterinarianAppointment
    ){
      VeterinarianAppointmentOutputDto veterinarianAppointmentOutputDto = new VeterinarianAppointmentOutputDto();
      veterinarianAppointmentOutputDto.setId(veterinarianAppointment.getId());
      veterinarianAppointmentOutputDto.setSubject(veterinarianAppointment.getSubject());
      veterinarianAppointmentOutputDto.setAppointmentDate(veterinarianAppointment.getAppointmentDate());
      veterinarianAppointmentOutputDto.setDomesticatedDog(transferToDomesticatedDogOutputDto(veterinarianAppointment.getDomesticatedDog()));
      return veterinarianAppointmentOutputDto;
    }

    /**
     * A method to transform a domesticated dog to a domesticated dog in output dto format
     * @param domesticatedDog ID of the domesticated dog for which information is requested
     * @return a domesticated dog in output dto format
     */
    private DomesticatedDogOutputDto transferToDomesticatedDogOutputDto(DomesticatedDog domesticatedDog) {
        DomesticatedDogOutputDto result = new DomesticatedDogOutputDto();
        result.setId(domesticatedDog.getId());
        result.setName(domesticatedDog.getName());
        result.setSex(domesticatedDog.getSex());
        result.setDateOfBirth(domesticatedDog.getDateOfBirth());
        result.setKindOfHair(domesticatedDog.getKindOfHair());
        result.setBreed(domesticatedDog.getBreed());
        return result;
    }

    /**
     * Look for domesticated dog by id in domesticated dog repository.
     * When no domesticated dog ID is given, the get domesticated dog method returns 0 and an error is thrown.
     * When domesticated dog ID is found, domesticated dog is returned.
     * If there is no domesticated dog found in the repository, null is returned.
     * @param dogId ID of the domesticated dog for which information is requested
     * @return domesticated dog or null if not present.
     * @throws RecordNotFoundException throws an exception when domesticated dog ID is missing
     */
    private DomesticatedDog getCompleteDogById(Long dogId){
        if(dogId == 0){
            throw new RecordNotFoundException("Missing dog ID");
        }
        Optional<DomesticatedDog> domesticatedDogOptional = domesticatedDogRepository.findById(dogId);
        return domesticatedDogOptional.orElse(null);
    }
}
