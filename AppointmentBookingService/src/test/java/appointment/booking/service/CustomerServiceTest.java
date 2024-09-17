package appointment.booking.service;

import appointment.booking.dto.AddressDTO;
import appointment.booking.dto.CustomerDTO;
import appointment.booking.mapping.AddressMapper;
import appointment.booking.mapping.CustomerMapper;
import appointment.booking.model.Address;
import appointment.booking.model.Customer;
import appointment.booking.repository.AddressRepository;
import appointment.booking.repository.CustomerRepository;
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

class CustomerServiceTest {
    private static final Faker faker = new Faker();
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private CustomerMapper customerMapper;
    @Mock
    private AddressRepository addressRepository;
    @Mock
    private AddressMapper addressMapper;
    @InjectMocks
    private CustomerService customerService;

    private Customer customer;
    private CustomerDTO customerDTO;
    private Address address;
    private AddressDTO addressDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        customer = new Customer();
        customer.setIdCustomer(faker.number().randomNumber());
        customer.setName(faker.name().firstName());
        customer.setSurname(faker.name().lastName());
        customer.setEmail(faker.internet().emailAddress());
        customer.setPhoneNumber(faker.phoneNumber().phoneNumber());

        customerDTO = new CustomerDTO();
        customerDTO.setIdCustomer(customer.getIdCustomer());
        customerDTO.setName(customer.getName());
        customerDTO.setSurname(customer.getSurname());
        customerDTO.setEmail(customer.getEmail());
        customerDTO.setPhoneNumber(customer.getPhoneNumber());

        address = new Address();
        address.setIdAddress(faker.number().randomNumber());
        address.setStreet(faker.address().streetAddress());
        address.setCity(faker.address().city());
        address.setPostCode(faker.address().zipCode());
        address.setCountry(faker.address().country());

        addressDTO = new AddressDTO();
        addressDTO.setIdAddress(address.getIdAddress());
        addressDTO.setStreet(address.getStreet());
        addressDTO.setCity(address.getCity());
        addressDTO.setPostCode(address.getPostCode());
        addressDTO.setCountry(address.getCountry());
    }

    @Test
    void testGetAllCustomers() {
        when(customerRepository.findAll()).thenReturn(Collections.singletonList(customer));
        when(customerMapper.toDTO(any(Customer.class))).thenReturn(customerDTO);
        List<CustomerDTO> result = customerService.getAllCustomers();
        assertEquals(1, result.size());
        assertEquals(customerDTO.getIdCustomer(), result.get(0).getIdCustomer());
        verify(customerRepository, times(1)).findAll();
    }

    @Test
    void testCreateCustomer() {
        when(customerMapper.toEntity(any(CustomerDTO.class))).thenReturn(customer);
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        when(customerMapper.toDTO(any(Customer.class))).thenReturn(customerDTO);
        CustomerDTO result = customerService.createCustomer(customerDTO);
        assertNotNull(result);
        assertEquals(customerDTO.getName(), result.getName());
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void testGetCustomerById() {
        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));
        when(customerMapper.toDTO(any(Customer.class))).thenReturn(customerDTO);
        CustomerDTO result = customerService.getCustomerById(customer.getIdCustomer());
        assertNotNull(result);
        assertEquals(customerDTO.getIdCustomer(), result.getIdCustomer());
        verify(customerRepository, times(1)).findById(anyLong());
    }

    @Test
    void testUpdateCustomer() {
        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        when(customerMapper.toEntity(any(CustomerDTO.class))).thenReturn(customer);
        when(customerMapper.toDTO(any(Customer.class))).thenReturn(customerDTO);
        CustomerDTO result = customerService.updateCustomer(customer.getIdCustomer(), customerDTO);
        assertNotNull(result);
        assertEquals(customerDTO.getName(), result.getName());
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void testAddAddressToCustomer() {
        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));
        when(addressMapper.toEntity(any(AddressDTO.class))).thenReturn(address);
        when(addressRepository.save(any(Address.class))).thenReturn(address);
        when(addressMapper.toDTO(any(Address.class))).thenReturn(addressDTO);
        AddressDTO result = customerService.addAddressToCustomer(customer.getIdCustomer(), addressDTO);
        assertNotNull(result);
        assertEquals(addressDTO.getStreet(), result.getStreet());
        verify(addressRepository, times(1)).save(any(Address.class));
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void testGetCustomerById_NotFound() {
        when(customerRepository.findById(anyLong())).thenReturn(Optional.empty());
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            customerService.getCustomerById(customer.getIdCustomer());
        });
        assertEquals(CustomerService.CUSTOMER_WITH_ID_NOT_FOUND + customer.getIdCustomer(), exception.getMessage());
    }
}
