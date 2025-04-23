package co.spribe;

import co.spribe.model.Unit;
import co.spribe.model.User;
import co.spribe.model.enums.AccommodationType;
import co.spribe.repository.PaymentRepository;
import co.spribe.repository.UnitRepository;
import co.spribe.repository.UserRepository;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import java.time.LocalDateTime;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.Transactional;

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
public class BookingApplication implements CommandLineRunner {

  public static final int UNITS_NUMBER_ON_START = 10;

  @Autowired
  private UnitRepository unitRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PaymentRepository paymentRepository;

  public static void main(String[] args) {
    SpringApplication.run(BookingApplication.class, args);
  }

  @Bean
  public OpenAPI springShopOpenAPI() {
    return new OpenAPI()
        .info(new Info().title("EIS API")
            .description("Employee Information System sample application")
            .version("v0.0.1")
            .license(new License().name("Apache 2.0").url("http://springdoc.org"))
            .description("SpringShop Wiki Documentation")
            .contact(new Contact().email("test@test.com").url("http://fullstackcode.dev")));
  }

  @Override
  @Transactional
  public void run(String... args) {
    Random random = new Random(System.currentTimeMillis());
    User user = userRepository.save(User.builder().username("user" + random.nextInt()).build());
    for (int i = 0; i < UNITS_NUMBER_ON_START; i++) {
      unitRepository.save(
          Unit.builder()
              .user(user)
              .numberOfRooms(random.nextInt())
              .unitFlor(random.nextInt())
              .accommodationType(AccommodationType.HOME)
              .availableStartDate(LocalDateTime.now())
              .availableEndDate(LocalDateTime.now())
              .booked(true)
              .build());
    }
  }
}
