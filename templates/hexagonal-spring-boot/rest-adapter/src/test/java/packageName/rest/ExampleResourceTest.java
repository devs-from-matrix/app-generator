package packageName.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import java.util.List;
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
import packageName.domain.model.Example;
import packageName.domain.model.ExampleInfo;
import packageName.domain.port.RequestExample;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = ExamplePoetryRestAdapterApplication.class, webEnvironment = RANDOM_PORT)
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
public class ExampleResourceTest {

  private static final String LOCALHOST = "http://localhost:";
  private static final String API_URI = "/api/v1/examples";
  @LocalServerPort
  private int port;
  @Autowired
  private TestRestTemplate restTemplate;
  @Autowired
  private RequestExample requestExample;

  @Test
  @DisplayName("should start the rest adapter application")
  public void startup() {
    assertThat(Boolean.TRUE).isTrue();
  }

  @Test
  @DisplayName("should give examples when asked for examples with the support of domain stub")
  public void obtainExamplesFromDomainStub() {
    // Given
    Example example = Example.builder().id(1L).description("Johnny Johnny Yes Papa !!").build();
    Mockito.lenient().when(requestExample.getExamples()).thenReturn(ExampleInfo.builder().examples(List.of(example)).build());
    // When
    String url = LOCALHOST + port + API_URI;
    ResponseEntity<ExampleInfo> responseEntity = restTemplate.getForEntity(url, ExampleInfo.class);
    // Then
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(responseEntity.getBody()).isNotNull();
    assertThat(responseEntity.getBody().getExamples()).isNotEmpty().extracting("description")
        .contains("Johnny Johnny Yes Papa !!");
  }
}
