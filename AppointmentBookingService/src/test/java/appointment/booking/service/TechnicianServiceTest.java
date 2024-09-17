package appointment.booking.service;

import appointment.booking.dto.TechnicianDTO;
import appointment.booking.mapping.TechnicianMapper;
import appointment.booking.model.Technician;
import appointment.booking.repository.TechnicianRepository;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class TechnicianServiceTest {
    private static final Faker faker = new Faker();
    @Mock
    private TechnicianRepository technicianRepository;
    @Mock
    private TechnicianMapper technicianMapper;
    @InjectMocks
    private TechnicianService technicianService;

    private Technician technician;
    private TechnicianDTO technicianDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        technician = new Technician();
        technician.setIdTechnician(faker.number().randomNumber());
        technician.setName(faker.name().fullName());
        technician.setSpecialty(faker.job().field());

        technicianDTO = new TechnicianDTO();
        technicianDTO.setIdTechnician(technician.getIdTechnician());
        technicianDTO.setName(technician.getName());
        technicianDTO.setSpecialty(technician.getSpecialty());
    }

    @Test
    void testCreateTechnician() {
        when(technicianMapper.toEntity(any(TechnicianDTO.class))).thenReturn(technician);
        when(technicianRepository.save(any(Technician.class))).thenReturn(technician);
        when(technicianMapper.toDTO(any(Technician.class))).thenReturn(technicianDTO);
        TechnicianDTO result = technicianService.createTechnician(technicianDTO);
        assertNotNull(result);
        assertEquals(technicianDTO.getName(), result.getName());
        verify(technicianRepository, times(1)).save(any(Technician.class));
    }

    @Test
    void testGetTechnicianById() {
        when(technicianRepository.findById(anyLong())).thenReturn(Optional.of(technician));
        when(technicianMapper.toDTO(any(Technician.class))).thenReturn(technicianDTO);
        TechnicianDTO result = technicianService.getTechnicianById(technician.getIdTechnician());
        assertNotNull(result);
        assertEquals(technicianDTO.getIdTechnician(), result.getIdTechnician());
        verify(technicianRepository, times(1)).findById(anyLong());
    }

    @Test
    void testGetAllTechnicians() {
        when(technicianRepository.findAll()).thenReturn(Collections.singletonList(technician));
        when(technicianMapper.toDTO(any(Technician.class))).thenReturn(technicianDTO);
        List<TechnicianDTO> result = technicianService.getAllTechnicians();
        assertEquals(1, result.size());
        assertEquals(technicianDTO.getIdTechnician(), result.get(0).getIdTechnician());
        verify(technicianRepository, times(1)).findAll();
    }

    @Test
    void testUpdateTechnician() {
        when(technicianRepository.findById(anyLong())).thenReturn(Optional.of(technician));
        when(technicianRepository.save(any(Technician.class))).thenReturn(technician);
        when(technicianMapper.toDTO(any(Technician.class))).thenReturn(technicianDTO);
        TechnicianDTO result = technicianService.updateTechnician(technician.getIdTechnician(), technicianDTO);
        assertNotNull(result);
        assertEquals(technicianDTO.getName(), result.getName());
        assertEquals(technicianDTO.getSpecialty(), result.getSpecialty());
        verify(technicianRepository, times(1)).save(any(Technician.class));
    }

    @Test
    void testGetTechnicianById_NotFound() {
        when(technicianRepository.findById(anyLong())).thenReturn(Optional.empty());
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            technicianService.getTechnicianById(technician.getIdTechnician());
        });
        assertEquals(TechnicianService.TECHNICIAN_WITH_ID_NOT_FOUND + technician.getIdTechnician(), exception.getMessage());
    }
}
