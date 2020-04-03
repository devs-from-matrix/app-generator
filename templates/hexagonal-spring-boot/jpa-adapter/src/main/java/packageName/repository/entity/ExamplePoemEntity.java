package packageName.repository.entity;

import static javax.persistence.GenerationType.AUTO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import packageName.domain.model.ExamplePoemInfo;

@Table(name = "T_POETRY")
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExamplePoemEntity {

  @Id
  @GeneratedValue(strategy = AUTO)
  private Long id;

  @Column(name = "poem")
  private String poem;

  public ExamplePoemInfo toModel() {
    return ExamplePoemInfo.builder().poem(this.poem).build();
  }
}
