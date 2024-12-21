package appointment.booking.repository;

import appointment.booking.model.Address;
import appointment.booking.model.Customer;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class AddressRepositoryTest {
    private final Faker faker = new Faker();
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private CustomerRepository customerRepository;
    private Customer customer;
    private Map<Long, Set<Address>> customers = new HashMap<>();

    @BeforeEach
    void setUp() {
        customer = createCustomer();
        customers.put(customer.getIdCustomer(), Set.of());
    }

    @Test
    @Rollback
    void saveAndFindAddressById() {
        Address saved = createAddress(customer);
        Optional<Address> found = addressRepository.findById(saved.getIdAddress());
        assertEquals(saved.getStreet(), found.get().getStreet());
        assertEquals(saved.getCity(), found.get().getCity());
        assertEquals(saved.getPostCode(), found.get().getPostCode());
        assertEquals(saved.getCountry(), found.get().getCountry());
        assertTrue(customer.getAddresses().contains(found.get()));
    }

    @Test
    @Rollback
    void findAllAddressesTest() {
        Address address1 = createAddress(customer);
        Address address2 = createAddress(customer);
        List<Address> createdAddressesList = List.of(address1, address2);
        List<Address> foundAddressesList = addressRepository.findAll();
        assertThat(foundAddressesList).isNotEmpty();
        assertThat(foundAddressesList).contains(address1);
        assertThat(foundAddressesList).contains(address2);
        assertEquals(createdAddressesList.size(), foundAddressesList.size());
    }

    @Test
    @Rollback
    void updateTest()
    {
        Address address = createAddress(customer);
        String updatedStreet = faker.address().streetAddress();
        address.setStreet(updatedStreet);
        Address updatedAddress = addressRepository.save(address);
        assertEquals(updatedStreet, updatedAddress.getStreet());
        assertEquals(address.getCity(), updatedAddress.getCity());
        assertTrue(customer.getAddresses().contains(updatedAddress));
    }

    private Customer createCustomer() {
        customer = new Customer();
        customer.setName(faker.name().firstName());
        customer.setSurname(faker.name().lastName());
        customer.setEmail(faker.internet().emailAddress());
        customer.setPhoneNumber(faker.phoneNumber().phoneNumberNational());
        customer.setAddresses(Set.of());
        return customerRepository.save(customer);
    }

    private Address createAddress(Customer customer) {
        Address address = new Address();
        address.setStreet( faker.address().streetAddress());
        address.setCity(faker.address().city());
        address.setPostCode(faker.address().postcode());
        address.setCountry(faker.address().country());
        customer.addAddress(address);
        Address savedAddress = addressRepository.save(address);
        addAddressToList(customer, savedAddress);
        return addressRepository.save(address);
    }

    private void addAddressToList(Customer customer, Address savedAddress) {
        Set<Address> customerAddresses = customer.getAddresses();
        customerAddresses.add(savedAddress);
        customers.put(customer.getIdCustomer(), customerAddresses);
    }
}
