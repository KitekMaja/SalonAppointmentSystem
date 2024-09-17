package appointment.booking.service;

import appointment.booking.dto.TechnicianDTO;
import appointment.booking.mapping.TechnicianMapper;
import appointment.booking.model.Technician;
import appointment.booking.repository.TechnicianRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TechnicianService {
    public static final String TECHNICIAN_WITH_ID_NOT_FOUND = "Technician not found with ID: ";
    private final TechnicianRepository technicianRepository;
    private final TechnicianMapper technicianMapper;

    public TechnicianService(TechnicianRepository technicianRepository, TechnicianMapper technicianMapper) {
        this.technicianRepository = technicianRepository;
        this.technicianMapper = technicianMapper;
    }

    public TechnicianDTO createTechnician(TechnicianDTO technicianDTO) {
        Technician technician = technicianMapper.toEntity(technicianDTO);
        if (technicianDTO.getAppointments() != null) {
            technician.setAppointments(technicianDTO.getAppointments());
        }
        return technicianMapper.toDTO(technicianRepository.save(technician));
    }

    public TechnicianDTO getTechnicianById(Long id) {
        Technician technician = technicianRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(TECHNICIAN_WITH_ID_NOT_FOUND + id));
        return technicianMapper.toDTO(technician);
    }

    public List<TechnicianDTO> getAllTechnicians() {
        return technicianRepository.findAll().stream().map(technicianMapper::toDTO).collect(Collectors.toList());
    }

    public TechnicianDTO updateTechnician(Long id, TechnicianDTO technicianDTO) {
        Technician technician = technicianRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(TECHNICIAN_WITH_ID_NOT_FOUND + id));
        technician.setName(technicianDTO.getName());
        technician.setSpecialty(technicianDTO.getSpecialty());
        if (technicianDTO.getAppointments() != null) {
            technician.setAppointments(technicianDTO.getAppointments());
        }
        return technicianMapper.toDTO(technicianRepository.save(technician));
    }

}
