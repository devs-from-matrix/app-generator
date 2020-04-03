package packageName.rest;

import packageName.domain.model.ExamplePoemInfo;
import packageName.domain.port.ExampleRequestVerse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/poems")
public class ExamplePoetryResource {

  private ExampleRequestVerse requestVerse;

  public ExamplePoetryResource(ExampleRequestVerse requestVerse) {
    this.requestVerse = requestVerse;
  }

  @GetMapping
  public ResponseEntity<ExamplePoemInfo> getPoems() {
    return ResponseEntity.ok(requestVerse.giveMeSomePoetry());
  }
}
