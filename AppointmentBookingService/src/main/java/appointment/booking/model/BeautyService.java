package appointment.booking.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

import static jakarta.persistence.CascadeType.ALL;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BeautyService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idService;

    private String serviceName;
    private Duration duration;
    private double price;

    @OneToMany(cascade = ALL, mappedBy = "service")
    private Set<Appointment> appointments = new HashSet<>();

    public void setToAppointment(Appointment appointment) {
        if (appointments == null || !(appointments instanceof HashSet)) {
            appointments = new HashSet<>(appointments); // Create a mutable copy
        }
        appointments.add(appointment);
        appointment.setService(this);
    }
}
