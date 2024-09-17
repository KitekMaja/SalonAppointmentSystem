package appointment.booking.controller;

import appointment.booking.dto.TechnicianDTO;
import appointment.booking.service.TechnicianService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/technicianAPI/api/v1")
public class TechnicianController {
    private final TechnicianService technicianService;
    private final Logger LOG = LoggerFactory.getLogger(TechnicianController.class);

    public TechnicianController(TechnicianService technicianService) {
        this.technicianService = technicianService;
    }

    @GetMapping("/getTechnicians")
    public ResponseEntity<List<TechnicianDTO>> getAllTechnicians() {
        LOG.info("Fetching all technicians...");
        return ResponseEntity.ok(technicianService.getAllTechnicians());
    }

    @PostMapping("/createTechnician")
    public ResponseEntity<TechnicianDTO> createTechnician(@RequestBody TechnicianDTO technicianDTO) {
        TechnicianDTO createdTechnician = technicianService.createTechnician(technicianDTO);
        LOG.info("Creating a technician...");
        return new ResponseEntity<>(createdTechnician, HttpStatus.CREATED);
    }

    @GetMapping("/getTechnician/{id}")
    public ResponseEntity<TechnicianDTO> getTechnicianById(@PathVariable Long id) {
        TechnicianDTO technician = technicianService.getTechnicianById(id);
        LOG.info("Fetching a technician...");
        return ResponseEntity.ok(technician);
    }

    @PutMapping("/updateTechnician/{id}")
    public ResponseEntity<TechnicianDTO> updateTechnician(@PathVariable Long id, @RequestBody TechnicianDTO technicianDTO) {
        TechnicianDTO updatedTechnician = technicianService.updateTechnician(id, technicianDTO);
        LOG.info("Updating a technician...");
        return ResponseEntity.ok(updatedTechnician);
    }
}
