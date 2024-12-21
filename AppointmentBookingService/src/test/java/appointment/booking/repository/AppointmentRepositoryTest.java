package appointment.booking.repository;

import appointment.booking.model.*;
import net.bytebuddy.asm.Advice;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class AppointmentRepositoryTest {

    public static final String STATUS_CANCELLED = "CANCELLED";
    public static final String STATUS_CONFIRMED = "CONFIRMED";
    public static final String STATUS_ON_HOLD = "ON HOLD";
    private final Faker faker = new Faker();
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private TechnicianRepository technicianRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private BeautyServiceRepository beautyServiceRepository;

    private Customer customer;
    private Technician technician;

    @BeforeEach
    void setUp()
    {
        customer = createCustomer();
        technician = createTechnician();
    }
    @Test
    @Rollback
    void createAndFindAppointmentByIdTest()
    {
        Appointment saved = createAppointment(technician, customer, STATUS_CONFIRMED);
        Optional<Appointment> found = appointmentRepository.findById(saved.getIdAppointment());
        assertEquals(saved.getStartTime(), found.get().getStartTime());
        assertEquals(saved.getEndTime(), found.get().getEndTime());
        assertEquals(saved.getStatus(), found.get().getStatus());
        assertEquals(saved.getCustomer(), found.get().getCustomer());
        assertEquals(saved.getTechnician(), found.get().getTechnician());
        assertEquals(saved.getService(), found.get().getService());
    }

    @Test
    @Rollback
    void findAllAppointmentsTest()
    {
        Appointment appt1 = createAppointment(technician, customer, STATUS_CONFIRMED);
        Appointment appt2 = createAppointment(technician, customer, STATUS_ON_HOLD);
        List<Appointment> createdAppointments = List.of(appt1, appt2);
        List<Appointment> foundAppointments = appointmentRepository.findAll();
        assertThat(foundAppointments).isNotEmpty();
        assertEquals(createdAppointments.size(), foundAppointments.size());
        assertThat(foundAppointments).contains(appt1);
        assertThat(foundAppointments).contains(appt2);
    }

    @Test
    @Rollback
    void updateAppointmentTest()
    {
        Appointment appt = createAppointment(technician, customer, STATUS_CONFIRMED);
        appt.setStatus(STATUS_CANCELLED);
        Appointment updatedAppointment = appointmentRepository.save(appt);
        assertEquals(STATUS_CANCELLED, updatedAppointment.getStatus());
        assertEquals(appt.getService(), updatedAppointment.getService());
    }

    @Test
    @Disabled
    void testFindAllByTechnicianAndTimeRangeNoOverlap() {
    }
    @Test
    @Disabled
    void testFindAllByTechnicianAndTimeRangeWithOverlap() {
    }

    private Technician createTechnician()
    {
        Technician technician = new Technician();
        String name = faker.name().firstName();
        String specialty = faker.dungeonsAndDragons().klasses();
        Set<Appointment> appointments = Set.of();
        technician.setName(name);
        technician.setSpecialty(specialty);
        technician.setAppointments(appointments);
        return technicianRepository.save(technician);
    }

    private Customer createCustomer() {
        StringBuilder sb = new StringBuilder();
        Customer customer = new Customer();
        String name = faker.name().firstName();
        String surname = faker.name().lastName();
        String email = sb.append(name).append(".").append(surname).append("@gmail.com").toString();
        customer.setName(name);
        customer.setSurname(surname);
        customer.setEmail(email);
        customer.setPhoneNumber(faker.phoneNumber().toString());
        customer.setAddresses(Set.of());
        customer.setAppointments(Set.of());
        return customerRepository.save(customer);
    }

    private Appointment createAppointment(Technician technician, Customer customer, String status) {
        BeautyService beautyService = createBeautyService();
        LocalDateTime appointmentStart = LocalDateTime.now().plusHours(1);
        Appointment appointment = new Appointment();
        appointment.setTechnician(technician);
        appointment.setCustomer(customer);
        appointment.setService(createBeautyService());
        appointment.setStartTime(appointmentStart);
        appointment.setEndTime(appointmentStart.plus(beautyService.getDuration()));
        appointment.setStatus(status);
        return appointmentRepository.save(appointment);
    }

    private BeautyService createBeautyService() {
        BeautyService newService = new BeautyService();
        newService.setServiceName(faker.commerce().productName());
        newService.setDuration(Duration.ofMinutes(faker.number().numberBetween(30, 120)));
        newService.setPrice(faker.number().randomDouble(2, 25, 100));
        return beautyServiceRepository.save(newService);
    }
}
