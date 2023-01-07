package nl.novi.breedsoft.service;

import nl.novi.breedsoft.dto.appointmentDtos.AppointmentOutputDto;
import nl.novi.breedsoft.exception.RecordNotFoundException;
import nl.novi.breedsoft.model.management.Appointment;
import nl.novi.breedsoft.repository.AppointmentRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;

    public AppointmentService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public List<AppointmentOutputDto> getAllAppointments(){
        List<Appointment> allAppointmentsList = appointmentRepository.findAll();
        return transferAppointmentListToDtoList(allAppointmentsList);
    }

    public List <AppointmentOutputDto> getAllAppointmentsForDogId(Long id){
        List<Appointment> allAppointmentsList = appointmentRepository.findAll();
        List<Appointment> dogAppointmentList = new ArrayList<>();

        for(Appointment appointment : allAppointmentsList){
            Long dogId = appointment.getDomesticatedDog().getId();
            if(dogId.equals(id)){
                dogAppointmentList.add(appointment);
            };
        }
        if(dogAppointmentList.isEmpty()){
            throw new RecordNotFoundException("There are no appointments found for this dog");
        }
        return transferAppointmentListToDtoList(dogAppointmentList);
    }
    public void deleteAppointment(Long id) {

        if (appointmentRepository.findById(id).isPresent()){
            // If an appointment is deleted from the database, this appointment is detached from persons and dogs.
            Appointment appointmentFound = appointmentRepository.getReferenceById(id);

            if(appointmentFound.getDomesticatedDog() != null){
                appointmentFound.setDomesticatedDog(null);
            }
            appointmentRepository.deleteById(id);
        } else {
            throw new RecordNotFoundException("No person with given ID found.");
        }
    }
    //DTO helper classes
    private List<AppointmentOutputDto>transferAppointmentListToDtoList(List<Appointment> appointments){

        List<AppointmentOutputDto> appointmentDtoList = new ArrayList<>();

        for(Appointment appointment : appointments) {
            AppointmentOutputDto dto = transferToOutputDto(appointment);
            appointmentDtoList.add(dto);
        }
        return appointmentDtoList;
    }

    public AppointmentOutputDto transferToOutputDto(Appointment appointment){

        AppointmentOutputDto AppointmentOutputDto = new AppointmentOutputDto();

        AppointmentOutputDto.setId(appointment.getId());
        AppointmentOutputDto.setSubject(appointment.getSubject());
        AppointmentOutputDto.setAppointmentDateTime(appointment.getAppointmentDateTime());
        AppointmentOutputDto.setDomesticatedDog(appointment.getDomesticatedDog());

        return AppointmentOutputDto;
    }
}
