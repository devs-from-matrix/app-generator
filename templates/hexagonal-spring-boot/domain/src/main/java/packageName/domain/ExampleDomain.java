package packageName.domain;

import packageName.domain.model.ExampleInfo;
import packageName.domain.port.ObtainExample;
import packageName.domain.port.RequestExample;

public class ExampleDomain implements RequestExample {

  private ObtainExample obtainExample;

  public ExampleDomain() {
    this(new ObtainExample() {
    });
  }

  public ExampleDomain(ObtainExample obtainExample) {
    this.obtainExample = obtainExample;
  }

  @Override
  public ExampleInfo getExamples() {
    return ExampleInfo.builder().examples(obtainExample.getAllExamples()).build();
  }
}
