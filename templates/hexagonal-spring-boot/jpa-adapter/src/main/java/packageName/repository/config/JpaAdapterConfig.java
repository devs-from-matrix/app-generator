package packageName.repository.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import packageName.domain.port.ExampleObtainPoem;
import packageName.repository.ExamplePoetryRepository;
import packageName.repository.dao.ExamplePoetryDao;

@Configuration
@EntityScan("packageName.repository.entity")
@EnableJpaRepositories("packageName.repository.dao")
public class JpaAdapterConfig {

  @Bean
  public ExampleObtainPoem getExamplePoetryRepository(ExamplePoetryDao poetryDao) {
    return new ExamplePoetryRepository(poetryDao);
  }
}
