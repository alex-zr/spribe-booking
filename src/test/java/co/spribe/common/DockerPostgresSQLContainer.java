package co.spribe.common;

import lombok.extern.slf4j.Slf4j;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@Slf4j
public class DockerPostgresSQLContainer extends PostgreSQLContainer<DockerPostgresSQLContainer> {

  private static final String IMAGE_NAME = "postgres:11.1";
  private static final String DATABASE_NAME = "soa_it";
  private static final String DB_USERNAME = "username";
  private static final String DB_PASSWORD = "password";
  private static final String SCHEMA_NAME = "public";
  private static final String CURRENT_SCHEMA_URL_PARAM = "currentSchema";

  private static DockerPostgresSQLContainer container;

  private DockerPostgresSQLContainer() {
    super(DockerImageName.parse(IMAGE_NAME).asCompatibleSubstituteFor("postgres"));
  }

  public static DockerPostgresSQLContainer getInstance() {
    if (container == null) {
      log.info("Creating container for database, image: {}", IMAGE_NAME);
      container = new DockerPostgresSQLContainer()
          .withDatabaseName(DATABASE_NAME)
          .withUsername(DB_USERNAME)
          .withPassword(DB_PASSWORD)
          .withUrlParam(CURRENT_SCHEMA_URL_PARAM, SCHEMA_NAME);
    } else {
      log.info("Re-using already created container for database, image: {}", IMAGE_NAME);
    }

    return container;
  }

  @Override
  public void stop() {
    //NOP let JVM to handle stop
  }
}
