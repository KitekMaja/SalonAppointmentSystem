package appointment.booking.dto;

import appointment.booking.model.Address;
import appointment.booking.model.Appointment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CustomerDTO {
    private Long idCustomer;
    private String name;
    private String surname;
    private String email;
    private String phoneNumber;
    private Set<AddressDTO> addresses;
    private Set<AppointmentDTO> appointments;
}
