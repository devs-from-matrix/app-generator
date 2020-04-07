package packageName.domain

import packageName.domain.model.ExampleInfo
import packageName.domain.port.ObtainExample
import packageName.domain.port.RequestExample

class ExampleDomain(private val obtainExample: ObtainExample) : RequestExample {

    constructor() : this(object : ObtainExample {})

    override fun getExamples(): ExampleInfo {
        return ExampleInfo(obtainExample.getAllExamples())
    }
}
