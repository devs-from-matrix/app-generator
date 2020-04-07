package packageName.rest

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import packageName.domain.model.ExampleInfo
import packageName.domain.port.RequestExample

@RestController
@RequestMapping("/api/v1/examples")
class ExampleResource(private val requestExample: RequestExample) {

    @GetMapping
    fun getExamples(): ResponseEntity<ExampleInfo> {
        return ResponseEntity.ok(requestExample.getExamples())
    }
}
