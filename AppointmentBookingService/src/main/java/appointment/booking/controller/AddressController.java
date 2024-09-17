package appointment.booking.controller;

import appointment.booking.dto.AddressDTO;
import appointment.booking.service.AddressService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/addressAPI/api/v1")
public class AddressController {
    private final AddressService addressService;
    private final Logger LOG = LoggerFactory.getLogger(AddressController.class);

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping("/getAddresses")
    public ResponseEntity<List<AddressDTO>> getAllAddresses() {
        LOG.info("Fetching all addresses...");
        return ResponseEntity.ok(addressService.getAllAddresses());
    }

    @PostMapping("/addAddress")
    public ResponseEntity<AddressDTO> createAddress(@RequestBody AddressDTO addressDTO) {
        AddressDTO createdAddress = addressService.createAddress(addressDTO);
        LOG.info("Creating an address...");
        return new ResponseEntity<>(createdAddress, HttpStatus.CREATED);
    }

    @GetMapping("/getAddress/{id}")
    public ResponseEntity<AddressDTO> getAddressById(@PathVariable Long id) {
        LOG.info("Fetching an address...");
        AddressDTO address = addressService.getAddressById(id);
        return ResponseEntity.ok(address);
    }
}
