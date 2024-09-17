package appointment.booking.repository;

import appointment.booking.model.Address;
import appointment.booking.model.Customer;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // Uses the default H2 in-memory database
class AddressRepositoryTest {
    private final Faker faker = new Faker();
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private CustomerRepository customerRepository;
    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setName(faker.name().firstName());
        customer.setSurname(faker.name().lastName());
        customer.setEmail(faker.internet().emailAddress());
        customer.setPhoneNumber(faker.phoneNumber().phoneNumberNational());
        customer = customerRepository.save(customer);
    }

    @Test
    @Rollback(false)
    void testSaveAddress() {
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

        Address savedAddress = addressRepository.save(address);
        assertThat(savedAddress).isNotNull();
        assertThat(savedAddress.getIdAddress()).isNotNull();
        assertThat(savedAddress.getStreet()).isEqualTo(street);
        assertThat(savedAddress.getCity()).isEqualTo(city);
        assertThat(savedAddress.getPostCode()).isEqualTo(postCode);
        assertThat(savedAddress.getCountry()).isEqualTo(country);
        assertThat(savedAddress.getCustomer()).isEqualTo(customer);
    }

    @Test
    void testFindById() {
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
        Address savedAddress = addressRepository.save(address);

        Optional<Address> foundAddress = addressRepository.findById(savedAddress.getIdAddress());
        assertThat(foundAddress).isPresent();
        assertThat(foundAddress.get().getStreet()).isEqualTo(street);
        assertThat(foundAddress.get().getCity()).isEqualTo(city);
        assertThat(foundAddress.get().getPostCode()).isEqualTo(postCode);
        assertThat(foundAddress.get().getCountry()).isEqualTo(country);
    }

    @Test
    void testFindAll() {
        Address address1 = new Address();
        String street = faker.address().streetAddress();
        String city = faker.address().city();
        String postCode = faker.address().postcode();
        String country = faker.address().country();
        address1.setStreet(street);
        address1.setCity(city);
        address1.setPostCode(postCode);
        address1.setCountry(country);
        address1.setCustomer(customer);
        addressRepository.save(address1);

        Address address2 = new Address();
        String street2 = faker.address().streetAddress();
        String city2 = faker.address().city();
        String postCode2 = faker.address().postcode();
        String country2 = faker.address().country();
        address2.setStreet(street2);
        address2.setCity(city2);
        address2.setPostCode(postCode2);
        address2.setCountry(country2);
        address2.setCustomer(customer);
        addressRepository.save(address2);

        List<Address> addresses = addressRepository.findAll();
        assertThat(addresses).isNotEmpty();
        assertThat(addresses.size()).isEqualTo(2);
    }
}
