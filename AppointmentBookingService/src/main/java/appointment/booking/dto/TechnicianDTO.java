package appointment.booking.dto;

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
public class TechnicianDTO {
    private Long idTechnician;
    private String name;
    private String specialty;
    private Set<Appointment> appointments;
}
