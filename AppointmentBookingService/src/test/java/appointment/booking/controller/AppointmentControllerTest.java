package appointment.booking.controller;

import appointment.booking.dto.AppointmentDTO;
import appointment.booking.service.AppointmentService;
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

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AppointmentController.class)
class AppointmentControllerTest {
    private final Faker faker = new Faker();
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private AppointmentService appointmentService;
    @Autowired
    private MockMvc mockMvc;
    private AppointmentDTO appointmentDTO;

    @BeforeEach
    void setUp() {
        appointmentDTO = new AppointmentDTO();
        appointmentDTO.setIdAppointment(faker.number().randomNumber());
        appointmentDTO.setStartTime(LocalDateTime.now());
        appointmentDTO.setEndTime(LocalDateTime.now().plusHours(1));
        appointmentDTO.setStatus("CONFIRMED");
        appointmentDTO.setIdCustomer(faker.number().randomNumber());
        appointmentDTO.setIdTechnician(faker.number().randomNumber());
        appointmentDTO.setIdService(faker.number().randomNumber());
    }

    @Test
    void testGetAllAppointments() throws Exception {
        List<AppointmentDTO> appointmentList = Collections.singletonList(appointmentDTO);
        when(appointmentService.getAllAppointments()).thenReturn(appointmentList);
        mockMvc.perform(get("/appointmentAPI/api/v1/getAppointments")).andExpect(status().isOk()).andExpect(jsonPath("$.size()").value(1)).andExpect(jsonPath("$[0].status").value("CONFIRMED")).andExpect(jsonPath("$[0].idCustomer").value(appointmentDTO.getIdCustomer()));
    }

    @Test
    void testBookAppointment() throws Exception {
        when(appointmentService.bookAppointment(any(AppointmentDTO.class))).thenReturn(appointmentDTO);
        mockMvc.perform(post("/appointmentAPI/api/v1/bookAppointment").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(appointmentDTO))).andExpect(status().isCreated()).andExpect(jsonPath("$.idAppointment").value(appointmentDTO.getIdAppointment())).andExpect(jsonPath("$.status").value("CONFIRMED")).andExpect(jsonPath("$.idCustomer").value(appointmentDTO.getIdCustomer()));
    }

    @Test
    void testGetAppointmentById() throws Exception {
        when(appointmentService.getAppointmentById(anyLong())).thenReturn(appointmentDTO);
        mockMvc.perform(get("/appointmentAPI/api/v1/getAppointment/1")).andExpect(status().isOk()).andExpect(jsonPath("$.idAppointment").value(appointmentDTO.getIdAppointment())).andExpect(jsonPath("$.status").value("CONFIRMED")).andExpect(jsonPath("$.idCustomer").value(appointmentDTO.getIdCustomer()));
    }
}