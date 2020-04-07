package packageName

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import packageName.domain.ExampleDomain
import packageName.domain.model.Example
import packageName.domain.model.ExampleInfo
import packageName.domain.port.ObtainExample
import packageName.domain.port.RequestExample

@ExtendWith(MockitoExtension::class)
@RunWith(JUnitPlatform::class)
class AcceptanceTest {

    @Test
    fun `should be able to get examples when asked for examples from hard coded examples`() {
        /*
      RequestExample    - left side port
      ExampleDomain     - hexagon (domain)
      ObtainExample     - right side port
   */
        val requestExample = ExampleDomain() // the poem is hard coded
        val exampleInfo = requestExample.getExamples()
        assertThat(exampleInfo).isNotNull
        assertThat(exampleInfo.examples).isNotEmpty.extracting("description")
                .contains("If you could read a leaf or tree\r\nyoud have no need of books.\r\n-- Alistair Cockburn (1987)")
    }

    @Test
    fun `should be able to get examples when asked for examples from stub`(@Mock obtainExample: ObtainExample) {
        // Stub
        val example = Example(1L, "I want to sleep\r\nSwat the flies\r\nSoftly, please.\r\n\r\n-- Masaoka Shiki (1867-1902)")
        Mockito.lenient().`when`(obtainExample.getAllExamples()).thenReturn(listOf(example))
        // hexagon
        val requestExample = ExampleDomain(obtainExample)
        val exampleInfo = requestExample.getExamples()
        assertThat(exampleInfo).isNotNull
        assertThat(exampleInfo.examples).isNotEmpty.extracting("description")
                .contains("I want to sleep\r\nSwat the flies\r\nSoftly, please.\r\n\r\n-- Masaoka Shiki (1867-1902)")
    }
}
