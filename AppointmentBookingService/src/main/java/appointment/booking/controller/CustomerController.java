package appointment.booking.controller;

import appointment.booking.dto.AddressDTO;
import appointment.booking.dto.CustomerDTO;
import appointment.booking.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customerAPI/api/v1")
public class CustomerController {
    private final CustomerService customerService;
    private final Logger LOG = LoggerFactory.getLogger(CustomerController.class);

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/getCustomers")
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        LOG.info("Fetching all customers...");
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    @PostMapping("/createCustomer")
    public ResponseEntity<CustomerDTO> createCustomer(@RequestBody CustomerDTO customerDTO) {
        CustomerDTO createdCustomer = customerService.createCustomer(customerDTO);
        LOG.info("Creating a customer...");
        return new ResponseEntity<>(createdCustomer, HttpStatus.CREATED);
    }

    @GetMapping("/getCustomer/{id}")
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable Long id) {
        CustomerDTO customer = customerService.getCustomerById(id);
        LOG.info("Fetching a customer...");
        return ResponseEntity.ok(customer);
    }

    @PutMapping("/updateCustomer/{id}")
    public ResponseEntity<CustomerDTO> updateCustomer(@PathVariable Long id, @RequestBody CustomerDTO customerDTO) {
        CustomerDTO updatedCustomer = customerService.updateCustomer(id, customerDTO);
        LOG.info("Updating a customer...");
        return ResponseEntity.ok(updatedCustomer);
    }

    @PostMapping("/getCustomerAddresses/{customerId}/addresses")
    public ResponseEntity<AddressDTO> addAddressToCustomer(@PathVariable Long customerId, @RequestBody AddressDTO addressDTO) {
        AddressDTO addedAddress = customerService.addAddressToCustomer(customerId, addressDTO);
        LOG.info("Updating a customer with an address...");
        return new ResponseEntity<>(addedAddress, HttpStatus.CREATED);
    }
}
