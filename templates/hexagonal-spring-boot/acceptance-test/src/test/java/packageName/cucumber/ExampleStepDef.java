package packageName.cucumber;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import io.cucumber.java8.HookNoArgsBody;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import packageName.ExampleE2EApplication;
import packageName.domain.model.Example;
import packageName.domain.model.ExampleInfo;
import packageName.repository.dao.ExampleDao;
import packageName.repository.entity.ExampleEntity;


@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ExampleE2EApplication.class, webEnvironment = RANDOM_PORT)
@ContextConfiguration(classes = ExampleE2EApplication.class, loader = SpringBootContextLoader.class)
public class ExampleStepDef implements En {

  private static final String LOCALHOST = "http://localhost:";
  private static final String API_URI = "/api/v1/examples";
  @LocalServerPort
  private int port;
  private ResponseEntity<ExampleInfo> responseEntity;

  public ExampleStepDef(TestRestTemplate restTemplate, ExampleDao poetryDao) {

    DataTableType((Map<String, String> row) -> Example.builder().description(row.get("description")).build());
    DataTableType((Map<String, String> row) -> ExampleEntity.builder().description(row.get("description")).build());

    Before((HookNoArgsBody) poetryDao::deleteAll);
    After((HookNoArgsBody) poetryDao::deleteAll);

    Given("the following examples exists in the library", (DataTable dataTable) -> {
      List<ExampleEntity> poems = dataTable.asList(ExampleEntity.class);
      poetryDao.saveAll(poems);
    });

    When("user requests for all examples", () -> {
      String url = LOCALHOST + port + API_URI;
      responseEntity = restTemplate.getForEntity(url, ExampleInfo.class);
    });

    Then("the user gets the following examples", (DataTable dataTable) -> {
      List<Example> expectedExampleInfo = dataTable.asList(Example.class);
      assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
      assertThat(responseEntity.getBody()).isNotNull();
      assertThat(responseEntity.getBody().getExamples()).isNotEmpty().extracting("description")
          .contains(expectedExampleInfo.stream().map(Example::getDescription).toArray());
    });
  }
}


