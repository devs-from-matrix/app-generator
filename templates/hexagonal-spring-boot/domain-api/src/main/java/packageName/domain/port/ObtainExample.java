package packageName.domain.port;

import java.util.List;
import packageName.domain.model.Example;

public interface ObtainExample {

  default List<Example> getAllExamples() {
    Example example = Example.builder().id(1L).description("If you could read a leaf or tree\r\nyoud have no need of books.\r\n-- Alistair Cockburn (1987)").build();
    return List.of(example);
  }
}
