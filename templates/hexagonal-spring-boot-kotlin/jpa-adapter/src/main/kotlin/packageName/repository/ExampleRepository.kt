package packageName.repository

import java.util.stream.Collectors
import packageName.domain.model.Example
import packageName.domain.port.ObtainExample
import packageName.repository.dao.ExampleDao
import packageName.repository.entity.ExampleEntity

class ExampleRepository(private val exampleDao: ExampleDao) : ObtainExample {

    override fun getAllExamples(): List<Example> {
        return exampleDao.findAll().stream().map(ExampleEntity::toModel).collect(Collectors.toList());
    }
}
