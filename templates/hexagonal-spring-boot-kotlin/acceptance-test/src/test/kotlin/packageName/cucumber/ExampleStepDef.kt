package packageName.cucumber

import org.assertj.core.api.Assertions.assertThat
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT

import io.cucumber.datatable.DataTable
import io.cucumber.java8.En
import io.cucumber.java8.HookNoArgsBody
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootContextLoader
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import packageName.ExampleE2EApplication
import packageName.domain.model.Example
import packageName.domain.model.ExampleInfo
import packageName.repository.dao.ExampleDao
import packageName.repository.entity.ExampleEntity


@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [ExampleE2EApplication::class], webEnvironment = RANDOM_PORT)
@ContextConfiguration(classes = [ExampleE2EApplication::class], loader = SpringBootContextLoader::class)
class ExampleStepDef(restTemplate: TestRestTemplate, poetryDao: ExampleDao) : En {

    companion object {
        private const val LOCALHOST = "http://localhost:"
        private const val API_URI = "/api/v1/examples"
    }

    @LocalServerPort
    private val port: Int = 0
    private var responseEntity: ResponseEntity<ExampleInfo>? = null

    init {

        DataTableType { row: Map<String, String> -> Example(1L, row["description"]) }
        DataTableType { row: Map<String, String> -> ExampleEntity(1L, row["description"]) }

        Before(HookNoArgsBody { poetryDao.deleteAll() })
        After(HookNoArgsBody { poetryDao.deleteAll() })

        Given("the following examples exists in the library") { dataTable: DataTable ->
            val poems = dataTable.asList<ExampleEntity>(ExampleEntity::class.java)
            poetryDao.saveAll(poems)
        }

        When("user requests for all examples") {
            val url = LOCALHOST + port + API_URI
            responseEntity = restTemplate.getForEntity(url, ExampleInfo::class.java)
        }

        Then("the user gets the following examples") { dataTable: DataTable ->
            val expectedExampleInfo = dataTable.asList<Example>(Example::class.java)
            assertThat(responseEntity!!.statusCode).isEqualTo(HttpStatus.OK)
            assertThat(responseEntity!!.body).isNotNull
            assertThat(responseEntity!!.body!!.examples).isNotEmpty.extracting("description")
                    .contains(*expectedExampleInfo.stream().map(Example::description).toArray())
        }
    }
}


