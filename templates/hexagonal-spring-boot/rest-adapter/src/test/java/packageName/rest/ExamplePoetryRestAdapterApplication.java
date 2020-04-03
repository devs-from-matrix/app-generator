package packageName.rest;

import packageName.domain.port.ExampleRequestVerse;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "packageName")
public class ExamplePoetryRestAdapterApplication {

  public static void main(String[] args) {
    SpringApplication.run(ExamplePoetryRestAdapterApplication.class, args);
  }

  @MockBean
  private ExampleRequestVerse requestVerse;
}
