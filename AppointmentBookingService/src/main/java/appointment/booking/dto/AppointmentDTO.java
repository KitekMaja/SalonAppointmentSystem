package appointment.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AppointmentDTO {
    private Long idAppointment;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;
    private Long idCustomer;
    private Long idTechnician;
    private Long idService;
}
