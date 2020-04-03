package packageName;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import packageName.domain.ExamplePoetryReader;
import packageName.domain.port.ExampleObtainPoem;
import packageName.domain.port.ExampleRequestVerse;
import packageName.repository.config.JpaAdapterConfig;

@SpringBootApplication
public class ExamplePoetryE2EApplication {

  public static void main(String[] args) {
    SpringApplication.run(ExamplePoetryE2EApplication.class);
  }

  @TestConfiguration
  @Import(JpaAdapterConfig.class)
  static class ExamplePoetryConfig {

    @Bean
    public ExampleRequestVerse getRequestVerse(final ExampleObtainPoem obtainPoem) {
      return new ExamplePoetryReader(obtainPoem);
    }
  }
}
