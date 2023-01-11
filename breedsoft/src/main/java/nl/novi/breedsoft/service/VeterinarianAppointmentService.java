package nl.novi.breedsoft.service;

import nl.novi.breedsoft.dto.veterinarianAppointmentDtos.VeterinarianAppointmentInputDto;
import nl.novi.breedsoft.dto.veterinarianAppointmentDtos.VeterinarianAppointmentOutputDto;
import nl.novi.breedsoft.dto.veterinarianAppointmentDtos.VeterinarianAppointmentPatchDto;
import nl.novi.breedsoft.exception.InputStringToShortException;
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

    public List<VeterinarianAppointmentOutputDto> getAllAppointments(){
        List<VeterinarianAppointment> allAppointmentsList = veterinarianAppointmentRepository.findAll();
        return transferAppointmentListToDtoList(allAppointmentsList);
    }

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
    public Long createVeterinarianAppointment(VeterinarianAppointmentInputDto veterinarianAppointmentInputDto){
        //Check if a dog owner is given
        if(veterinarianAppointmentInputDto.getDomesticatedDog() != null) {
            DomesticatedDog dog = getCompleteDogById(veterinarianAppointmentInputDto.getDomesticatedDog().getId());
            if (dog == null) {
                throw new RecordNotFoundException("Provided dog does not exist");
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
            return transferToOutputDto(veterinarianAppointment);
        }else{
            Long newAppointmentId = createVeterinarianAppointment(veterinarianAppointmentInputDto);
            VeterinarianAppointment newVeterinarianAppointment = veterinarianAppointmentRepository.getReferenceById(newAppointmentId);

            return transferToOutputDto(newVeterinarianAppointment);
        }
    }

    //PATCH will only update an existing object,
    //with the properties mapped in the request body (that are not null).
    public VeterinarianAppointmentOutputDto patchVeterinarianAppointment(Long id, VeterinarianAppointmentPatchDto veterinarianAppointmentPatchDto){
        Optional<VeterinarianAppointment> veterinarianAppointmentOptional = veterinarianAppointmentRepository.findById(id);

        if(veterinarianAppointmentOptional.isPresent()){
            VeterinarianAppointment updatedVeterinarianAppointment = veterinarianAppointmentRepository.getReferenceById(id);
            if(veterinarianAppointmentPatchDto.getAppointmentDate() != null){
                updatedVeterinarianAppointment.setAppointmentDate(veterinarianAppointmentPatchDto.getAppointmentDate());
            }
            if(veterinarianAppointmentPatchDto.getSubject() != null){
                String subject = veterinarianAppointmentPatchDto.getSubject();

                if(subject.length() <= 2){
                    throw new InputStringToShortException("Subject must at be least 3 characters long.");
                }
                updatedVeterinarianAppointment.setSubject(subject);
            }
            if(veterinarianAppointmentPatchDto.getDomesticatedDog() != null){
                DomesticatedDog dog = getCompleteDogById(veterinarianAppointmentPatchDto.getDomesticatedDog().getId());
                if(dog == null){
                    throw new RecordNotFoundException("Provided dog owner does not exist");
                }
                updatedVeterinarianAppointment.setDomesticatedDog(dog);
            }
            veterinarianAppointmentRepository.save(updatedVeterinarianAppointment);
            return transferToOutputDto(updatedVeterinarianAppointment);
        }
        else {
            throw new RecordNotFoundException("Appointment is not found.");
        }
    }

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

    //DTO helper classes
    private VeterinarianAppointment transferToVeterinarianAppointment(VeterinarianAppointmentInputDto veterinarianAppointmentInputDto){
        VeterinarianAppointment veterinarianAppointment = new VeterinarianAppointment();
        veterinarianAppointment.setDomesticatedDog(veterinarianAppointmentInputDto.getDomesticatedDog());
        veterinarianAppointment.setAppointmentDate(veterinarianAppointmentInputDto.getAppointmentDate());
        veterinarianAppointment.setSubject(veterinarianAppointmentInputDto.getSubject());

        return veterinarianAppointment;
    }
    private List<VeterinarianAppointmentOutputDto>transferAppointmentListToDtoList(List<VeterinarianAppointment> veterinarianAppointments){

        List<VeterinarianAppointmentOutputDto> appointmentDtoList = new ArrayList<>();

        for(VeterinarianAppointment veterinarianAppointment : veterinarianAppointments) {
            VeterinarianAppointmentOutputDto dto = transferToOutputDto(veterinarianAppointment);
            appointmentDtoList.add(dto);
        }
        return appointmentDtoList;
    }

    public VeterinarianAppointmentOutputDto transferToOutputDto(VeterinarianAppointment veterinarianAppointment){

        VeterinarianAppointmentOutputDto VeterinarianAppointmentOutputDto = new VeterinarianAppointmentOutputDto();
        VeterinarianAppointmentOutputDto.setId(veterinarianAppointment.getId());
        VeterinarianAppointmentOutputDto.setSubject(veterinarianAppointment.getSubject());
        VeterinarianAppointmentOutputDto.setAppointmentDate(veterinarianAppointment.getAppointmentDate());
        VeterinarianAppointmentOutputDto.setDomesticatedDog(veterinarianAppointment.getDomesticatedDog());

        return VeterinarianAppointmentOutputDto;
    }

    private DomesticatedDog getCompleteDogById(Long dogId){
        if(dogId == 0){
            throw new RecordNotFoundException("Missing Person ID");
        }
        Optional<DomesticatedDog> domesticatedDogOptional = domesticatedDogRepository.findById(dogId);
        return domesticatedDogOptional.orElse(null);
    }
}
