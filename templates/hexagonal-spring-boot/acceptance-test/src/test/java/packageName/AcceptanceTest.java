package packageName;

import static org.junit.jupiter.api.Assertions.assertEquals;

import packageName.domain.ExamplePoetryReader;
import packageName.domain.model.ExamplePoemInfo;
import packageName.domain.port.ExampleObtainPoem;
import packageName.domain.port.ExampleRequestVerse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
public class AcceptanceTest {

  @Test
  @DisplayName("Should be able to get verses when asked for poetry")
  public void getVersesFromHardCodedPoetryReader() {
  /*
      ExampleRequestVerse - left side port
      ExamplePoetryReader - hexagon (domain)
      ExampleObtainPoem   - right side port (hardcoded as it uses
   */
    ExampleRequestVerse poetryReader = new ExamplePoetryReader(); // the poem is hard coded
    ExamplePoemInfo verses = poetryReader.giveMeSomePoetry();
    assertEquals("If you could read a leaf or tree\r\nyoud have no need of books.\r\n-- Alistair Cockburn (1987)", verses.getPoem());
  }

  @Test
  @DisplayName("Should be able to get verses when asked for poetry from a mocked poetry library")
  public void getVersesFromMockedPoetryReader(@Mock ExampleObtainPoem obtainPoem) {
    // Stub
    Mockito.lenient().when(obtainPoem.getMeSomePoetry()).thenReturn(ExamplePoemInfo.builder().poem("I want to sleep\r\nSwat the flies\r\nSoftly, please.\r\n\r\n-- Masaoka Shiki (1867-1902)").build());
    // hexagon
    ExampleRequestVerse poetryReader = new ExamplePoetryReader(obtainPoem);
    ExamplePoemInfo verses = poetryReader.giveMeSomePoetry();
    assertEquals("I want to sleep\r\nSwat the flies\r\nSoftly, please.\r\n\r\n-- Masaoka Shiki (1867-1902)", verses.getPoem());
  }
}
