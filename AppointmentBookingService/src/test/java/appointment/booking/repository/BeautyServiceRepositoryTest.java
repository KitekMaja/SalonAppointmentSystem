package appointment.booking.repository;

import appointment.booking.model.BeautyService;
import net.datafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class BeautyServiceRepositoryTest {
    private final Faker faker = new Faker();
    @Autowired
    private BeautyServiceRepository beautyServiceRepository;

    @Test
    @Rollback
    void saveAndFindBeautyServiceById() {
        BeautyService saved = createBeautyService();
        Optional<BeautyService> foundService = beautyServiceRepository.findById(saved.getIdService());
        assertEquals(saved.getServiceName(), foundService.get().getServiceName());
        assertEquals(saved.getDuration(), foundService.get().getDuration());
        assertEquals(saved.getPrice(), foundService.get().getPrice());
    }

    @Test
    @Rollback
    void findAllBeautyServices() {
        BeautyService beautyService1 = createBeautyService();
        BeautyService beautyService2 = createBeautyService();
        List<BeautyService> createdServices = List.of(beautyService2, beautyService1);
        List<BeautyService> foundServices = beautyServiceRepository.findAll();
        assertThat(foundServices).contains(beautyService1);
        assertThat(foundServices).contains(beautyService2);
        assertEquals(createdServices.size(), foundServices.size());
    }

    @Test
    @Rollback
    void updateBeautyServiceTest() {
        BeautyService beautyService = createBeautyService();
        Duration updatedDuration = Duration.ofMinutes(faker.number().numberBetween(30, 120));
        beautyService.setDuration(updatedDuration);
        BeautyService updatedService = beautyServiceRepository.save(beautyService);
        assertEquals(updatedDuration, updatedService.getDuration());
        assertEquals(beautyService.getServiceName(), updatedService.getServiceName());
    }

    @Test
    void testDeleteBeautyService() {
        BeautyService beautyService = createBeautyService();
        beautyServiceRepository.delete(beautyService);
        Optional<BeautyService> deletedService = beautyServiceRepository.findById(beautyService.getIdService());
        assertThat(deletedService).isNotPresent();
    }

    private BeautyService createBeautyService() {
        BeautyService newService = new BeautyService();
        newService.setServiceName(faker.commerce().productName());
        newService.setDuration(Duration.ofMinutes(faker.number().numberBetween(30, 120)));
        newService.setPrice(faker.number().randomDouble(2, 20, 100));
        return beautyServiceRepository.save(newService);
    }
}
