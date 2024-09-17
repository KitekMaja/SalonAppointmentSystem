package appointment.booking.service;

import appointment.booking.dto.AddressDTO;
import appointment.booking.mapping.AddressMapper;
import appointment.booking.model.Address;
import appointment.booking.model.Customer;
import appointment.booking.repository.AddressRepository;
import appointment.booking.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AddressService {
    public static final String CUSTOMER_WITH_ID_NOT_FOUND = "Customer not found with ID: ";
    public static final String ADDRESS_WITH_ID_NOT_FOUND = "Address not found with ID: ";
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;
    private final CustomerRepository customerRepository;

    public AddressService(AddressRepository addressRepository, AddressMapper addressMapper, CustomerRepository customerRepository) {
        this.addressRepository = addressRepository;
        this.addressMapper = addressMapper;
        this.customerRepository = customerRepository;
    }

    public List<AddressDTO> getAllAddresses() {
        return addressRepository.findAll().stream().map(addressMapper::toDTO).collect(Collectors.toList());
    }

    public AddressDTO createAddress(AddressDTO addressDTO) {
        Address address = addressMapper.toEntity(addressDTO);
        Customer customer = customerRepository.findById(addressDTO.getIdCustomer()).orElseThrow(() -> new IllegalArgumentException(CUSTOMER_WITH_ID_NOT_FOUND + addressDTO.getIdCustomer()));
        address.setCustomer(customer);
        return addressMapper.toDTO(addressRepository.save(address));
    }

    public AddressDTO getAddressById(Long id) {
        return addressRepository.findById(id).map(addressMapper::toDTO).orElseThrow(() -> new IllegalArgumentException(ADDRESS_WITH_ID_NOT_FOUND + id));
    }
}
