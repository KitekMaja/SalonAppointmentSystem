package appointment.booking.repository;

import appointment.booking.model.Address;
import appointment.booking.model.Appointment;
import appointment.booking.model.Customer;
import appointment.booking.model.Technician;
import net.datafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class TechnicianControllerTest {
    private final Faker faker = new Faker();

    @Autowired
    private TechnicianRepository technicianRepository;

    @Test
    @Rollback()
    void saveAndFindTechnicianByIdTest()
    {
        Technician saved = createTechnician();
        Technician found = technicianRepository.findById(saved.getIdTechnician()).orElse(null);
        assertEquals(saved.getName(), found.getName());
        assertEquals(saved.getSpecialty(), found.getSpecialty());
        assertEquals(saved.getAppointments(), found.getAppointments());
    }

    @Test
    @Rollback()
    void findAllTechniciansTest()
    {
        Technician technician1 = createTechnician();
        Technician technician2 = createTechnician();
        List<Technician> createdTechniciansSet = List.of(technician1, technician2);
        List<Technician> foundTechniciansSet = technicianRepository.findAll();
        assertEquals(createdTechniciansSet.size(), foundTechniciansSet.size());
    }

    @Test
    @Rollback
    void updateTechnicianTest()
    {
        Technician technician = createTechnician();
        String specialty = faker.harryPotter().character();
        technician.setSpecialty(specialty);
        technicianRepository.save(technician);
        Optional<Technician> updatedTechnician = technicianRepository.findById(technician.getIdTechnician());
        assertEquals(technician.getName(), updatedTechnician.get().getName());
        assertEquals(specialty, updatedTechnician.get().getSpecialty());
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
}
