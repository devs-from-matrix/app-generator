package packageName.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import packageName.domain.model.ExampleInfo;
import packageName.domain.port.RequestExample;

@RestController
@RequestMapping("/api/v1/examples")
public class ExampleResource {

  private RequestExample requestExample;

  public ExampleResource(RequestExample requestExample) {
    this.requestExample = requestExample;
  }

  @GetMapping
  public ResponseEntity<ExampleInfo> getExamples() {
    return ResponseEntity.ok(requestExample.getExamples());
  }
}
