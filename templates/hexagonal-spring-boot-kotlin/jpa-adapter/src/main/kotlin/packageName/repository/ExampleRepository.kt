package packageName.repository

import packageName.domain.model.Example
import packageName.domain.port.ObtainExample
import packageName.repository.dao.ExampleDao

class ExampleRepository(private val exampleDao: ExampleDao) : ObtainExample {

    override fun getAllExamples(): List<Example> {
        return exampleDao.findAll().map { it.toModel() }
    }
}
