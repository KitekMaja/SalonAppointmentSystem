package appointment.booking.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private Set<Address> addresses;

    @OneToMany(cascade = ALL, mappedBy = "customer")
    private Set<Appointment> appointments;
}
