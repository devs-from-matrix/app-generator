package domainname.rest.exception;

import domainname.exception.ExamplePoemNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice(basePackages = {"domainname"})
public class ExampleExceptionHandler {

  @ExceptionHandler(value = ExamplePoemNotFoundException.class)
  public final ResponseEntity<ExampleExceptionResponse> handlePoemNotFoundException(final WebRequest request) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ExampleExceptionResponse.builder().message("Poem not found").path(((ServletWebRequest) request).getRequest().getRequestURI()).build());
  }
}
