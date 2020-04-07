package packageName.rest

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import packageName.domain.model.Example
import packageName.domain.model.ExampleInfo
import packageName.domain.port.RequestExample

@ExtendWith(MockitoExtension::class)
@SpringBootTest(classes = [ExamplePoetryRestAdapterApplication::class], webEnvironment = RANDOM_PORT)
@EnableAutoConfiguration(exclude = [DataSourceAutoConfiguration::class])
class ExampleResourceTest {
    companion object {
        private const val LOCALHOST = "http://localhost:"
        private const val API_URI = "/api/v1/examples"
    }

    @LocalServerPort
    private val port: Int = 0
    @Autowired
    private lateinit var restTemplate: TestRestTemplate
    @Autowired
    private lateinit var requestExample: RequestExample

    @Test
    fun `should start the rest adapter application`() {
        assertThat(java.lang.Boolean.TRUE).isTrue()
    }

    @Test
    fun `should give examples when asked for examples with the support of domain stub`() {
        // Given
        Mockito.lenient().`when`(requestExample!!.getExamples()).thenReturn(mockExampleInfo())
        // When
        val url = LOCALHOST + port + API_URI
        val responseEntity = restTemplate!!.getForEntity(url, ExampleInfo::class.java)
        // Then
        assertThat(responseEntity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(responseEntity.body).isNotNull
        assertThat(responseEntity.body.examples).isNotEmpty.extracting("description").contains("Johnny Johnny Yes Papa !!")
    }

    private fun mockExample(id: Long, description: String): Example {
        return Example(id, description)
    }

    private fun mockExampleInfo(): ExampleInfo {
        return ExampleInfo(listOf(mockExample(1L, "Johnny Johnny Yes Papa !!")))
    }
}
