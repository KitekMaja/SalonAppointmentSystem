package appointment.booking.mapping;

import appointment.booking.dto.AddressDTO;
import appointment.booking.model.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    @Mapping(source = "customer.idCustomer", target = "idCustomer")
    AddressDTO toDTO(Address address);

    @Mapping(source = "idCustomer", target = "customer.idCustomer")
    Address toEntity(AddressDTO addressDTO);
}
