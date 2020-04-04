package packageName;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import packageName.domain.ExampleDomain;
import packageName.domain.model.Example;
import packageName.domain.model.ExampleInfo;
import packageName.domain.port.ObtainExample;
import packageName.domain.port.RequestExample;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
public class AcceptanceTest {

  @Test
  @DisplayName("should be able to get examples when asked for examples from hard coded examples")
  public void getExamplesFromHardCoded() {
  /*
      RequestExample    - left side port
      ExampleDomain     - hexagon (domain)
      ObtainExample     - right side port
   */
    RequestExample requestExample = new ExampleDomain(); // the poem is hard coded
    ExampleInfo exampleInfo = requestExample.getExamples();
    assertThat(exampleInfo).isNotNull();
    assertThat(exampleInfo.getExamples()).isNotEmpty().extracting("description")
        .contains("If you could read a leaf or tree\r\nyoud have no need of books.\r\n-- Alistair Cockburn (1987)");
  }

  @Test
  @DisplayName("should be able to get examples when asked for examples from stub")
  public void getExamplesFromMockedStub(@Mock ObtainExample obtainExample) {
    // Stub
    Example example = Example.builder().id(1L).description("I want to sleep\r\nSwat the flies\r\nSoftly, please.\r\n\r\n-- Masaoka Shiki (1867-1902)").build();
    Mockito.lenient().when(obtainExample.getAllExamples()).thenReturn(List.of(example));
    // hexagon
    RequestExample requestExample = new ExampleDomain(obtainExample);
    ExampleInfo exampleInfo = requestExample.getExamples();
    assertThat(exampleInfo).isNotNull();
    assertThat(exampleInfo.getExamples()).isNotEmpty().extracting("description")
        .contains("I want to sleep\r\nSwat the flies\r\nSoftly, please.\r\n\r\n-- Masaoka Shiki (1867-1902)");
  }
}
