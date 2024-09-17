package appointment.booking.dto;

import appointment.booking.model.Appointment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BeautyServiceDTO {
    private Long idService;
    private String serviceName;
    private Duration duration;
    private double price;
    private Set<Appointment> appointments;
}
