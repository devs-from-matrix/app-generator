package domainname.domain.port;

import domainname.model.ExamplePoemInfo;

public interface ExampleObtainPoem {

  default ExamplePoemInfo getMeSomePoetry() {
    return ExamplePoemInfo.builder().poem("If you could read a leaf or tree\r\nyoud have no need of books.\r\n-- Alistair Cockburn (1987)").build();
  }
}
