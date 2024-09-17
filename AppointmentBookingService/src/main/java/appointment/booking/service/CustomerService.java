package appointment.booking.service;

import appointment.booking.dto.AddressDTO;
import appointment.booking.dto.CustomerDTO;
import appointment.booking.mapping.AddressMapper;
import appointment.booking.mapping.CustomerMapper;
import appointment.booking.model.Address;
import appointment.booking.model.Customer;
import appointment.booking.repository.AddressRepository;
import appointment.booking.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CustomerService {
    public static final String CUSTOMER_WITH_ID_NOT_FOUND = "Customer not found with ID: ";
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;

    public CustomerService(CustomerRepository customerRepository, CustomerMapper customerMapper, AddressRepository addressRepository, AddressMapper addressMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
        this.addressRepository = addressRepository;
        this.addressMapper = addressMapper;
    }

    public List<CustomerDTO> getAllCustomers() {
        return customerRepository.findAll().stream().map(customerMapper::toDTO).collect(Collectors.toList());
    }

    public CustomerDTO createCustomer(CustomerDTO customerDTO) {
        Customer customer = customerMapper.toEntity(customerDTO);

        if (customerDTO.getAppointments() != null) {
            customer.setAppointments(customerMapper.toEntity(customerDTO).getAppointments());
        }
        return customerMapper.toDTO(customerRepository.save(customer));
    }

    public CustomerDTO getCustomerById(Long id) {
        return customerRepository.findById(id).map(customerMapper::toDTO).orElseThrow(() -> new IllegalArgumentException(CUSTOMER_WITH_ID_NOT_FOUND + id));
    }

    public CustomerDTO updateCustomer(Long id, CustomerDTO customerDTO) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(CUSTOMER_WITH_ID_NOT_FOUND + id));
        customer.setName(customerDTO.getName());
        customer.setSurname(customerDTO.getSurname());
        customer.setEmail(customerDTO.getEmail());
        customer.setPhoneNumber(customerDTO.getPhoneNumber());
        customer.setAddresses(customerMapper.toEntity(customerDTO).getAddresses());
        customer.setAppointments(customerMapper.toEntity(customerDTO).getAppointments());

        return customerMapper.toDTO(customerRepository.save(customer));
    }

    public AddressDTO addAddressToCustomer(Long customerId, AddressDTO addressDTO) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException(CUSTOMER_WITH_ID_NOT_FOUND + customerId));

        Address address = addressMapper.toEntity(addressDTO);
        address.setCustomer(customer);

        Address savedAddress = addressRepository.save(address);
        customer.getAddresses().add(savedAddress);
        customerRepository.save(customer);

        return addressMapper.toDTO(savedAddress);
    }
}