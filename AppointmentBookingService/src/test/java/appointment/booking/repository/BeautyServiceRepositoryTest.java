package appointment.booking.repository;

import appointment.booking.model.BeautyService;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.Duration;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class BeautyServiceRepositoryTest {
    private final Faker faker = new Faker();
    @Autowired
    private BeautyServiceRepository beautyServiceRepository;
    private BeautyService beautyService;

    @BeforeEach
    void setUp() {
        beautyService = new BeautyService();
        beautyService.setServiceName(faker.commerce().productName());
        beautyService.setDuration(Duration.ofMinutes(faker.number().numberBetween(30, 120)));
        beautyService.setPrice(faker.number().randomDouble(2, 20, 100));
        beautyService = beautyServiceRepository.save(beautyService);
    }

    @Test
    void testSaveBeautyService() {
        BeautyService newService = createBeautyService();
        assertThat(newService).isNotNull();
        assertThat(newService.getIdService()).isNotNull();
        assertThat(newService.getServiceName()).isEqualTo(newService.getServiceName());
    }

    @Test
    void saveAndFindBeautyServiceById() {
        BeautyService saved = createBeautyService();
        Optional<BeautyService> foundService = beautyServiceRepository.findById(beautyService.getIdService());
        assertEquals(saved.getServiceName(), foundService.get().getServiceName());
        assertEquals(saved.getPrice(), foundService.get().getPrice());

    }

    @Test
    void testFindAllBeautyServices() {
        Iterable<BeautyService> services = beautyServiceRepository.findAll();
        assertThat(services).isNotEmpty();
        assertThat(services).contains(beautyService);
    }

    @Test
    void testDeleteBeautyService() {
        beautyServiceRepository.delete(beautyService);
        Optional<BeautyService> deletedService = beautyServiceRepository.findById(beautyService.getIdService());
        assertThat(deletedService).isNotPresent();
    }

    private BeautyService createBeautyService()
    {
        BeautyService newService = new BeautyService();
        newService.setServiceName(faker.commerce().productName());
        newService.setDuration(Duration.ofMinutes(faker.number().numberBetween(30, 120)));
        newService.setPrice(faker.number().randomDouble(2, 20, 100));
        return beautyServiceRepository.save(newService);
    }
}
