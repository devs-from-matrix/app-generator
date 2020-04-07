package packageName.rest.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.ServletWebRequest
import org.springframework.web.context.request.WebRequest
import packageName.domain.exception.ExampleNotFoundException

@RestControllerAdvice(basePackages = ["packageName"])
class ExampleExceptionHandler {

    @ExceptionHandler(value = [ExampleNotFoundException::class])
    fun handleExampleNotFoundException(request: WebRequest): ResponseEntity<ExampleExceptionResponse> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ExampleExceptionResponse("Example not found", (request as ServletWebRequest).request.requestURI))
    }
}
