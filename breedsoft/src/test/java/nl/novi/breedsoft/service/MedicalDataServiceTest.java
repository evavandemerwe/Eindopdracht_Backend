package nl.novi.breedsoft.service;

import nl.novi.breedsoft.dto.medicalDataDtos.MedicalDataInputDto;
import nl.novi.breedsoft.dto.medicalDataDtos.MedicalDataOutputDto;
import nl.novi.breedsoft.dto.medicalDataDtos.MedicalDataPatchDto;
import nl.novi.breedsoft.exception.RecordNotFoundException;
import nl.novi.breedsoft.model.management.DomesticatedDog;
import nl.novi.breedsoft.model.management.MedicalData;
import nl.novi.breedsoft.repository.DomesticatedDogRepository;
import nl.novi.breedsoft.repository.MedicalDataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MedicalDataServiceTest {

    @Mock
    MedicalDataRepository medicalDataRepository;

    @Mock
    DomesticatedDogRepository domesticatedDogRepository;

    MedicalDataService medicalDataService;

    @BeforeEach
    void setUp() {
        this.medicalDataService = new MedicalDataService(
                this.medicalDataRepository,
                this.domesticatedDogRepository
        );
    }

    @Test
    void getAllMedicalData() {
        // Arrange
        MedicalData medicalData = new MedicalData();
        Long expectedID = 42L;
        medicalData.setId(expectedID);
        LocalDate expectedMedicalTreatmentDate = LocalDate.now().plusDays(7);
        medicalData.setDateOfMedicalTreatment(expectedMedicalTreatmentDate);
        String expectedDiagnose = "expectedDiagnose";
        medicalData.setDiagnose(expectedDiagnose);
        String expectedTreatment = "expectedTreatment";
        medicalData.setTreatment(expectedTreatment);
        String expectedMedicine = "expectedMedicine";
        medicalData.setMedicine(expectedMedicine);
        DomesticatedDog domesticatedDog = new DomesticatedDog();
        Long expectedDogID = 42L;
        domesticatedDog.setId(expectedDogID);
        medicalData.setDomesticatedDog(domesticatedDog);
        List<MedicalData> medicalDataList = new ArrayList<>(List.of(medicalData));
        when(medicalDataRepository.findAll()).thenReturn(medicalDataList);
        // Act
        List<MedicalDataOutputDto> result = medicalDataService.getAllMedicalData();
        // Assert
        assertEquals(1, result.size());
        assertEquals(expectedID, result.get(0).getId());
        assertEquals(expectedMedicalTreatmentDate, result.get(0).getDateOfMedicalTreatment());
        assertEquals(expectedDiagnose, result.get(0).getDiagnose());
        assertEquals(expectedTreatment, result.get(0).getTreatment());
        assertEquals(expectedMedicine, result.get(0).getMedicine());
        assertEquals(expectedDogID, result.get(0).getDomesticatedDog().getId());
    }

    @Test
    void getMedicaLDataByDogId() {
        // Arrange
        MedicalData medicalData = new MedicalData();
        Long expectedID = 42L;
        medicalData.setId(expectedID);
        LocalDate expectedMedicalTreatmentDate = LocalDate.now().plusDays(7);
        medicalData.setDateOfMedicalTreatment(expectedMedicalTreatmentDate);
        String expectedDiagnose = "expectedDiagnose";
        medicalData.setDiagnose(expectedDiagnose);
        String expectedTreatment = "expectedTreatment";
        medicalData.setTreatment(expectedTreatment);
        String expectedMedicine = "expectedMedicine";
        medicalData.setMedicine(expectedMedicine);
        DomesticatedDog domesticatedDog = new DomesticatedDog();
        Long expectedDogID = 42L;
        domesticatedDog.setId(expectedDogID);
        medicalData.setDomesticatedDog(domesticatedDog);
        List<MedicalData> medicalDataList = new ArrayList<>(List.of(medicalData));
        when(medicalDataRepository.findAll()).thenReturn(medicalDataList);
        // Act
        List<MedicalDataOutputDto> result = medicalDataService.getmedicaldatabydogid(expectedDogID);
        // Assert
        assertEquals(1, result.size());
        assertEquals(expectedID, result.get(0).getId());
        assertEquals(expectedMedicalTreatmentDate, result.get(0).getDateOfMedicalTreatment());
        assertEquals(expectedDiagnose, result.get(0).getDiagnose());
        assertEquals(expectedTreatment, result.get(0).getTreatment());
        assertEquals(expectedMedicine, result.get(0).getMedicine());
        assertEquals(expectedDogID, result.get(0).getDomesticatedDog().getId());
    }

    @Test
    void getMedicaLDataByDogIdWithNoMedicalData() {
        // Arrange
        when(medicalDataRepository.findAll()).thenReturn(new ArrayList<>());
        // Act and Assert
        assertThrows(
                RecordNotFoundException.class,
                () -> medicalDataService.getmedicaldatabydogid(42L),
                "Expected record not found exception to be thrown"
        );
    }

    @Test
    void createMedicalData() {
        // Arrange
        MedicalDataInputDto medicalDataInputDto = new MedicalDataInputDto();
        Long expectedID = 42L;
        medicalDataInputDto.setId(expectedID);
        LocalDate expectedMedicalTreatmentDate = LocalDate.now().plusDays(7);
        medicalDataInputDto.setDateOfMedicalTreatment(expectedMedicalTreatmentDate);
        String expectedDiagnose = "expectedDiagnose";
        medicalDataInputDto.setDiagnose(expectedDiagnose);
        String expectedTreatment = "expectedTreatment";
        medicalDataInputDto.setTreatment(expectedTreatment);
        String expectedMedicine = "expectedMedicine";
        medicalDataInputDto.setMedicine(expectedMedicine);
        DomesticatedDog domesticatedDog = new DomesticatedDog();
        Long expectedDogID = 42L;
        domesticatedDog.setId(expectedDogID);
        medicalDataInputDto.setDomesticatedDog(domesticatedDog);
        when(domesticatedDogRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(domesticatedDog));
        final AtomicReference<MedicalData> medicalDataAtomicReference = new AtomicReference<>() ;
        when(medicalDataRepository.save(Mockito.any(MedicalData.class))).then(
            i -> {
                medicalDataAtomicReference.set((MedicalData) i.getArguments()[0]);
                return i.getArguments()[0];
            }
        );
        // Act
        medicalDataService.createMedicalData(medicalDataInputDto);
        MedicalData storedMedicalData = medicalDataAtomicReference.get();
        // Assert
        assertEquals(expectedID, storedMedicalData.getId());
        assertEquals(expectedMedicalTreatmentDate, storedMedicalData.getDateOfMedicalTreatment());
        assertEquals(expectedDiagnose, storedMedicalData.getDiagnose());
        assertEquals(expectedTreatment, storedMedicalData.getTreatment());
        assertEquals(expectedMedicine, storedMedicalData.getMedicine());
        assertEquals(expectedDogID, storedMedicalData.getDomesticatedDog().getId());
    }

    @Test
    void createMedicalDataWithNotFoundDog() {
        // Arrange
        MedicalDataInputDto medicalDataInputDto = new MedicalDataInputDto();
        DomesticatedDog domesticatedDog = new DomesticatedDog();
        Long expectedDogID = 42L;
        domesticatedDog.setId(expectedDogID);
        medicalDataInputDto.setDomesticatedDog(domesticatedDog);
        when(domesticatedDogRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        // Act and Assert
        assertThrows(
                RecordNotFoundException.class,
                () -> medicalDataService.createMedicalData(medicalDataInputDto),
                "Expected record not found exception to be thrown"
        );
    }

    @Test
    void createMedicalDataWithIncorrectDogID() {
        // Arrange
        MedicalDataInputDto medicalDataInputDto = new MedicalDataInputDto();
        DomesticatedDog domesticatedDog = new DomesticatedDog();
        Long expectedDogID = 0L;
        domesticatedDog.setId(expectedDogID);
        medicalDataInputDto.setDomesticatedDog(domesticatedDog);
        // Act and Assert
        assertThrows(
                RecordNotFoundException.class,
                () -> medicalDataService.createMedicalData(medicalDataInputDto),
                "Expected record not found exception to be thrown"
        );
    }

    @Test
    void updateMedicalData() {
        // Arrange
        MedicalDataInputDto medicalDataInputDto = new MedicalDataInputDto();
        Long expectedID = 42L;
        medicalDataInputDto.setId(expectedID);
        LocalDate expectedMedicalTreatmentDate = LocalDate.now().plusDays(7);
        medicalDataInputDto.setDateOfMedicalTreatment(expectedMedicalTreatmentDate);
        String expectedDiagnose = "expectedDiagnose";
        medicalDataInputDto.setDiagnose(expectedDiagnose);
        String expectedTreatment = "expectedTreatment";
        medicalDataInputDto.setTreatment(expectedTreatment);
        String expectedMedicine = "expectedMedicine";
        medicalDataInputDto.setMedicine(expectedMedicine);
        DomesticatedDog domesticatedDog = new DomesticatedDog();
        Long expectedDogID = 42L;
        domesticatedDog.setId(expectedDogID);
        medicalDataInputDto.setDomesticatedDog(domesticatedDog);
        when(domesticatedDogRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(domesticatedDog));
        when(medicalDataRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(new MedicalData()));
        // Act
        MedicalDataOutputDto result = medicalDataService.updateMedicalData(42L, medicalDataInputDto);
        // Assert
        assertEquals(expectedID, result.getId());
        assertEquals(expectedMedicalTreatmentDate, result.getDateOfMedicalTreatment());
        assertEquals(expectedDiagnose, result.getDiagnose());
        assertEquals(expectedTreatment, result.getTreatment());
        assertEquals(expectedMedicine, result.getMedicine());
        assertEquals(expectedDogID, result.getDomesticatedDog().getId());
    }

    @Test
    void updateMedicalDataWithNoDogFound() {
        // Arrange
        MedicalDataInputDto medicalDataInputDto = new MedicalDataInputDto();
        DomesticatedDog domesticatedDog = new DomesticatedDog();
        domesticatedDog.setId(42L);
        medicalDataInputDto.setDomesticatedDog(domesticatedDog);
        when(domesticatedDogRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        when(medicalDataRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(new MedicalData()));
        // Act and Assert
        assertThrows(
                RecordNotFoundException.class,
                () -> medicalDataService.updateMedicalData(42L, medicalDataInputDto),
                "Expected record not found exception to be thrown"
        );
    }

    @Test
    void updateMedicalDataWithNoExistingMedicalData() {
        // Arrange
        MedicalDataInputDto medicalDataInputDto = new MedicalDataInputDto();
        when(medicalDataRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        MedicalData medicalData = new MedicalData();
        Long expectedMedicalDataID = 42L;
        medicalData.setId(expectedMedicalDataID);
        when(medicalDataRepository.save(Mockito.any(MedicalData.class))).thenReturn(medicalData);
        when(medicalDataRepository.getReferenceById(Mockito.anyLong())).thenReturn(medicalData);
        // Act
        MedicalDataOutputDto result = medicalDataService.updateMedicalData(42L, medicalDataInputDto);
        // Assert
        verify(medicalDataRepository, times(1)).save(Mockito.any(MedicalData.class));
        assertEquals(expectedMedicalDataID, result.getId());
    }

    @Test
    void patchMedicalData() {
        // Arrange
        MedicalDataPatchDto medicalDataPatchDto = new MedicalDataPatchDto();
        LocalDate expectedMedicalTreatmentDate = LocalDate.now().plusDays(7);
        medicalDataPatchDto.setDateOfMedicalTreatment(expectedMedicalTreatmentDate);
        String expectedDiagnose = "expectedDiagnose";
        medicalDataPatchDto.setDiagnose(expectedDiagnose);
        String expectedTreatment = "expectedTreatment";
        medicalDataPatchDto.setTreatment(expectedTreatment);
        String expectedMedicine = "expectedMedicine";
        medicalDataPatchDto.setMedicine(expectedMedicine);
        DomesticatedDog domesticatedDog = new DomesticatedDog();
        Long expectedDogID = 42L;
        domesticatedDog.setId(expectedDogID);
        medicalDataPatchDto.setDomesticatedDog(domesticatedDog);
        when(domesticatedDogRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(new DomesticatedDog()));
        when(medicalDataRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(new MedicalData()));
        when(medicalDataRepository.save(Mockito.any(MedicalData.class))).thenAnswer(i -> i.getArguments()[0]);
        // Act
        MedicalDataOutputDto result = medicalDataService.patchMedicalData(42L, medicalDataPatchDto);
        // Assert
        assertEquals(expectedMedicalTreatmentDate, result.getDateOfMedicalTreatment());
        assertEquals(expectedDiagnose, result.getDiagnose());
        assertEquals(expectedTreatment, result.getTreatment());
        assertEquals(expectedMedicine, result.getMedicine());
        assertNotNull(result.getDomesticatedDog());
        verify(medicalDataRepository, times(1)).save(Mockito.any(MedicalData.class));
    }

    @Test
    void patchMedicalDataWithNoDogFound() {
        // Arrange
        MedicalDataPatchDto medicalDataPatchDto = new MedicalDataPatchDto();
        DomesticatedDog domesticatedDog = new DomesticatedDog();
        domesticatedDog.setId(42L);
        medicalDataPatchDto.setDomesticatedDog(domesticatedDog);
        when(domesticatedDogRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        when(medicalDataRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(new MedicalData()));
        // Act and Assert
        assertThrows(
                RecordNotFoundException.class,
                () -> medicalDataService.patchMedicalData(42L, medicalDataPatchDto),
                "Expected record not found exception to be thrown"
        );
    }

    @Test
    void patchMedicalDataWithNoMedicalDataFound() {
        // Arrange
        when(medicalDataRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        // Act and Assert
        assertThrows(
                RecordNotFoundException.class,
                () -> medicalDataService.patchMedicalData(42L, new MedicalDataPatchDto()),
                "Expected record not found exception to be thrown"
        );
    }

    @Test
    void deleteMedicalData() {
        // Arrange
        Long expectedID = 42L;
        when(medicalDataRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(new MedicalData()));
        // Act
        medicalDataService.deleteMedicalData(expectedID);
        // Assert
        verify(medicalDataRepository, times(1)).deleteById(expectedID);
    }

    @Test
    void deleteMedicalDataWithDog() {
        // Arrange
        Long expectedID = 42L;
        MedicalData medicalData = new MedicalData();
        medicalData.setDomesticatedDog(new DomesticatedDog());
        when(medicalDataRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(medicalData));
        // Act
        medicalDataService.deleteMedicalData(expectedID);
        // Assert
        assertNull(medicalData.getDomesticatedDog());
        verify(medicalDataRepository, times(1)).deleteById(expectedID);
    }


    @Test
    void deleteMedicalDataWithNoMedicalDataFound() {
        // Arrange
        when(medicalDataRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        // Act and Assert
        assertThrows(
                RecordNotFoundException.class,
                () -> medicalDataService.deleteMedicalData(42L),
                "Expected record not found exception to be thrown"
        );
    }
}
