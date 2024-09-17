package appointment.booking.controller;

import appointment.booking.dto.AddressDTO;
import appointment.booking.dto.CustomerDTO;
import appointment.booking.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Disabled
class CustomerControllerTest {

    private final Faker faker = new Faker();
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private CustomerService customerService;
    private MockMvc mockMvc;
    @InjectMocks
    private CustomerController customerController;
    private CustomerDTO customerDTO;
    private AddressDTO addressDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
        customerDTO = new CustomerDTO();
        customerDTO.setIdCustomer(faker.number().randomNumber());
        customerDTO.setName(faker.name().firstName());
        customerDTO.setSurname(faker.name().lastName());
        customerDTO.setEmail(faker.internet().emailAddress());
        customerDTO.setPhoneNumber(faker.phoneNumber().phoneNumber());

        addressDTO = new AddressDTO();
        addressDTO.setIdAddress(faker.number().randomNumber());
        addressDTO.setStreet(faker.address().streetAddress());
        addressDTO.setCity(faker.address().city());
        addressDTO.setPostCode(faker.address().zipCode());
        addressDTO.setCountry(faker.address().country());
    }

    @Test
    void testGetAllCustomers() throws Exception {
        List<CustomerDTO> customerList = Collections.singletonList(customerDTO);
        when(customerService.getAllCustomers()).thenReturn(customerList);
        mockMvc.perform(get("/customerAPI/api/v1/getCustomers")).andExpect(status().isOk()).andExpect(jsonPath("$.size()").value(1)).andExpect(jsonPath("$[0].name").value(customerDTO.getName()));
    }

    @Test
    void testCreateCustomer() throws Exception {
        when(customerService.createCustomer(any(CustomerDTO.class))).thenReturn(customerDTO);
        mockMvc.perform(post("/customerAPI/api/v1/createCustomer").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(customerDTO))).andExpect(status().isCreated()).andExpect(jsonPath("$.idCustomer").value(customerDTO.getIdCustomer())).andExpect(jsonPath("$.name").value(customerDTO.getName()));
    }

    @Test
    void testGetCustomerById() throws Exception {
        when(customerService.getCustomerById(anyLong())).thenReturn(customerDTO);
        mockMvc.perform(get("/customerAPI/api/v1/getCustomer/1")).andExpect(status().isOk()).andExpect(jsonPath("$.idCustomer").value(customerDTO.getIdCustomer())).andExpect(jsonPath("$.name").value(customerDTO.getName()));
    }

    @Test
    void testUpdateCustomer() throws Exception {
        when(customerService.updateCustomer(anyLong(), any(CustomerDTO.class))).thenReturn(customerDTO);
        mockMvc.perform(put("/customerAPI/api/v1/updateCustomer/1").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(customerDTO))).andExpect(status().isOk()).andExpect(jsonPath("$.idCustomer").value(customerDTO.getIdCustomer())).andExpect(jsonPath("$.name").value(customerDTO.getName()));
    }

    @Test
    void testAddAddressToCustomer() throws Exception {
        when(customerService.addAddressToCustomer(anyLong(), any(AddressDTO.class))).thenReturn(addressDTO);
        mockMvc.perform(post("/customerAPI/api/v1/getCustomerAddresses/1/addresses").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(addressDTO))).andExpect(status().isCreated()).andExpect(jsonPath("$.idAddress").value(addressDTO.getIdAddress())).andExpect(jsonPath("$.street").value(addressDTO.getStreet()));
    }
}
