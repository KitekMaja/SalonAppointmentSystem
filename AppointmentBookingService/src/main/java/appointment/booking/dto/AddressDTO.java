package appointment.booking.dto;

import appointment.booking.model.Customer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AddressDTO {
    private Long idAddress;
    private String street;
    private String city;
    private String postCode;
    private String country;
    private Long idCustomer;
}
