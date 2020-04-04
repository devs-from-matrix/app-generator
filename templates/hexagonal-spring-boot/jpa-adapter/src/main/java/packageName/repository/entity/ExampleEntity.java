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
import packageName.domain.model.Example;

@Table(name = "T_EXAMPLE")
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExampleEntity {

  @Id
  @GeneratedValue(strategy = AUTO)
  private Long id;

  @Column(name = "DESCRIPTION")
  private String description;

  public Example toModel() {
    return Example.builder().id(id).description(description).build();
  }
}
