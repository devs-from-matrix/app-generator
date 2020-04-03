package packageName.domain;

import packageName.domain.model.ExamplePoemInfo;
import packageName.domain.port.ExampleObtainPoem;
import packageName.domain.port.ExampleRequestVerse;

public class ExamplePoetryReader implements ExampleRequestVerse {

  private ExampleObtainPoem obtainPoem;

  public ExamplePoetryReader() {
    this(new ExampleObtainPoem() {
    });
  }

  public ExamplePoetryReader(ExampleObtainPoem obtainPoem) {
    this.obtainPoem = obtainPoem;
  }

  @Override
  public ExamplePoemInfo giveMeSomePoetry() {
    return obtainPoem.getMeSomePoetry();
  }
}
