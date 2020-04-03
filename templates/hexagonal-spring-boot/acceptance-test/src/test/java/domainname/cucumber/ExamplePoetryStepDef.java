package domainname.cucumber;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import domainname.ExamplePoetryE2EApplication;
import domainname.domain.model.ExamplePoemInfo;
import domainname.domain.port.ExampleObtainPoem;
import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ExamplePoetryE2EApplication.class, webEnvironment = RANDOM_PORT)
@ContextConfiguration(classes = ExamplePoetryE2EApplication.class, loader = SpringBootContextLoader.class)
public class ExamplePoetryStepDef implements En {

  private static final String LOCALHOST = "http://localhost:";
  private static final String API_URI = "/api/v1/poems";

  @LocalServerPort
  private int port;

  private ResponseEntity<ExamplePoemInfo> responseEntity;

  public ExamplePoetryStepDef(ExampleObtainPoem obtainPoem, TestRestTemplate restTemplate) {

    DataTableType((Map<String, String> row) -> ExamplePoemInfo.builder().poem(row.get("poem")).build());

    Before(() -> {
      // anything to do before ?
    });

    Given("the following poem exists in the library", (DataTable dataTable) -> {
      List<ExamplePoemInfo> examplePoemInfos = dataTable.asList(ExamplePoemInfo.class);
      Mockito.lenient().when(obtainPoem.getMeSomePoetry()).thenReturn(examplePoemInfos.get(0));
    });

    When("user requests for verses", () -> {
      String url = LOCALHOST + port + API_URI;
      responseEntity = restTemplate.getForEntity(url, ExamplePoemInfo.class);
    });

    Then("the user gets the following verse from poetry library", (DataTable dataTable) -> {
      List<ExamplePoemInfo> expectedExamplePoemInfos = dataTable.asList(ExamplePoemInfo.class);
      ExamplePoemInfo expectedExamplePoemInfo = expectedExamplePoemInfos.get(0);
      assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
      assertThat(responseEntity.getBody()).isNotNull();
      assertThat(responseEntity.getBody().getPoem()).isEqualTo(expectedExamplePoemInfo.getPoem());
    });
  }
}


