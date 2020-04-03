package packageName.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "packageName")
public class ExamplePoetryApplication {

  public static void main(String[] args) {
    SpringApplication.run(ExamplePoetryApplication.class, args);
  }
}
