package appointment.booking.mapping;

import appointment.booking.dto.AppointmentDTO;
import appointment.booking.model.Appointment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {

    @Mapping(source = "customer.idCustomer", target = "idCustomer")
    @Mapping(source = "technician.idTechnician", target = "idTechnician")
    @Mapping(source = "service.idService", target = "idService")
    AppointmentDTO toDTO(Appointment appointment);

    @Mapping(source = "idCustomer", target = "customer.idCustomer")
    @Mapping(source = "idTechnician", target = "technician.idTechnician")
    @Mapping(source = "idService", target = "service.idService")
    Appointment toEntity(AppointmentDTO appointmentDTO);
}
