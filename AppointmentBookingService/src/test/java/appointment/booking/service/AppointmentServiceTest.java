import appointment.booking.dto.AppointmentDTO;
import appointment.booking.mapping.AppointmentMapper;
import appointment.booking.model.Appointment;
import appointment.booking.model.BeautyService;
import appointment.booking.model.Customer;
import appointment.booking.model.Technician;
import appointment.booking.repository.AppointmentRepository;
import appointment.booking.repository.BeautyServiceRepository;
import appointment.booking.repository.CustomerRepository;
import appointment.booking.repository.TechnicianRepository;
import appointment.booking.service.AppointmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class AppointmentServiceTest {
    @Mock
    private AppointmentRepository appointmentRepository;
    @Mock
    private AppointmentMapper appointmentMapper;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private TechnicianRepository technicianRepository;
    @Mock
    private BeautyServiceRepository beautyServiceRepository;
    @InjectMocks
    private AppointmentService appointmentService;
    private AppointmentDTO appointmentDTO;
    private Customer customer;
    private Technician technician;
    private BeautyService beautyService;
    private Appointment appointment;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        customer = new Customer();
        customer.setIdCustomer(1L);

        technician = new Technician();
        technician.setIdTechnician(1L);

        beautyService = new BeautyService();
        beautyService.setIdService(1L);

        appointmentDTO = new AppointmentDTO();
        appointmentDTO.setIdCustomer(1L);
        appointmentDTO.setIdTechnician(1L);
        appointmentDTO.setIdService(1L);
        appointmentDTO.setStartTime(LocalDateTime.now().plusHours(1));
        appointmentDTO.setEndTime(LocalDateTime.now().plusHours(2));

        appointment = new Appointment();
        appointment.setIdAppointment(1L);
        appointment.setCustomer(customer);
        appointment.setTechnician(technician);
        appointment.setService(beautyService);
    }

    @Test
    void testGetAllAppointments() {
        when(appointmentRepository.findAll()).thenReturn(Collections.singletonList(appointment));
        when(appointmentMapper.toDTO(any(Appointment.class))).thenReturn(appointmentDTO);

        List<AppointmentDTO> result = appointmentService.getAllAppointments();

        assertEquals(1, result.size());
        verify(appointmentRepository, times(1)).findAll();
    }

    @Test
    void testGetAppointmentById() {
        when(appointmentRepository.findById(anyLong())).thenReturn(Optional.of(appointment));
        when(appointmentMapper.toDTO(any(Appointment.class))).thenReturn(appointmentDTO);

        AppointmentDTO result = appointmentService.getAppointmentById(1L);

        assertNotNull(result);
        assertEquals(appointmentDTO.getIdCustomer(), result.getIdCustomer());
    }

    @Test
    void testBookAppointment_Success() {
        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));
        when(technicianRepository.findById(anyLong())).thenReturn(Optional.of(technician));
        when(beautyServiceRepository.findById(anyLong())).thenReturn(Optional.of(beautyService));
        when(appointmentRepository.findAllByTechnicianAndTimeRange(any(Technician.class), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Collections.emptyList());
        when(appointmentMapper.toEntity(any(AppointmentDTO.class))).thenReturn(appointment);
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(appointment);
        when(appointmentMapper.toDTO(any(Appointment.class))).thenReturn(appointmentDTO);

        AppointmentDTO result = appointmentService.bookAppointment(appointmentDTO);

        assertNotNull(result);
        assertEquals(appointmentDTO.getIdCustomer(), result.getIdCustomer());
        verify(appointmentRepository, times(1)).save(any(Appointment.class));
    }

    @Test
    void testBookAppointment_MissingTimes() {
        appointmentDTO.setStartTime(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            appointmentService.bookAppointment(appointmentDTO);
        });

        assertEquals(AppointmentService.BOTH_START_TIME_AND_END_TIME_MUST_BE_PROVIDED, exception.getMessage());
    }

    @Test
    void testBookAppointment_EndTimeBeforeStartTime() {
        appointmentDTO.setEndTime(appointmentDTO.getStartTime().minusHours(1));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            appointmentService.bookAppointment(appointmentDTO);
        });

        assertEquals(AppointmentService.END_TIME_MUST_BE_AFTER_START_TIME, exception.getMessage());
    }

    @Test
    void testBookAppointment_TechnicianNotAvailable() {
        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));
        when(technicianRepository.findById(anyLong())).thenReturn(Optional.of(technician));
        when(beautyServiceRepository.findById(anyLong())).thenReturn(Optional.of(beautyService));
        when(appointmentRepository.findAllByTechnicianAndTimeRange(any(Technician.class), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Collections.singletonList(appointment));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            appointmentService.bookAppointment(appointmentDTO);
        });

        assertEquals(AppointmentService.TECHNICIAN_NOT_AVAILABLE, exception.getMessage());
    }

    @Test
    void testBookAppointment_CustomerNotFound() {
        when(customerRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            appointmentService.bookAppointment(appointmentDTO);
        });

        assertEquals(AppointmentService.CUSTOMER_WITH_ID_NOT_FOUND + appointmentDTO.getIdCustomer(), exception.getMessage());
    }

    @Test
    void testBookAppointment_TechnicianNotFound() {
        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));
        when(technicianRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            appointmentService.bookAppointment(appointmentDTO);
        });

        assertEquals(AppointmentService.TECHNICIAN_WITH_ID_NOT_FOUND + appointmentDTO.getIdTechnician(), exception.getMessage());
    }

    @Test
    void testBookAppointment_ServiceNotFound() {
        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));
        when(technicianRepository.findById(anyLong())).thenReturn(Optional.of(technician));
        when(beautyServiceRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            appointmentService.bookAppointment(appointmentDTO);
        });

        assertEquals(AppointmentService.SERVICE_WITH_ID_NOT_FOUND + appointmentDTO.getIdService(), exception.getMessage());
    }
}
