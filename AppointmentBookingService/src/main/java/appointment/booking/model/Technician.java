package appointment.booking.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

import static jakarta.persistence.CascadeType.ALL;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Technician {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTechnician;
    private String name;
    private String specialty;

    @OneToMany(cascade = ALL, mappedBy = "technician")
    private Set<Appointment> appointments = new HashSet<>();

    public void scheduleAppointment(Appointment appointment) {
        if (appointments == null || !(appointments instanceof HashSet)) {
            appointments = new HashSet<>(appointments); // Create a mutable copy
        }
        appointments.add(appointment);
        appointment.setTechnician(this);
    }
}
