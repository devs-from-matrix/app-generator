package packageName.repository.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import packageName.domain.port.ObtainExample;
import packageName.repository.ExampleRepository;
import packageName.repository.dao.ExampleDao;

@Configuration
@EntityScan("packageName.repository.entity")
@EnableJpaRepositories("packageName.repository.dao")
public class JpaAdapterConfig {

  @Bean
  public ObtainExample getExampleRepository(ExampleDao exampleDao) {
    return new ExampleRepository(exampleDao);
  }
}
