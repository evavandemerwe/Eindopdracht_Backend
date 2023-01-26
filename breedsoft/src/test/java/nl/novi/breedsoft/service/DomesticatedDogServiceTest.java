package nl.novi.breedsoft.service;

import nl.novi.breedsoft.dto.domesticatedDogDtos.DomesticatedDogInputDto;
import nl.novi.breedsoft.dto.domesticatedDogDtos.DomesticatedDogOutputDto;
import nl.novi.breedsoft.dto.domesticatedDogDtos.DomesticatedDogPatchDto;
import nl.novi.breedsoft.exception.IncorrectInputException;
import nl.novi.breedsoft.exception.RecordNotFoundException;
import nl.novi.breedsoft.model.animal.enumerations.Sex;
import nl.novi.breedsoft.model.management.DomesticatedDog;
import nl.novi.breedsoft.model.management.MedicalData;
import nl.novi.breedsoft.model.management.Person;
import nl.novi.breedsoft.model.management.VeterinarianAppointment;
import nl.novi.breedsoft.model.management.enumerations.Breed;
import nl.novi.breedsoft.model.management.enumerations.BreedGroup;
import nl.novi.breedsoft.model.management.enumerations.Status;
import nl.novi.breedsoft.repository.DomesticatedDogRepository;
import nl.novi.breedsoft.repository.MedicalDataRepository;
import nl.novi.breedsoft.repository.VeterinarianAppointmentRepository;
import nl.novi.breedsoft.utility.RepositoryUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DomesticatedDogServiceTest {

    @Mock
    DomesticatedDogRepository domesticatedDogRepository;
    @Mock
    VeterinarianAppointmentRepository veterinarianAppointmentRepository;
    @Mock
    MedicalDataRepository medicalDataRepository;
    @Mock
    RepositoryUtility repositoryUtility;

    DomesticatedDogService domesticatedDogService;

    @BeforeEach
    void setUp() {
        this.domesticatedDogService = new DomesticatedDogService(
            domesticatedDogRepository,
            veterinarianAppointmentRepository,
            medicalDataRepository,
            repositoryUtility
        );
    }

    @Test
    void getAllDomesticatedDogs() {
        // Arrange
        String expectedName = "expectedName";
        DomesticatedDog domesticatedDog = new DomesticatedDog();
        domesticatedDog.setName(expectedName);
        when(domesticatedDogRepository.findAll()).thenReturn(
                new ArrayList<>(List.of(domesticatedDog))
        );
        // Act
        List<DomesticatedDogOutputDto> result = domesticatedDogService.getAllDomesticatedDogs();
        // Assert
        assertEquals(1, result.size());
        assertEquals(expectedName, result.get(0).getName());
    }

    @Test
    void getAllDomesticatedDogsWithEmptyRepositoryResponse() {
        // Arrange
        when(domesticatedDogRepository.findAll()).thenReturn(new ArrayList<>());
        // Act
        List<DomesticatedDogOutputDto> result = domesticatedDogService.getAllDomesticatedDogs();
        // Assert
        assertEquals(0, result.size());
    }

    @Test
    void getAllDomesticatedDogsWithParentID() {
        // Arrange
        Long expectedParentID = 42L;
        DomesticatedDog domesticatedDog = new DomesticatedDog();
        domesticatedDog.setParentId(expectedParentID);
        when(domesticatedDogRepository.findAll()).thenReturn(
                new ArrayList<>(List.of(domesticatedDog))
        );
        when(domesticatedDogRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(new DomesticatedDog()));
        // Act
        List<DomesticatedDogOutputDto> result = domesticatedDogService.getAllDomesticatedDogs();
        // Assert
        assertEquals(1, result.size());
        assertEquals(expectedParentID, result.get(0).getParentId());
    }

    @Test
    void getAllDomesticatedDogsWithEmptyParentByParentID() {
        // Arrange
        Long expectedParentID = 42L;
        DomesticatedDog domesticatedDog = new DomesticatedDog();
        domesticatedDog.setParentId(expectedParentID);
        when(domesticatedDogRepository.findAll()).thenReturn(
                new ArrayList<>(List.of(domesticatedDog))
        );
        when(domesticatedDogRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());
        // Act and Assert
        assertThrows(
                RecordNotFoundException.class,
                () -> domesticatedDogService.getAllDomesticatedDogs(),
                "Expected Record not found exception to be thrown"
        );
    }

    @Test
    void getAllDomesticatedDogsWithLitter() {
        // Arrange
        DomesticatedDog domesticatedDog = new DomesticatedDog();
        String expectedLitterName = "expectedLitterName";
        DomesticatedDog litterDog = new DomesticatedDog();
        litterDog.setName(expectedLitterName);
        domesticatedDog.setLitter(new ArrayList<>(List.of(litterDog)));
        when(domesticatedDogRepository.findAll()).thenReturn(
                new ArrayList<>(List.of(domesticatedDog))
        );
        // Act
        List<DomesticatedDogOutputDto> result = domesticatedDogService.getAllDomesticatedDogs();
        // Assert
        assertEquals(1, result.size());
        List<DomesticatedDog> resultLitter = result.get(0).getLitters();
        assertEquals(1, resultLitter.size());
        assertEquals(expectedLitterName, resultLitter.get(0).getName());
    }

    @Test
    void getAllDomesticatedDogsWithVeterinarianAppointment() {
        // Arrange
        DomesticatedDog domesticatedDog = new DomesticatedDog();
        VeterinarianAppointment veterinarianAppointment = new VeterinarianAppointment();
        String expectedVeterinarianAppointmentSubject = "expectedVeterinarianAppointmentSubject";
        veterinarianAppointment.setSubject(expectedVeterinarianAppointmentSubject);
        domesticatedDog.setVeterinarianAppointments(new ArrayList<>(List.of(veterinarianAppointment)));
        when(domesticatedDogRepository.findAll()).thenReturn(
                new ArrayList<>(List.of(domesticatedDog))
        );
        // Act
        List<DomesticatedDogOutputDto> result = domesticatedDogService.getAllDomesticatedDogs();
        // Assert
        assertEquals(1, result.size());
        List<VeterinarianAppointment> resultVeterinarianAppointments = result.get(0).getVeterinarianAppointments();
        assertEquals(1, resultVeterinarianAppointments.size());
        assertEquals(expectedVeterinarianAppointmentSubject, resultVeterinarianAppointments.get(0).getSubject());
    }

    @Test
    void getAllDomesticatedDogsWithMedicalData() {
        // Arrange
        DomesticatedDog domesticatedDog = new DomesticatedDog();
        MedicalData medicalData = new MedicalData();
        String expectedDiagnose = "expectedDiagnose";
        medicalData.setDiagnose(expectedDiagnose);
        domesticatedDog.setMedicalData(new ArrayList<>(List.of(medicalData)));
        when(domesticatedDogRepository.findAll()).thenReturn(
                new ArrayList<>(List.of(domesticatedDog))
        );
        // Act
        List<DomesticatedDogOutputDto> result = domesticatedDogService.getAllDomesticatedDogs();
        // Assert
        assertEquals(1, result.size());
        List<MedicalData> resultMedicalData = result.get(0).getMedicalData();
        assertEquals(1, resultMedicalData.size());
        assertEquals(expectedDiagnose, resultMedicalData.get(0).getDiagnose());
    }


    @Test
    void getDomesticatedDogById() {
        // Arrange
        Long expectedID = 42L;
        DomesticatedDog domesticatedDog = new DomesticatedDog();
        domesticatedDog.setId(expectedID);
        when(domesticatedDogRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(domesticatedDog));
        // Act
        DomesticatedDogOutputDto result = domesticatedDogService.getDomesticatedDogById(42L);
        // Assert
        assertEquals(expectedID, result.getId());
    }

    @Test
    void getDomesticatedDogByIdWithEmptyRepositoryResponse() {
        // Arrange
        when(domesticatedDogRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        // Act and Assert
        assertThrows(
                RecordNotFoundException.class,
                () ->  domesticatedDogService.getDomesticatedDogById(42L),
                "Expected Record not found exception to be thrown"
        );
    }

    @Test
    void getDomesticatedDogByName() {
        // Arrange
        DomesticatedDog domesticatedDog = new DomesticatedDog();
        String expectedName = "expectedName";
        domesticatedDog.setName(expectedName);
        when(domesticatedDogRepository.findByNameContaining(Mockito.anyString())).thenReturn(new ArrayList<>(List.of(domesticatedDog)));
        // Act
        List<DomesticatedDogOutputDto> result = domesticatedDogService.getDomesticatedDogByName(expectedName);
        // Assert
        assertEquals(1, result.size());
        assertEquals(expectedName, result.get(0).getName());
    }

    @Test
    void getDomesticatedDogByNameWithEmptyRepositoryResponse() {
        // Arrange
        when(domesticatedDogRepository.findByNameContaining(Mockito.anyString())).thenReturn(new ArrayList<>());
        // Act and Assert
        assertThrows(
                RecordNotFoundException.class,
                () ->  domesticatedDogService.getDomesticatedDogByName("name"),
                "Expected Record not found exception to be thrown"
        );
    }

    @Test
    void getAllChildren() {
        // Arrange
        when(domesticatedDogRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(new DomesticatedDog()));

        DomesticatedDog domesticatedDog = new DomesticatedDog();
        Long expectedParentID = 42L;
        domesticatedDog.setParentId(expectedParentID);
        when(domesticatedDogRepository.findAll()).thenReturn(new ArrayList<>(List.of(domesticatedDog)));
        // Act
        List<DomesticatedDogOutputDto> result = domesticatedDogService.getAllChildren(expectedParentID);
        // Assert
        assertEquals(1, result.size());
        assertEquals(expectedParentID, result.get(0).getParentId());
    }

    @Test
    void getAllChildrenWithNotFoundDomesticatedDogId() {
        // Arrange
        when(domesticatedDogRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        // Act and Assert
        assertThrows(
                RecordNotFoundException.class,
                () -> domesticatedDogService.getAllChildren(42L),
                "Expected Record not found exception to be thrown"
        );
    }

    @Test
    void getAllChildrenWithNotMatchingParentID() {
        // Arrange
        when(domesticatedDogRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(new DomesticatedDog()));

        DomesticatedDog domesticatedDog = new DomesticatedDog();
        domesticatedDog.setParentId(42L);
        when(domesticatedDogRepository.findAll()).thenReturn(new ArrayList<>(List.of(domesticatedDog)));
        // Act and Assert
        assertThrows(
            RecordNotFoundException.class,
            () -> domesticatedDogService.getAllChildren(3L),
            "Expected Record not found exception to be thrown"
        );
    }

    @Test
    void getAvailableDomesticatedDogs() {
        // Arrange
        Status expectedStatus = Status.availablePup;
        DomesticatedDog domesticatedDog = new DomesticatedDog();
        domesticatedDog.setDogStatus(expectedStatus);
        when(domesticatedDogRepository.findAll()).thenReturn(new ArrayList<>(List.of(domesticatedDog)));
        // Act
        List<DomesticatedDogOutputDto> result = domesticatedDogService.getAvailableDomesticatedDogs();
        // Assert
        assertEquals(1, result.size());
        assertEquals(expectedStatus, result.get(0).getDogStatus());
    }

    @Test
    void getAvailableDomesticatedDogsWithNotAvailableDogs() {
        // Arrange
        Status expectedStatus = Status.ownedDog;
        DomesticatedDog domesticatedDog = new DomesticatedDog();
        domesticatedDog.setDogStatus(expectedStatus);
        when(domesticatedDogRepository.findAll()).thenReturn(new ArrayList<>(List.of(domesticatedDog)));
        // Act and Assert
        assertThrows(
            RecordNotFoundException.class,
            () -> domesticatedDogService.getAvailableDomesticatedDogs(),
            "Expected Record not found exception to be thrown"
        );
    }

    @Test
    void getDomesticatedBreedDogs() {
        // Arrange
        DomesticatedDog domesticatedDog = new DomesticatedDog();
        domesticatedDog.setDogStatus(Status.breedDog);
        when(domesticatedDogRepository.findAll()).thenReturn(new ArrayList<>(List.of(domesticatedDog)));
        // Act
        List<DomesticatedDogOutputDto> result = domesticatedDogService.getDomesticatedBreedDogs();
        // Assert
        assertEquals(1, result.size());
        assertEquals(Status.breedDog, result.get(0).getDogStatus());
    }

    @Test
    void getDomesticatedBreedDogsWithNoBreedDogStatus() {
        // Arrange
        DomesticatedDog domesticatedDog = new DomesticatedDog();
        domesticatedDog.setDogStatus(Status.ownedDog);
        when(domesticatedDogRepository.findAll()).thenReturn(new ArrayList<>(List.of(domesticatedDog)));
        // Act and Assert
        assertThrows(
                RecordNotFoundException.class,
                () -> domesticatedDogService.getDomesticatedBreedDogs(),
                "Expected Record not found exception to be thrown"
        );
    }

    @Test
    void getParentDog() {
        // Arrange
        when(domesticatedDogRepository.existsById(Mockito.anyLong())).thenReturn(true);

        Long expectedParentID = 42L;
        Long id = 1L;
        DomesticatedDog domesticatedDog = new DomesticatedDog();
        domesticatedDog.setId(id);
        domesticatedDog.setParentId(expectedParentID);
        when(domesticatedDogRepository.getReferenceById(id)).thenReturn(domesticatedDog);

        DomesticatedDog parentDomesticatedDog = new DomesticatedDog();
        parentDomesticatedDog.setId(expectedParentID);
        when(domesticatedDogRepository.getReferenceById(expectedParentID)).thenReturn(parentDomesticatedDog);
        // Act
        DomesticatedDogOutputDto result = domesticatedDogService.getParentDog(id);
        // Assert
        assertEquals(expectedParentID, result.getId());
    }

    @Test
    void getParentDogWithNoneExistingDogId() {
        // Arrange
        when(domesticatedDogRepository.existsById(Mockito.anyLong())).thenReturn(false);
        // Act and Assert
        assertThrows(
            RecordNotFoundException.class,
            () -> domesticatedDogService.getParentDog(1L),
    "Expected Record not found exception to be thrown"
        );
    }

    @Test
    void getParentDogWithNoneExistingParent() {
        // Arrange
        when(domesticatedDogRepository.existsById(Mockito.anyLong())).thenReturn(true);
        when(domesticatedDogRepository.getReferenceById(Mockito.anyLong())).thenReturn(new DomesticatedDog());
        // Act and Assert
        assertThrows(
                RecordNotFoundException.class,
                () -> domesticatedDogService.getParentDog(1L),
        "Expected Record not found exception to be thrown"
        );
    }

    @Test
    void createDomesticatedDog() {
        // Arrange
        Person person = new Person();
        person.setId(1L);
        when(repositoryUtility.getCompletePersonById(Mockito.anyLong())).thenReturn(person);

        Long expectedID = 42L;
        DomesticatedDog domesticatedDog = new DomesticatedDog();
        domesticatedDog.setId(expectedID);
        when(domesticatedDogRepository.save(Mockito.any(DomesticatedDog.class))).thenReturn(domesticatedDog);

        DomesticatedDogInputDto domesticatedDogInputDto = new DomesticatedDogInputDto();
        domesticatedDogInputDto.setPerson(person);
        // Act
        Long result = domesticatedDogService.createDomesticatedDog(domesticatedDogInputDto);
        // Assert
        assertEquals(expectedID, result);
    }

    @Test
    void createDomesticatedDogWithNoneExistingPerson() {
        // Arrange
        when(repositoryUtility.getCompletePersonById(Mockito.anyLong())).thenReturn(null);

        DomesticatedDogInputDto domesticatedDogInputDto = new DomesticatedDogInputDto();
        domesticatedDogInputDto.setPerson(new Person());
        // Act and Assert
        assertThrows(
                RecordNotFoundException.class,
                () -> domesticatedDogService.createDomesticatedDog(domesticatedDogInputDto),
                "Expected Record not found exception to be thrown"
        );
    }

    @Test
    void createLitterList() {
        // Arrange
        when(domesticatedDogRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(new DomesticatedDog()));
        Long expectedParentID = 42L;
        // Act
        List<DomesticatedDogOutputDto> result = domesticatedDogService.createLitterList(List.of(new DomesticatedDogInputDto()),expectedParentID);
        // Assert
        assertEquals(1, result.size());
        assertEquals(expectedParentID, result.get(0).getParentId());
    }

    @Test
    void createLitterListWithNoneExistingID() {
        // Arrange
        when(domesticatedDogRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        // Act and Assert
        assertThrows(
                RecordNotFoundException.class,
                () -> domesticatedDogService.createLitterList(List.of(new DomesticatedDogInputDto()),42L),
                "Expected Record not found exception to be thrown"
        );
    }

    @Test
    void createLitterListWithEmptyLitterListInput() {
        // Arrange
        when(domesticatedDogRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(new DomesticatedDog()));

        // Act and Assert
        assertThrows(
                IncorrectInputException.class,
                () -> domesticatedDogService.createLitterList(new ArrayList<>(),42L),
                "Expected incorrect input exception to be thrown"
        );
    }

    @Test
    void updateDomesticatedDog() {
        // Arrange
        Long expectedID = 42L;
        DomesticatedDog domesticatedDog = new DomesticatedDog();
        domesticatedDog.setId(expectedID);
        when(domesticatedDogRepository.findById(expectedID)).thenReturn(Optional.of(domesticatedDog));
        // Act
        DomesticatedDogOutputDto result = domesticatedDogService.updateDomesticatedDog(expectedID, new DomesticatedDogInputDto());
        // Assert
        assertEquals(expectedID, result.getId());
    }

    @Test
    void updateDomesticatedDogWithPerson() {
        // Arrange
        Long expectedID = 42L;
        DomesticatedDog domesticatedDog = new DomesticatedDog();
        domesticatedDog.setId(expectedID);
        when(domesticatedDogRepository.findById(expectedID)).thenReturn(Optional.of(domesticatedDog));

        Long expectedPersonID = 42L;
        Person person = new Person();
        person.setId(expectedPersonID);
        when(repositoryUtility.getCompletePersonById(expectedPersonID)).thenReturn(person);

        DomesticatedDogInputDto domesticatedDogInputDto = new DomesticatedDogInputDto();
        domesticatedDogInputDto.setPerson(person);
        // Act
        DomesticatedDogOutputDto result = domesticatedDogService.updateDomesticatedDog(expectedID, domesticatedDogInputDto);
        // Assert
        assertEquals(expectedID, result.getId());
        assertEquals(expectedPersonID, result.getPerson().getId());
    }

    @Test
    void updateDomesticatedDogWithNoneExistingPerson() {
        // Arrange
        Long expectedID = 42L;
        DomesticatedDog domesticatedDog = new DomesticatedDog();
        domesticatedDog.setId(expectedID);
        when(domesticatedDogRepository.findById(expectedID)).thenReturn(Optional.of(domesticatedDog));
        when(repositoryUtility.getCompletePersonById(Mockito.anyLong())).thenReturn(null);

        Person person = new Person();
        person.setId(42L);
        DomesticatedDogInputDto domesticatedDogInputDto = new DomesticatedDogInputDto();
        domesticatedDogInputDto.setPerson(person);
        // Act and Assert
        assertThrows(
                RecordNotFoundException.class,
                () -> domesticatedDogService.updateDomesticatedDog(expectedID, domesticatedDogInputDto),
                "Expected record not found exception to be thrown"
        );
    }

    @Test
    void updateDomesticatedDogWithNoneExistingDog() {
        // Arrange
        String expectedName = "expectedName";
        when(domesticatedDogRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        DomesticatedDogInputDto domesticatedDogInputDto = new DomesticatedDogInputDto();
        domesticatedDogInputDto.setName(expectedName);

        DomesticatedDog domesticatedDog = new DomesticatedDog();
        domesticatedDog.setName(expectedName);
        Long expectedID = 42L;
        domesticatedDog.setId(expectedID);
        when(domesticatedDogRepository.save(Mockito.any(DomesticatedDog.class))).thenReturn(domesticatedDog);
        when(domesticatedDogRepository.getReferenceById(expectedID)).thenReturn(domesticatedDog);
        // Act
        DomesticatedDogOutputDto result = domesticatedDogService.updateDomesticatedDog(42L, new DomesticatedDogInputDto());
        // Assert
        assertEquals(expectedName, result.getName());
    }

    @Test
    void patchDomesticatedDog() {
        // Arrange
        DomesticatedDogPatchDto domesticatedDogPatchDto = new DomesticatedDogPatchDto();
        String expectedName = "expectedName";
        domesticatedDogPatchDto.setName(expectedName);
        String expectedHairColor = "expectedHairColor";
        domesticatedDogPatchDto.setHairColor(expectedHairColor);
        String expectedFood = "expectedFood";
        domesticatedDogPatchDto.setFood(expectedFood);
        String expectedSex = Sex.female.name();
        domesticatedDogPatchDto.setSex(expectedSex);
        Double expectedWeight = 1.0D;
        domesticatedDogPatchDto.setWeightInGrams(expectedWeight);
        String expectedKindOfHair = "expectedKindOfHair";
        domesticatedDogPatchDto.setKindOfHair(expectedKindOfHair);
        LocalDate expectedDateOfBirth = LocalDate.now().minusYears(1);
        domesticatedDogPatchDto.setDateOfBirth(expectedDateOfBirth);
        LocalDate expectedDateOfDeath = LocalDate.now().minusDays(7);
        domesticatedDogPatchDto.setDateOfDeath(expectedDateOfDeath);
        String expectedBreed = Breed.Dachschund.name();
        domesticatedDogPatchDto.setBreed(expectedBreed);
        String expectedBreedGroup = BreedGroup.Toy.name();
        domesticatedDogPatchDto.setBreedGroup(expectedBreedGroup);
        String expectedChipNumber = "expectedChipNumber";
        domesticatedDogPatchDto.setChipNumber(expectedChipNumber);
        Person person = new Person();
        Long expectedPersonID = 42L;
        person.setId(expectedPersonID);
        when(repositoryUtility.getCompletePersonById(Mockito.anyLong())).thenReturn(person);
        domesticatedDogPatchDto.setPerson(person);
        String expectedStatus = Status.deceased.name();
        domesticatedDogPatchDto.setDogStatus(expectedStatus);
        DomesticatedDog domesticatedDog = new DomesticatedDog();
        when(domesticatedDogRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(domesticatedDog));
        // Act
        domesticatedDogService.patchDomesticatedDog(3L, domesticatedDogPatchDto);
        // Assert
        assertEquals(expectedName, domesticatedDog.getName());
        assertEquals(expectedHairColor, domesticatedDog.getHairColor());
        assertEquals(expectedFood, domesticatedDog.getFood());
        assertEquals(expectedSex, domesticatedDog.getSex().name());
        assertEquals(expectedWeight, domesticatedDog.getWeightInGrams());
        assertEquals(expectedKindOfHair, domesticatedDog.getKindOfHair());
        assertEquals(expectedDateOfBirth, domesticatedDog.getDateOfBirth());
        assertEquals(expectedDateOfDeath, domesticatedDog.getDateOfDeath());
        assertEquals(expectedBreed, domesticatedDog.getBreed().name());
        assertEquals(expectedBreedGroup, domesticatedDog.getBreedGroup().name());
        assertEquals(expectedChipNumber, domesticatedDog.getChipNumber());
        assertEquals(expectedPersonID, domesticatedDog.getPerson().getId());
        assertEquals(expectedStatus, domesticatedDog.getDogStatus().name());
        verify(domesticatedDogRepository, times(1)).save(domesticatedDog);
    }

    @Test
    void patchDomesticatedDogWithNotFoundDog() {
        // Arrange
        when(domesticatedDogRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        // Act and Assert
        assertThrows(
                RecordNotFoundException.class,
                () -> domesticatedDogService.patchDomesticatedDog(42L, new DomesticatedDogPatchDto()),
                "Expect record not found exception to be thrown"
        );
    }

    @Test
    void storeDogImage() {
        // Arrange
        Long expectedID = 42L;
        DomesticatedDog domesticatedDog = new DomesticatedDog();
        domesticatedDog.setId(expectedID);
        when(domesticatedDogRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(domesticatedDog));
        String expectedBytes = "expectedBytes";
        MultipartFile file = new MockMultipartFile("file", expectedBytes.getBytes());
        // Act
        Long result = null;
        try {
            result = domesticatedDogService.storeDogImage(expectedID, file);
        } catch (Exception ex) {
            fail("Unexpected exception was thrown");
        }
        // Assert
        assertEquals(expectedID, result);
        assertArrayEquals(expectedBytes.getBytes(), domesticatedDog.getDogImage());
    }

    @Test
    void storeDogImageWithNoDogFound() {
        // Arrange
        when(domesticatedDogRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        String bytes = "bytes";
        MultipartFile file = new MockMultipartFile("file", bytes.getBytes());
        // Act and Assert
        try {
            assertThrows(
                    RecordNotFoundException.class,
                    () -> domesticatedDogService.storeDogImage(42L, file),
                    "Expected record not found exception to be thrown"
            );
        } catch (Exception ex) {
            fail("Unexpected exception was thrown");
        }
    }

    @Test
    void storeDogImageWithNoImageGiven() {
        // Arrange
        MultipartFile file = new MockMultipartFile("file", new byte[0]);
        // Act and Assert
        assertThrows(
                IOException.class,
                () -> domesticatedDogService.storeDogImage(42L, file),
                "Expected IOException to be thrown"
        );
    }

    @Test
    void deleteDogImage() {
        // Arrange
        DomesticatedDog domesticatedDog = new DomesticatedDog();
        domesticatedDog.setDogImage("bytes".getBytes());
        when(domesticatedDogRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(domesticatedDog));
        // Act
        domesticatedDogService.deleteDogImage(42L);
        // Assert
        assertNull(domesticatedDog.getDogImage());
        verify(domesticatedDogRepository, times(1)).save(domesticatedDog);
    }

    @Test
    void deleteDogImageWithNoDogFound() {
        // Arrange
        when(domesticatedDogRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        // Act and Assert
        assertThrows(
                RecordNotFoundException.class,
                () -> domesticatedDogService.deleteDogImage(42L),
                "Expected record not found exception to be thrown"
        );
    }

    @Test
    void deleteDogImageWithDogWithoutImage() {
        // Arrange
        when(domesticatedDogRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(new DomesticatedDog()));
        // Act and Assert
        assertThrows(
                RecordNotFoundException.class,
                () -> domesticatedDogService.deleteDogImage(42L),
                "Expected record not found exception to be thrown"
        );
    }

    @Test
    void deleteDomesticatedDog() {
    }
}