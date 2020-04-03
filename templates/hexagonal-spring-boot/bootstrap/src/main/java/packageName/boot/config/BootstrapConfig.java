package packageName.boot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import packageName.domain.ExamplePoetryReader;
import packageName.domain.port.ExampleObtainPoem;
import packageName.domain.port.ExampleRequestVerse;
import packageName.repository.config.JpaAdapterConfig;

@Configuration
@Import(JpaAdapterConfig.class)
public class BootstrapConfig {

  @Bean
  public ExampleRequestVerse getRequestVerse(ExampleObtainPoem obtainPoem) {
    return new ExamplePoetryReader(obtainPoem);
  }
}
