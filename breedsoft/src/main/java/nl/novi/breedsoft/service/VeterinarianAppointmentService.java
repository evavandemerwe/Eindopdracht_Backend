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
     * A method that returns a list of all appointments from the database
     * @return a list of all appointments in output dto format
     */
    public List<VeterinarianAppointmentOutputDto> getAllAppointments(){
        List<VeterinarianAppointment> allAppointmentsList = veterinarianAppointmentRepository.findAll();
        return transferAppointmentListToDtoList(allAppointmentsList);
    }

    /**
     * A method that returns a list of all appointments by dog id
     * @param id Dog id of the dog to get appointments for
     * @return a list of appointments for given dog in output dto format
     */
    public List <VeterinarianAppointmentOutputDto> getAllAppointmentsByDogId(Long id){
        List<VeterinarianAppointment> allAppointmentsList = veterinarianAppointmentRepository.findAll();
        List<VeterinarianAppointment> dogVeterinarianAppointmentList = new ArrayList<>();

        for(VeterinarianAppointment veterinarianAppointment : allAppointmentsList){
            Long dogId = veterinarianAppointment.getDomesticatedDog().getId();
            if(dogId.equals(id)){
                dogVeterinarianAppointmentList.add(veterinarianAppointment);
            }
        }
        if(dogVeterinarianAppointmentList.isEmpty()){
            throw new RecordNotFoundException("There are no appointments found for this dog");
        }
        return transferAppointmentListToDtoList(dogVeterinarianAppointmentList);
    }

    //Create a new appointment

    /**
     * A method to create a new appointment
     * @param veterinarianAppointmentInputDto Input parameters for appointment to be created
     * @return ID of the created appointment
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

    //PUT sends an enclosed entity of a resource to the server.
    //If the entity already exists, the server overrides the existing object.
    //Otherwise, the server creates a new entity

    /**
     * A method to update an existing appointment
     * @param id of the appointment to be updated
     * @param veterinarianAppointmentInputDto Updated appointment
     * @return Updated appointment
     */
    public Object updateVeterinarianAppointment(Long id, VeterinarianAppointmentInputDto veterinarianAppointmentInputDto){
        if(veterinarianAppointmentRepository.findById(id).isPresent()){
            VeterinarianAppointment veterinarianAppointment = veterinarianAppointmentRepository.findById(id).get();
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
     * A method to patch an existing appointment
     * @param id of the appointment to be patched
     * @param veterinarianAppointmentPatchDto Patched appointment
     * @return Patched appointment
     */
    public VeterinarianAppointmentOutputDto patchVeterinarianAppointment(Long id, VeterinarianAppointmentPatchDto veterinarianAppointmentPatchDto){
        if(veterinarianAppointmentRepository.findById(id).isPresent()){
            VeterinarianAppointment updatedVeterinarianAppointment = veterinarianAppointmentRepository.getReferenceById(id);
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
     * A method to delete an existing appointment
     * @param id of the appointment to be deleted
     */
    public void deleteAppointment(Long id) {

        if (veterinarianAppointmentRepository.findById(id).isPresent()){
            // If an appointment is deleted from the database, this appointment is detached from dogs.
            VeterinarianAppointment veterinarianAppointmentFound = veterinarianAppointmentRepository.getReferenceById(id);

            if(veterinarianAppointmentFound.getDomesticatedDog() != null){
                veterinarianAppointmentFound.setDomesticatedDog(null);
            }
            veterinarianAppointmentRepository.deleteById(id);
        } else {
            throw new RecordNotFoundException("No appointment with given ID found.");
        }
    }

    /**
     * A method to transfer a veterinarian appointment DTO to a veterinarian appointment
     * @param veterinarianAppointmentInputDto DTO to be transferred
     * @return VeterinarianAppointment of the given input DTO
     */
    private VeterinarianAppointment transferToVeterinarianAppointment(VeterinarianAppointmentInputDto veterinarianAppointmentInputDto){
        VeterinarianAppointment veterinarianAppointment = new VeterinarianAppointment();
        veterinarianAppointment.setDomesticatedDog(veterinarianAppointmentInputDto.getDomesticatedDog());
        veterinarianAppointment.setAppointmentDate(veterinarianAppointmentInputDto.getAppointmentDate());
        veterinarianAppointment.setSubject(veterinarianAppointmentInputDto.getSubject());

        return veterinarianAppointment;
    }
    /**
     * A method to transfer a list of veterinarian appointment DTOs to a list of veterinarian appointments
     * @param veterinarianAppointments List of DTOs to be transferred
     * @return List of VeterinarianAppointments of the given input DTOs
     */
    private List<VeterinarianAppointmentOutputDto>transferAppointmentListToDtoList(List<VeterinarianAppointment> veterinarianAppointments){

        List<VeterinarianAppointmentOutputDto> appointmentOutputDtoList = new ArrayList<>();

        for(VeterinarianAppointment veterinarianAppointment : veterinarianAppointments) {
            VeterinarianAppointmentOutputDto dto = transferVeterinarianAppointmentToOutputDto(veterinarianAppointment);
            appointmentOutputDtoList.add(dto);
        }
        return appointmentOutputDtoList;
    }

    /**
     * A method to transfer a veterinarian appointment to a veterinarian appointment DTO
     * @param veterinarianAppointment to be transferred tp DTO
     * @return DTO of the given veterinarian appointment
     */

    private VeterinarianAppointmentOutputDto transferVeterinarianAppointmentToOutputDto(VeterinarianAppointment veterinarianAppointment){
      VeterinarianAppointmentOutputDto veterinarianAppointmentOutputDto = new VeterinarianAppointmentOutputDto();
      veterinarianAppointmentOutputDto.setId(veterinarianAppointment.getId());
      veterinarianAppointmentOutputDto.setSubject(veterinarianAppointment.getSubject());
      veterinarianAppointmentOutputDto.setAppointmentDate(veterinarianAppointment.getAppointmentDate());
      veterinarianAppointmentOutputDto.setDomesticatedDog(transferToDomesticatedDogOutputDto(veterinarianAppointment.getDomesticatedDog()));
      return veterinarianAppointmentOutputDto;
    }

    private DomesticatedDogOutputDto transferToDomesticatedDogOutputDto(DomesticatedDog dog) {
        DomesticatedDogOutputDto result = new DomesticatedDogOutputDto();
        result.setId(dog.getId());
        result.setName(dog.getName());
        result.setSex(dog.getSex());
        result.setDateOfBirth(dog.getDateOfBirth());
        result.setKindOfHair(dog.getKindOfHair());
        result.setBreed(dog.getBreed());
        return result;
    }

    //Look for dog by ID in dogrespository.
    //When nu dog ID is given, the get dog method returns 0 and an error is thrown.
    //When dog ID is found, dog is returned. If there is no dog found in the repository, null is returned.
    private DomesticatedDog getCompleteDogById(Long dogId){
        if(dogId == 0){
            throw new RecordNotFoundException("Missing dog ID");
        }
        Optional<DomesticatedDog> domesticatedDogOptional = domesticatedDogRepository.findById(dogId);
        return domesticatedDogOptional.orElse(null);
    }
}
