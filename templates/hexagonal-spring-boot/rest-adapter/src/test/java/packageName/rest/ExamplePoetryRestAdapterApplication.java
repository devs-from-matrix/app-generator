package packageName.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import packageName.domain.port.RequestExample;

@SpringBootApplication
@ComponentScan(basePackages = "packageName")
public class ExamplePoetryRestAdapterApplication {

  public static void main(String[] args) {
    SpringApplication.run(ExamplePoetryRestAdapterApplication.class, args);
  }

  @MockBean
  private RequestExample requestExample;
}
