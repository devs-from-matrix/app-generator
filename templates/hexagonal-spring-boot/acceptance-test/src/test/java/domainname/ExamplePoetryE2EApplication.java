package domainname;

import domainname.domain.ExamplePoetryReader;
import domainname.domain.port.ExampleObtainPoem;
import domainname.domain.port.ExampleRequestVerse;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class ExamplePoetryE2EApplication {

  public static void main(String[] args) {
    SpringApplication.run(ExamplePoetryE2EApplication.class);
  }

  @TestConfiguration
  @ComponentScan(basePackages = "domainname")
  static class ExamplePoetryConfig {

    @MockBean
    private ExampleObtainPoem obtainPoem;

    @Bean
    public ExampleRequestVerse getRequestVerse() {
      return new ExamplePoetryReader(obtainPoem);
    }
  }
}
