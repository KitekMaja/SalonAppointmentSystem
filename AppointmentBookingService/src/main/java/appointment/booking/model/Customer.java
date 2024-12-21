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
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCustomer;

    private String name;
    private String surname;
    private String email;
    private String phoneNumber;

    @OneToMany(cascade = ALL, mappedBy = "customer")
    private Set<Address> addresses = new HashSet<>();

    @OneToMany(cascade = ALL, mappedBy = "customer")
    private Set<Appointment> appointments = new HashSet<>();

    public void addAddress(Address address) {
        if (addresses == null || !(addresses instanceof HashSet)) {
            addresses = new HashSet<>(addresses); // Create a mutable copy
        }
        addresses.add(address);
        address.setCustomer(this);
    }

    public void bookAppointment(Appointment appointment) {
        if (appointments == null || !(appointments instanceof HashSet)) {
            appointments = new HashSet<>(appointments); // Create a mutable copy
        }
        appointments.add(appointment);
        appointment.setCustomer(this);
    }
}
