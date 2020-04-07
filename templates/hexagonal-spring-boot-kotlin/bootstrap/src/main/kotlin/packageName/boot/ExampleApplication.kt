package packageName.boot

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(basePackages = ["packageName"])
class ExampleApplication {
}

fun main(args: Array<String>) {
    SpringApplication.run(ExampleApplication::class.java, *args)
}
