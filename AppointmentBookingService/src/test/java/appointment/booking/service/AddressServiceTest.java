package appointment.booking.controller;


import appointment.booking.dto.AddressDTO;
import appointment.booking.mapping.AddressMapper;
import appointment.booking.model.Address;
import appointment.booking.model.Customer;
import appointment.booking.repository.AddressRepository;
import appointment.booking.repository.CustomerRepository;
import appointment.booking.service.AddressService;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddressServiceTest {
    private final Faker faker = new Faker();
    @Mock
    private AddressRepository addressRepository;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private AddressMapper addressMapper;
    @InjectMocks
    private AddressService addressService;

    private Address address;
    private AddressDTO addressDTO;
    private Customer customer;

    @BeforeEach
    void setUp() {
        address = new Address();
        address.setIdAddress(1L);
        address.setStreet(faker.address().streetAddress());
        address.setCity(faker.address().city());

        addressDTO = new AddressDTO();
        addressDTO.setIdAddress(1L);
        addressDTO.setStreet(faker.address().streetAddress());
        addressDTO.setCity(faker.address().city());
        addressDTO.setIdCustomer(1L);

        customer = new Customer();
        customer.setIdCustomer(1L);
    }

    @Test
    void testGetAllAddresses() {
        List<Address> addresses = new ArrayList<>();
        addresses.add(address);
        when(addressRepository.findAll()).thenReturn(addresses);
        when(addressMapper.toDTO(any(Address.class))).thenReturn(addressDTO);

        List<AddressDTO> result = addressService.getAllAddresses();
        assertThat(result).isNotEmpty();
        assertThat(result.size()).isEqualTo(1);
        verify(addressRepository, times(1)).findAll();
    }

    @Test
    void testCreateAddress() {
        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));
        when(addressMapper.toEntity(any(AddressDTO.class))).thenReturn(address);
        when(addressRepository.save(any(Address.class))).thenReturn(address);
        when(addressMapper.toDTO(any(Address.class))).thenReturn(addressDTO);

        AddressDTO result = addressService.createAddress(addressDTO);
        assertThat(result).isNotNull();
        assertThat(result.getIdAddress()).isEqualTo(addressDTO.getIdAddress());
        verify(addressRepository, times(1)).save(address);
        verify(customerRepository, times(1)).findById(addressDTO.getIdCustomer());
    }

    @Test
    void testCreateAddress_CustomerNotFound() {
        when(customerRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> addressService.createAddress(addressDTO)).isInstanceOf(IllegalArgumentException.class).hasMessageContaining(AddressService.CUSTOMER_WITH_ID_NOT_FOUND + addressDTO.getIdCustomer());
        verify(customerRepository, times(1)).findById(addressDTO.getIdCustomer());
        verify(addressRepository, never()).save(any(Address.class));
    }

    @Test
    void testGetAddressById() {
        when(addressRepository.findById(anyLong())).thenReturn(Optional.of(address));
        when(addressMapper.toDTO(any(Address.class))).thenReturn(addressDTO);
        AddressDTO result = addressService.getAddressById(1L);
        assertThat(result).isNotNull();
        assertThat(result.getIdAddress()).isEqualTo(1L);
        verify(addressRepository, times(1)).findById(1L);
    }

    @Test
    void testGetAddressById_NotFound() {
        when(addressRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> addressService.getAddressById(1L)).isInstanceOf(IllegalArgumentException.class).hasMessageContaining(AddressService.ADDRESS_WITH_ID_NOT_FOUND + 1L);
        verify(addressRepository, times(1)).findById(1L);
    }
}
