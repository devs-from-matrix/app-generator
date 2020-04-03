package packageName.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import packageName.domain.model.ExamplePoemInfo;
import packageName.domain.port.ExampleRequestVerse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = ExamplePoetryRestAdapterApplication.class, webEnvironment = RANDOM_PORT)
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
public class ExamplePoetryResourceTest {

  private static final String LOCALHOST = "http://localhost:";
  private static final String API_URI = "/api/v1/poems";
  @LocalServerPort
  private int port;

  @Autowired
  private TestRestTemplate restTemplate;

  @Autowired
  private ExampleRequestVerse requestVerse;

  @Test
  @DisplayName("should start the application")
  public void startup() {
    assertThat(Boolean.TRUE).isTrue();
  }

  @Test
  @DisplayName("should give verse when asked for poetry with the support of domain stub")
  public void obtainVerseFromDomainStub() {
    // Given
    Mockito.lenient().when(requestVerse.giveMeSomePoetry()).thenReturn(ExamplePoemInfo.builder().poem("Johnny Johnny Yes Papa !!").build());
    // When
    String url = LOCALHOST + port + API_URI;
    ResponseEntity<ExamplePoemInfo> poemInfoRequestEntity = restTemplate.getForEntity(url, ExamplePoemInfo.class);
    // Then
    assertThat(poemInfoRequestEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(poemInfoRequestEntity.getBody()).isNotNull();
    assertThat(poemInfoRequestEntity.getBody().getPoem()).isEqualTo("Johnny Johnny Yes Papa !!");
  }
}
