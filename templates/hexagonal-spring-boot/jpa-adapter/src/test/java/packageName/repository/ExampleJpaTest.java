package packageName.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import packageName.domain.model.Example;
import packageName.domain.port.ObtainExample;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class ExampleJpaTest {

  @Autowired
  private ObtainExample obtainExample;

  @Test
  @DisplayName("should start the application")
  public void startup() {
    assertThat(Boolean.TRUE).isTrue();
  }

  @Sql(scripts = {"/sql/data.sql"})
  @Test
  @DisplayName("should give me examples when asked for examples from database")
  public void shouldGiveMeExamplesWhenAskedForExamples() {
    // Given from @Sql
    // When
    List<Example> examples = obtainExample.getAllExamples();
    // Then
    assertThat(examples).isNotNull().extracting("description").contains("Twinkle twinkle little star");
  }
}
