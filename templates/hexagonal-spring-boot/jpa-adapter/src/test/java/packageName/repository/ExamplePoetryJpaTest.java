package packageName.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import packageName.domain.model.ExamplePoemInfo;
import packageName.domain.port.ExampleObtainPoem;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class ExamplePoetryJpaTest {

  @Autowired
  private ExampleObtainPoem obtainPoem;

  @Test
  @DisplayName("should start the application")
  public void startup() {
    assertThat(Boolean.TRUE).isTrue();
  }

  @Sql(scripts = {"/sql/data.sql"})
  @Test
  public void shouldGiveMePoemWhenAskedForPoetry() {
    // Given from @Sql
    // When
    ExamplePoemInfo actualPoem = obtainPoem.getMeSomePoetry();
    // Then
    assertThat(actualPoem).isNotNull().extracting("poem").isEqualTo("Twinkle twinkle little star");
  }
}
