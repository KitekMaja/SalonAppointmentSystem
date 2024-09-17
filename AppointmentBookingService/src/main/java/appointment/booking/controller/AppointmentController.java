package appointment.booking.controller;

import appointment.booking.dto.AppointmentDTO;
import appointment.booking.service.AppointmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/appointmentAPI/api/v1")
public class AppointmentController {
    private final AppointmentService appointmentService;
    private final Logger LOG = LoggerFactory.getLogger(AppointmentController.class);

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping("/getAppointments")
    public ResponseEntity<List<AppointmentDTO>> getAllAppointments() {
        LOG.info("Fetching all appointments...");
        return ResponseEntity.ok(appointmentService.getAllAppointments());
    }

    @PostMapping("/bookAppointment")
    public ResponseEntity<AppointmentDTO> bookAppointment(@RequestBody AppointmentDTO appointmentDTO) {
        AppointmentDTO createdAppointment = appointmentService.bookAppointment(appointmentDTO);
        LOG.info("Creating an appointment...");
        return new ResponseEntity<>(createdAppointment, HttpStatus.CREATED);
    }

    @GetMapping("/getAppointment/{id}")
    public ResponseEntity<AppointmentDTO> getAppointmentById(@PathVariable Long id) {
        AppointmentDTO appointment = appointmentService.getAppointmentById(id);
        LOG.info("Fetching an appointment...");
        return ResponseEntity.ok(appointment);
    }
}
