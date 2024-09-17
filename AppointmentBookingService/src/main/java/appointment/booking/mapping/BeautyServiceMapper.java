package appointment.booking.mapping;

import appointment.booking.dto.BeautyServiceDTO;
import appointment.booking.model.BeautyService;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BeautyServiceMapper {
    BeautyServiceDTO toDTO(BeautyService service);

    BeautyService toEntity(BeautyServiceDTO serviceDTO);
}
