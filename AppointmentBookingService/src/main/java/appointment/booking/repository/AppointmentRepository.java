package appointment.booking.repository;

import appointment.booking.model.Appointment;
import appointment.booking.model.Technician;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    @Query("SELECT a FROM Appointment a WHERE a.technician = :technician AND NOT (a.endTime <= :startTime OR a.startTime >= :endTime)")
    List<Appointment> findAllByTechnicianAndTimeRange(Technician technician, LocalDateTime startTime, LocalDateTime endTime);
}
