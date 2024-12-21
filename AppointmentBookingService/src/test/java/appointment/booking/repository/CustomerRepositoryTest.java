package appointment.booking.repository;

import appointment.booking.model.Address;
import appointment.booking.model.Appointment;
import appointment.booking.model.Customer;
import net.datafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class CustomerRepositoryTest {
    private final Faker faker = new Faker();

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    @Rollback()
    void saveAndFindCustomerByIdTest() {
        Customer saved = createCustomer();
        Optional<Customer> found = customerRepository.findById(saved.getIdCustomer());
        assertEquals(saved.getName(), found.get().getName());
        assertEquals(saved.getSurname(), found.get().getSurname());
        assertEquals(saved.getPhoneNumber(), found.get().getPhoneNumber());
        assertEquals(saved.getEmail(), found.get().getEmail());
        assertEquals(saved.getAddresses(), found.get().getAddresses());
        assertEquals(saved.getAppointments(), found.get().getAppointments());
    }

    @Test
    @Rollback()
    void findAllCustomersTest() {
        Customer customer1 = createCustomer();
        Customer customer2 = createCustomer();
        List<Customer> createdCustomersList = List.of(customer1, customer2);
        List<Customer> foundCustomersList = customerRepository.findAll();
        assertThat(foundCustomersList).hasSizeGreaterThan(0);
        assertEquals(createdCustomersList.size(), foundCustomersList.size());
        for (int counter = 0; counter < foundCustomersList.size(); counter++) {
            assertEquals(createdCustomersList.get(counter), foundCustomersList.get(counter));
        }
    }

    @Test
    @Rollback()
    void updateCustomerTest() {
        Customer customer = createCustomer();
        String surname = faker.name().lastName();
        customer.setSurname(surname);
        customerRepository.save(customer);
        Optional<Customer> updatedCustomer = customerRepository.findById(customer.getIdCustomer());
        assertEquals(customer.getName(), updatedCustomer.get().getName());
        assertEquals(surname, updatedCustomer.get().getSurname());
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
}
