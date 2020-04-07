package packageName.repository

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.junit.jupiter.SpringExtension
import packageName.domain.model.Example
import packageName.domain.port.ObtainExample

@ExtendWith(SpringExtension::class)
@DataJpaTest
class ExampleJpaTest {

    @Autowired
    private val obtainExample: ObtainExample? = null

    @Test
    fun `should start the application`() {
        assertThat(java.lang.Boolean.TRUE).isTrue()
    }

    @Sql(scripts = ["/sql/data.sql"])
    @Test
    fun `should give me examples when asked for examples from database`() {
        // Given from @Sql
        // When
        val examples = obtainExample!!.getAllExamples()
        // Then
        assertThat(examples).isNotNull.extracting("description").contains("Twinkle twinkle little star")
    }
}
