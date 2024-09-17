package appointment.booking.controller;

import appointment.booking.dto.TechnicianDTO;
import appointment.booking.service.TechnicianService;
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
class TechnicianControllerTest {
    private final Faker faker = new Faker();
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private TechnicianService technicianService;
    private MockMvc mockMvc;
    @InjectMocks
    private TechnicianController technicianController;
    private TechnicianDTO technicianDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(technicianController).build();
        technicianDTO = new TechnicianDTO();
        technicianDTO.setIdTechnician(faker.number().randomNumber());
        technicianDTO.setName(faker.name().fullName());
        technicianDTO.setSpecialty(faker.job().field());
    }

    @Test
    void testGetAllTechnicians() throws Exception {
        List<TechnicianDTO> technicianList = Collections.singletonList(technicianDTO);
        when(technicianService.getAllTechnicians()).thenReturn(technicianList);
        mockMvc.perform(get("/technicianAPI/api/v1/getTechnicians")).andExpect(status().isOk()).andExpect(jsonPath("$.size()").value(1)).andExpect(jsonPath("$[0].name").value(technicianDTO.getName()));
    }

    @Test
    void testCreateTechnician() throws Exception {
        when(technicianService.createTechnician(any(TechnicianDTO.class))).thenReturn(technicianDTO);
        mockMvc.perform(post("/technicianAPI/api/v1/createTechnician").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(technicianDTO))).andExpect(status().isCreated()).andExpect(jsonPath("$.idTechnician").value(technicianDTO.getIdTechnician())).andExpect(jsonPath("$.name").value(technicianDTO.getName()));
    }

    @Test
    void testGetTechnicianById() throws Exception {
        when(technicianService.getTechnicianById(anyLong())).thenReturn(technicianDTO);
        mockMvc.perform(get("/technicianAPI/api/v1/getTechnician/1")).andExpect(status().isOk()).andExpect(jsonPath("$.idTechnician").value(technicianDTO.getIdTechnician())).andExpect(jsonPath("$.name").value(technicianDTO.getName()));
    }

    @Test
    void testUpdateTechnician() throws Exception {
        when(technicianService.updateTechnician(anyLong(), any(TechnicianDTO.class))).thenReturn(technicianDTO);
        mockMvc.perform(put("/technicianAPI/api/v1/updateTechnician/1").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(technicianDTO))).andExpect(status().isOk()).andExpect(jsonPath("$.idTechnician").value(technicianDTO.getIdTechnician())).andExpect(jsonPath("$.name").value(technicianDTO.getName()));
    }
}
