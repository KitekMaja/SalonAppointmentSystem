package appointment.booking.controller;

import appointment.booking.dto.BeautyServiceDTO;
import appointment.booking.service.BeautyServiceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/beautyServiceAPI/api/v1")
public class BeautyServiceController {
    private final BeautyServiceService beautyServiceService;
    private final Logger LOG = LoggerFactory.getLogger(BeautyServiceController.class);

    public BeautyServiceController(BeautyServiceService beautyServiceService) {
        this.beautyServiceService = beautyServiceService;
    }

    @GetMapping("/getBeautyServices")
    public ResponseEntity<List<BeautyServiceDTO>> getAllServices() {
        LOG.info("Fetching all services...");
        return ResponseEntity.ok(beautyServiceService.getAllServices());
    }

    @PostMapping("/addBeautyService")
    public ResponseEntity<BeautyServiceDTO> createService(@RequestBody BeautyServiceDTO serviceDTO) {
        BeautyServiceDTO createdService = beautyServiceService.createService(serviceDTO);
        LOG.info("Creating a service...");
        return new ResponseEntity<>(createdService, HttpStatus.CREATED);
    }

    @GetMapping("/getBeautyService/{id}")
    public ResponseEntity<BeautyServiceDTO> getServiceById(@PathVariable Long id) {
        BeautyServiceDTO service = beautyServiceService.getServiceById(id);
        LOG.info("Fetching a service...");
        return ResponseEntity.ok(service);
    }
}
