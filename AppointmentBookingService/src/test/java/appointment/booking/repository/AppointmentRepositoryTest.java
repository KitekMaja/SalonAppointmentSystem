package appointment.booking.repository;

import appointment.booking.model.Appointment;
import appointment.booking.model.Technician;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AppointmentRepositoryTest {

    private final Faker faker = new Faker();
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private TechnicianRepository technicianRepository;
    private Technician technician;

    @BeforeEach
    void setUp() {
        technician = new Technician();
        technician.setName(faker.name().fullName());
        technician.setSpecialty(faker.job().field());
        technician = technicianRepository.save(technician);

        Appointment appointment1 = new Appointment();
        appointment1.setTechnician(technician);
        appointment1.setStartTime(LocalDateTime.now().plusHours(1));
        appointment1.setEndTime(LocalDateTime.now().plusHours(2));
        appointment1.setStatus("CONFIRMED");
        appointmentRepository.save(appointment1);

        Appointment appointment2 = new Appointment();
        appointment2.setTechnician(technician);
        appointment2.setStartTime(LocalDateTime.now().plusHours(3));
        appointment2.setEndTime(LocalDateTime.now().plusHours(4));
        appointment2.setStatus("CONFIRMED");
        appointmentRepository.save(appointment2);
    }

    @Test
    void testFindAllByTechnicianAndTimeRange_NoOverlap() {
        LocalDateTime searchStartTime = LocalDateTime.now().plusHours(5);
        LocalDateTime searchEndTime = LocalDateTime.now().plusHours(6);
        List<Appointment> results = appointmentRepository.findAllByTechnicianAndTimeRange(technician, searchStartTime, searchEndTime);
        assertThat(results).isEmpty();
    }

    @Test
    void testFindAllByTechnicianAndTimeRange_WithOverlap() {
        LocalDateTime searchStartTime = LocalDateTime.now().plusHours(1).plusMinutes(30);
        LocalDateTime searchEndTime = LocalDateTime.now().plusHours(2).plusMinutes(30);

        List<Appointment> results = appointmentRepository.findAllByTechnicianAndTimeRange(technician, searchStartTime, searchEndTime);
        assertThat(results).isNotEmpty();
        assertThat(results.size()).isEqualTo(1);
        assertThat(results.get(0).getTechnician()).isEqualTo(technician);
    }
}
