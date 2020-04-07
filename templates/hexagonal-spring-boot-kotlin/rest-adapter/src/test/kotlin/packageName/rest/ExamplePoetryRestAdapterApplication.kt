package packageName.rest

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.ComponentScan
import packageName.domain.port.RequestExample

@SpringBootApplication
@ComponentScan(basePackages = ["packageName"])
class ExamplePoetryRestAdapterApplication {

    @MockBean
    private lateinit var requestExample: RequestExample
}

fun main(args: Array<String>) {
    SpringApplication.run(ExamplePoetryRestAdapterApplication::class.java, *args)
}
