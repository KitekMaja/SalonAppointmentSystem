package appointment.booking.service;

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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AppointmentService {
    public static final String CUSTOMER_WITH_ID_NOT_FOUND = "Customer not found with ID: ";
    public static final String TECHNICIAN_WITH_ID_NOT_FOUND = "Technician not found with ID: ";
    public static final String SERVICE_WITH_ID_NOT_FOUND = "Service not found with ID: ";
    public static final String APPOINTMENT_WITH_ID_NOT_FOUND = "Appointment not found with ID: ";
    public static final String BOTH_START_TIME_AND_END_TIME_MUST_BE_PROVIDED = "Both start time and end time must be provided.";
    public static final String END_TIME_MUST_BE_AFTER_START_TIME = "End time must be after start time.";
    public static final String TECHNICIAN_NOT_AVAILABLE = "Technician is not available for the selected time slot.";
    private final AppointmentRepository appointmentRepository;
    private final AppointmentMapper appointmentMapper;
    private final CustomerRepository customerRepository;
    private final TechnicianRepository technicianRepository;
    private final BeautyServiceRepository beautyServiceRepository;

    public AppointmentService(AppointmentRepository appointmentRepository, AppointmentMapper appointmentMapper, CustomerRepository customerRepository, TechnicianRepository technicianRepository, BeautyServiceRepository beautyServiceRepository) {
        this.appointmentRepository = appointmentRepository;
        this.appointmentMapper = appointmentMapper;
        this.customerRepository = customerRepository;
        this.technicianRepository = technicianRepository;
        this.beautyServiceRepository = beautyServiceRepository;
    }

    public List<AppointmentDTO> getAllAppointments() {
        return appointmentRepository.findAll().stream().map(appointmentMapper::toDTO).collect(Collectors.toList());
    }

    public AppointmentDTO getAppointmentById(Long id) {
        return appointmentRepository.findById(id).map(appointmentMapper::toDTO).orElseThrow(() -> new IllegalArgumentException(APPOINTMENT_WITH_ID_NOT_FOUND + id));
    }

    public AppointmentDTO bookAppointment(AppointmentDTO appointmentDTO) {
        if (appointmentDTO.getStartTime() == null || appointmentDTO.getEndTime() == null) {
            throw new IllegalArgumentException(BOTH_START_TIME_AND_END_TIME_MUST_BE_PROVIDED);
        }
        if (appointmentDTO.getEndTime().isBefore(appointmentDTO.getStartTime())) {
            throw new IllegalArgumentException(END_TIME_MUST_BE_AFTER_START_TIME);
        }

        Customer customer = customerRepository.findById(appointmentDTO.getIdCustomer()).orElseThrow(() -> new IllegalArgumentException(CUSTOMER_WITH_ID_NOT_FOUND + appointmentDTO.getIdCustomer()));
        Technician technician = technicianRepository.findById(appointmentDTO.getIdTechnician()).orElseThrow(() -> new IllegalArgumentException(TECHNICIAN_WITH_ID_NOT_FOUND + appointmentDTO.getIdTechnician()));
        BeautyService service = beautyServiceRepository.findById(appointmentDTO.getIdService()).orElseThrow(() -> new IllegalArgumentException(SERVICE_WITH_ID_NOT_FOUND + appointmentDTO.getIdService()));

        if (!isTechnicianAvailable(technician, appointmentDTO.getStartTime(), appointmentDTO.getEndTime())) {
            throw new IllegalArgumentException(TECHNICIAN_NOT_AVAILABLE);
        }

        Appointment appointment = appointmentMapper.toEntity(appointmentDTO);
        appointment.setCustomer(customer);
        appointment.setTechnician(technician);
        appointment.setService(service);

        return appointmentMapper.toDTO(appointmentRepository.save(appointment));
    }

    // current mock for later technician availability service
    private boolean isTechnicianAvailable(Technician technician, LocalDateTime startTime, LocalDateTime endTime) {
        List<Appointment> overlappingAppointments = appointmentRepository.findAllByTechnicianAndTimeRange(technician, startTime, endTime);
        return overlappingAppointments.isEmpty();
    }
}
