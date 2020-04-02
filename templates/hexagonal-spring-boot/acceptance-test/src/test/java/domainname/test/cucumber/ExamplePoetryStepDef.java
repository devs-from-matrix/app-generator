package domainname.test.cucumber;

import domainname.domain.ExamplePoetryReader;
import domainname.domain.port.ExampleObtainPoem;
import domainname.domain.port.ExampleRequestVerse;
import domainname.model.ExamplePoemInfo;
import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class ExamplePoetryStepDef implements En {

  private ExampleRequestVerse poetryReader;
  private ExamplePoemInfo actualPoem;
  @Mock
  private ExampleObtainPoem obtainPoem;

  public ExamplePoetryStepDef() {

    DataTableType((Map<String, String> row) -> ExamplePoemInfo.builder().poem(row.get("poem")).build());

    Before(() -> {
      MockitoAnnotations.initMocks(this);
      poetryReader = new ExamplePoetryReader(obtainPoem);
    });

    Given("the following poem exists in the library", (DataTable dataTable) -> {
      List<ExamplePoemInfo> examplePoemInfos = dataTable.asList(ExamplePoemInfo.class);
      Mockito.lenient().when(obtainPoem.getMeSomePoetry()).thenReturn(examplePoemInfos.get(0));
    });

    When("user requests for verses", () -> {
      actualPoem = poetryReader.giveMeSomePoetry();
    });

    Then("the user gets the following verse from poetry library", (DataTable dataTable) -> {
      List<ExamplePoemInfo> expectedExamplePoemInfos = dataTable.asList(ExamplePoemInfo.class);
      Assertions.assertEquals(expectedExamplePoemInfos.get(0), this.actualPoem);
    });
  }
}


