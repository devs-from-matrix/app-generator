package domainname.domain;

import domainname.domain.model.ExamplePoemInfo;
import domainname.domain.port.ExampleObtainPoem;
import domainname.domain.port.ExampleRequestVerse;

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
