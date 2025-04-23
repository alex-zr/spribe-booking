package co.spribe.common;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.google.common.io.Resources;

@Testcontainers
@DirtiesContext
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringJUnitConfig(initializers = {ITBase.Initialization.class})
@TestPropertySource(properties =
    "spring.autoconfigure.exclude=org.springframework.boot.actuate.autoconfigure.tracing.zipkin.ZipkinAutoConfiguration")
public class ITBase {

  private static final String DB_CHANGELOG_FILE = "db.changelog/changelog-v1.0.xml";

  @Container
  private static final DockerPostgresSQLContainer POSTGRES_SQL_CONTAINER = DockerPostgresSQLContainer.getInstance();

  public static class Initialization implements
      ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
      checkDBMigrationFileAvailable();

      TestPropertyValues values = TestPropertyValues.of(
          "spring.datasource.url=" + POSTGRES_SQL_CONTAINER.getJdbcUrl(),
          "spring.datasource.username=" + POSTGRES_SQL_CONTAINER.getUsername(),
          "spring.datasource.password=" + POSTGRES_SQL_CONTAINER.getPassword(),
          "spring.liquibase.change-log=" + "classpath:" + DB_CHANGELOG_FILE,
          """
              spring.liquibase.contexts=\
              test\
              """,
          "spring.datasource.hikari.maximum-pool-size=" + 5,
          "spring.datasource.hikari.minimum-idle=" + 2
      );
      values.applyTo(configurableApplicationContext);
    }

    private void checkDBMigrationFileAvailable() {
      Resources.getResource(DB_CHANGELOG_FILE);
    }
  }
}
