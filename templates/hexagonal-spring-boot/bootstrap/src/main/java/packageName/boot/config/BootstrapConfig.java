package packageName.boot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import packageName.domain.ExampleDomain;
import packageName.domain.port.ObtainExample;
import packageName.domain.port.RequestExample;
import packageName.repository.config.JpaAdapterConfig;

@Configuration
@Import(JpaAdapterConfig.class)
public class BootstrapConfig {

  @Bean
  public RequestExample getRequestExample(ObtainExample obtainExample) {
    return new ExampleDomain(obtainExample);
  }
}
