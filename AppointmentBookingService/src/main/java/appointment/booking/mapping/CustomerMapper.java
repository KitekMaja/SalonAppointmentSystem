package appointment.booking.mapping;

import appointment.booking.dto.CustomerDTO;
import appointment.booking.model.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {AddressMapper.class, AppointmentMapper.class})
public interface CustomerMapper {

    @Mapping(source = "addresses", target = "addresses")
    @Mapping(source = "appointments", target = "appointments")
    CustomerDTO toDTO(Customer customer);

    @Mapping(source = "addresses", target = "addresses")
    @Mapping(source = "appointments", target = "appointments")
    Customer toEntity(CustomerDTO customerDTO);
}
