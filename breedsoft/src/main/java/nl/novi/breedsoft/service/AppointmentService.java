package nl.novi.breedsoft.service;

import nl.novi.breedsoft.dto.appointmentDtos.AppointmentInputDto;
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

    public List <AppointmentOutputDto> getAllAppointmentsByDogId(Long id){
        List<Appointment> allAppointmentsList = appointmentRepository.findAll();
        List<Appointment> dogAppointmentList = new ArrayList<>();

        for(Appointment appointment : allAppointmentsList){
            Long dogId = appointment.getDomesticatedDog().getId();
            if(dogId.equals(id)){
                dogAppointmentList.add(appointment);
            }
        }
        if(dogAppointmentList.isEmpty()){
            throw new RecordNotFoundException("There are no appointments found for this dog");
        }
        return transferAppointmentListToDtoList(dogAppointmentList);
    }

    public Long createAppointment(AppointmentInputDto appointmentInputDto){
        Appointment appointment = transferToAppointment(appointmentInputDto);
        appointmentRepository.save(appointment);

        return appointment.getId();
    }
    public void deleteAppointment(Long id) {

        if (appointmentRepository.findById(id).isPresent()){
            // If an appointment is deleted from the database, this appointment is detached from dogs.
            Appointment appointmentFound = appointmentRepository.getReferenceById(id);

            if(appointmentFound.getDomesticatedDog() != null){
                appointmentFound.setDomesticatedDog(null);
            }
            appointmentRepository.deleteById(id);
        } else {
            throw new RecordNotFoundException("No appointment with given ID found.");
        }
    }
    //DTO helper classes

    private Appointment transferToAppointment(AppointmentInputDto appointmentInputDto){
        Appointment appointment = new Appointment();

        appointment.setDomesticatedDog(appointmentInputDto.getDomesticatedDog());
        appointment.setAppointmentDate(appointmentInputDto.getAppointmentDate());
        appointment.setSubject(appointmentInputDto.getSubject());

        return appointment;
    }
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
        AppointmentOutputDto.setAppointmentDate(appointment.getAppointmentDate());
        AppointmentOutputDto.setDomesticatedDog(appointment.getDomesticatedDog());

        return AppointmentOutputDto;
    }
}
