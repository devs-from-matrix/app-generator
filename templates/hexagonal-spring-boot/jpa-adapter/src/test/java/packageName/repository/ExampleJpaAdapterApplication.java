package packageName.repository;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import packageName.domain.port.ObtainExample;
import packageName.repository.dao.ExampleDao;

@SpringBootApplication
public class ExampleJpaAdapterApplication {

  public static void main(String[] args) {
    SpringApplication.run(ExampleJpaAdapterApplication.class, args);
  }

  @TestConfiguration
  static class ExampleJpaTestConfig {

    @Bean
    public ObtainExample getObtainExampleRepository(ExampleDao exampleDao) {
      return new ExampleRepository(exampleDao);
    }
  }
}
