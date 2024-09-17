package appointment.booking.service;

import appointment.booking.dto.BeautyServiceDTO;
import appointment.booking.mapping.BeautyServiceMapper;
import appointment.booking.model.BeautyService;
import appointment.booking.repository.BeautyServiceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class BeautyServiceService {
    public static final String SERVICE_WITH_ID_NOT_FOUND = "Service not found with ID: ";
    private final BeautyServiceRepository serviceRepository;
    private final BeautyServiceMapper serviceMapper;

    public BeautyServiceService(BeautyServiceRepository serviceRepository, BeautyServiceMapper serviceMapper) {
        this.serviceRepository = serviceRepository;
        this.serviceMapper = serviceMapper;
    }

    public List<BeautyServiceDTO> getAllServices() {
        return serviceRepository.findAll().stream().map(serviceMapper::toDTO).collect(Collectors.toList());
    }

    public BeautyServiceDTO createService(BeautyServiceDTO serviceDTO) {
        BeautyService service = serviceMapper.toEntity(serviceDTO);
        return serviceMapper.toDTO(serviceRepository.save(service));
    }

    public BeautyServiceDTO getServiceById(Long id) {
        return serviceRepository.findById(id).map(serviceMapper::toDTO).orElseThrow(() -> new IllegalArgumentException(SERVICE_WITH_ID_NOT_FOUND + id));
    }
}
