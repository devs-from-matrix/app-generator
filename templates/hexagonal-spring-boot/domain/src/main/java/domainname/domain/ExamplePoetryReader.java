package domainname.domain;

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
  public String giveMeSomePoetry() {
    return obtainPoem.getMeSomePoetry();
  }
}
