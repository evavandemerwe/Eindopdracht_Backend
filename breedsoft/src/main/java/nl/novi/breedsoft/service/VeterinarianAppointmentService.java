package nl.novi.breedsoft.service;

import nl.novi.breedsoft.dto.veterinarianAppointmentDtos.VeterinarianAppointmentInputDto;
import nl.novi.breedsoft.dto.veterinarianAppointmentDtos.VeterinarianAppointmentOutputDto;
import nl.novi.breedsoft.exception.RecordNotFoundException;
import nl.novi.breedsoft.model.management.VeterinarianAppointment;
import nl.novi.breedsoft.repository.AppointmentRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VeterinarianAppointmentService {

    private final AppointmentRepository appointmentRepository;

    public VeterinarianAppointmentService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public List<VeterinarianAppointmentOutputDto> getAllAppointments(){
        List<VeterinarianAppointment> allAppointmentsList = appointmentRepository.findAll();
        return transferAppointmentListToDtoList(allAppointmentsList);
    }

    public List <VeterinarianAppointmentOutputDto> getAllAppointmentsByDogId(Long id){
        List<VeterinarianAppointment> allAppointmentsList = appointmentRepository.findAll();
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

    public Long createAppointment(VeterinarianAppointmentInputDto veterinarianAppointmentInputDto){
        VeterinarianAppointment veterinarianAppointment = transferToAppointment(veterinarianAppointmentInputDto);
        appointmentRepository.save(veterinarianAppointment);

        return veterinarianAppointment.getId();
    }
    public void deleteAppointment(Long id) {

        if (appointmentRepository.findById(id).isPresent()){
            // If an appointment is deleted from the database, this appointment is detached from dogs.
            VeterinarianAppointment veterinarianAppointmentFound = appointmentRepository.getReferenceById(id);

            if(veterinarianAppointmentFound.getDomesticatedDog() != null){
                veterinarianAppointmentFound.setDomesticatedDog(null);
            }
            appointmentRepository.deleteById(id);
        } else {
            throw new RecordNotFoundException("No appointment with given ID found.");
        }
    }
    //DTO helper classes

    private VeterinarianAppointment transferToAppointment(VeterinarianAppointmentInputDto veterinarianAppointmentInputDto){
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
}
