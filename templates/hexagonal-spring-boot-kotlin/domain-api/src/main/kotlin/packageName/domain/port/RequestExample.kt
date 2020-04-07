package packageName.domain.port

import packageName.domain.model.ExampleInfo

interface RequestExample {
    fun getExamples(): ExampleInfo
}
