package domainname.domain.port;

public interface ExampleObtainPoem {
  default String getMeSomePoetry() {
    return "If you could read a leaf or tree\r\nyoud have no need of books.\r\n-- Alistair Cockburn (1987)";
  }
}
