package appointment.booking.mapping;

import appointment.booking.dto.TechnicianDTO;
import appointment.booking.model.Technician;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {AppointmentMapper.class})
public interface TechnicianMapper {

    @Mapping(source = "appointments", target = "appointments")
    TechnicianDTO toDTO(Technician technician);

    @Mapping(source = "appointments", target = "appointments")
    Technician toEntity(TechnicianDTO technicianDTO);
}
