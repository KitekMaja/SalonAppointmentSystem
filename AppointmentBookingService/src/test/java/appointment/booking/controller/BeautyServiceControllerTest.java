package appointment.booking.controller;

import appointment.booking.dto.BeautyServiceDTO;
import appointment.booking.service.BeautyServiceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BeautyServiceController.class)
class BeautyServiceControllerTest {
    private final Faker faker = new Faker();
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private BeautyServiceService beautyServiceService;
    @Autowired
    private MockMvc mockMvc;
    private BeautyServiceDTO beautyServiceDTO;

    @BeforeEach
    void setUp() {
        beautyServiceDTO = new BeautyServiceDTO();
        beautyServiceDTO.setIdService(faker.number().randomNumber());
        beautyServiceDTO.setServiceName(faker.commerce().productName());
        beautyServiceDTO.setPrice(faker.number().randomDouble(2, 10, 100));
    }

    @Test
    void testGetAllServices() throws Exception {
        List<BeautyServiceDTO> serviceList = Collections.singletonList(beautyServiceDTO);
        when(beautyServiceService.getAllServices()).thenReturn(serviceList);
        mockMvc.perform(get("/beautyServiceAPI/api/v1/getBeautyServices")).andExpect(status().isOk()).andExpect(jsonPath("$.size()").value(1)).andExpect(jsonPath("$[0].serviceName").value(beautyServiceDTO.getServiceName()));
    }

    @Test
    void testCreateService() throws Exception {
        when(beautyServiceService.createService(any(BeautyServiceDTO.class))).thenReturn(beautyServiceDTO);
        mockMvc.perform(post("/beautyServiceAPI/api/v1/addBeautyService").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(beautyServiceDTO))).andExpect(status().isCreated()).andExpect(jsonPath("$.idService").value(beautyServiceDTO.getIdService())).andExpect(jsonPath("$.serviceName").value(beautyServiceDTO.getServiceName()));
    }

    @Test
    void testGetServiceById() throws Exception {
        when(beautyServiceService.getServiceById(anyLong())).thenReturn(beautyServiceDTO);
        mockMvc.perform(get("/beautyServiceAPI/api/v1/getBeautyService/1")).andExpect(status().isOk()).andExpect(jsonPath("$.idService").value(beautyServiceDTO.getIdService())).andExpect(jsonPath("$.serviceName").value(beautyServiceDTO.getServiceName()));
    }
}
