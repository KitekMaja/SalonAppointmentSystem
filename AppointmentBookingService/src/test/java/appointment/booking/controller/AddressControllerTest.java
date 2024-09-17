package appointment.booking.controller;

import appointment.booking.dto.AddressDTO;
import appointment.booking.service.AddressService;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AddressController.class)
class AddressControllerTest {
    private final Faker faker = new Faker();
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private AddressService addressService;
    private AddressDTO addressDTO;

    @BeforeEach
    void setUp() {
        addressDTO = new AddressDTO();
        addressDTO.setIdAddress(faker.number().randomNumber());
        addressDTO.setStreet(faker.address().streetAddress());
        addressDTO.setCity(faker.address().city());
        addressDTO.setPostCode(faker.address().zipCode());
        addressDTO.setCountry(faker.address().country());
        addressDTO.setIdCustomer(faker.number().randomNumber());
    }

    @Test
    void testGetAllAddresses() throws Exception {
        List<AddressDTO> addressList = Collections.singletonList(addressDTO);
        when(addressService.getAllAddresses()).thenReturn(addressList);

        mockMvc.perform(get("/addressAPI/api/v1/getAddresses")).andExpect(status().isOk()).andExpect(jsonPath("$.size()").value(1)).andExpect(jsonPath("$[0].street").value(addressDTO.getStreet())).andExpect(jsonPath("$[0].city").value(addressDTO.getCity())).andExpect(jsonPath("$[0].postCode").value(addressDTO.getPostCode()));
    }

    @Test
    void testCreateAddress() throws Exception {
        when(addressService.createAddress(any(AddressDTO.class))).thenReturn(addressDTO);
        mockMvc.perform(post("/addressAPI/api/v1/addAddress").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(addressDTO))).andExpect(status().isCreated()).andExpect(jsonPath("$.idAddress").value(addressDTO.getIdAddress())).andExpect(jsonPath("$.street").value(addressDTO.getStreet())).andExpect(jsonPath("$.city").value(addressDTO.getCity())).andExpect(jsonPath("$.postCode").value(addressDTO.getPostCode()));
    }

    @Test
    void testGetAddressById() throws Exception {
        when(addressService.getAddressById(anyLong())).thenReturn(addressDTO);
        mockMvc.perform(get("/addressAPI/api/v1/getAddress/1")).andExpect(status().isOk()).andExpect(jsonPath("$.idAddress").value(addressDTO.getIdAddress())).andExpect(jsonPath("$.street").value(addressDTO.getStreet())).andExpect(jsonPath("$.city").value(addressDTO.getCity())).andExpect(jsonPath("$.postCode").value(addressDTO.getPostCode()));
    }
}
