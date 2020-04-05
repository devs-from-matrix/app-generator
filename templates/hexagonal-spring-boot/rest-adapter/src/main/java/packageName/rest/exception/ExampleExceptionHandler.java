package packageName.rest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import packageName.domain.exception.ExampleNotFoundException;

@RestControllerAdvice(basePackages = {"packageName"})
public class ExampleExceptionHandler {

  @ExceptionHandler(value = ExampleNotFoundException.class)
  public final ResponseEntity<ExampleExceptionResponse> handleExampleNotFoundException(final WebRequest request) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ExampleExceptionResponse.builder().message("Example not found").path(((ServletWebRequest) request).getRequest().getRequestURI()).build());
  }
}
