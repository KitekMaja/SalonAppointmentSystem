package appointment.booking.repository;

import appointment.booking.model.Address;
import appointment.booking.model.Customer;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class AddressRepositoryTest {
    private final Faker faker = new Faker();
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private CustomerRepository customerRepository;
    private Customer customer;
    private Map<Long, Set<Address>> customers;

    @BeforeEach
    void setUp() {
        Customer customer = createCustomer();
        customers.put(customer.getIdCustomer(), Set.of());
    }

    @Test
    @Rollback
    void saveAndFindAddressById() {
        Address saved = createAddress();
        Optional<Address> found = addressRepository.findById(saved.getIdAddress());
        assertEquals(saved.getStreet(), found.get().getStreet());
        assertEquals(saved.getCity(), found.get().getCity());
        assertEquals(saved.getPostCode(), found.get().getPostCode());
        assertEquals(saved.getCountry(), found.get().getCountry());
        assertEquals(saved.getCustomer(), customer);
    }

    @Test
    @Rollback
    void findAllAddressesTest() {
        Address address1 = createAddress();
        Address address2 = createAddress();
        List<Address> createdAddressesList = List.of(address1, address2);
        List<Address> foundAddressesList = addressRepository.findAll();
        assertThat(foundAddressesList).hasSizeGreaterThan(0);
        assertEquals(createdAddressesList.size(), foundAddressesList.size());
        for (int counter = 0; counter < foundAddressesList.size(); counter++) {
            assertEquals(createdAddressesList.get(counter), foundAddressesList.get(counter));
        }
    }

    @Test
    @Rollback
    void updateTest()
    {
        Address address = createAddress();
        String updatedStreet = faker.address().streetAddress();
        address.setStreet(updatedStreet);
        Address updatedAddress = addressRepository.save(address);
        assertEquals(updatedStreet, updatedAddress.getStreet());
        assertEquals(address.getCity(), updatedAddress.getCity());
    }

    private Customer createCustomer() {
        customer = new Customer();
        customer.setName(faker.name().firstName());
        customer.setSurname(faker.name().lastName());
        customer.setEmail(faker.internet().emailAddress());
        customer.setPhoneNumber(faker.phoneNumber().phoneNumberNational());
        return customerRepository.save(customer);
    }

    private Address createAddress() {
        Address address = new Address();
        String street = faker.address().streetAddress();
        String city = faker.address().city();
        String postCode = faker.address().postcode();
        String country = faker.address().country();
        address.setStreet(street);
        address.setCity(city);
        address.setPostCode(postCode);
        address.setCountry(country);
        address.setCustomer(customer);
        customers.get(customer.getIdCustomer()).add(address);
        return addressRepository.save(address);
    }
}
