package packageName.repository;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import packageName.domain.port.ExampleObtainPoem;
import packageName.repository.dao.ExamplePoetryDao;

@SpringBootApplication
public class ExamplePoetryJpaAdapterApplication {

  public static void main(String[] args) {
    SpringApplication.run(ExamplePoetryJpaAdapterApplication.class, args);
  }

  @TestConfiguration
  static class ExamplePoetryJpaTestConfig {

    @Bean
    public ExampleObtainPoem getObtainPoemRepository(ExamplePoetryDao poetryDao) {
      return new ExamplePoetryRepository(poetryDao);
    }
  }
}
