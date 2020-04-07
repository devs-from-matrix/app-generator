package packageName.domain.port

import packageName.domain.model.Example

interface ObtainExample {

    fun getAllExamples(): List<Example> {
        val example = Example(1L, "If you could read a leaf or tree\r\nyoud have no need of books.\r\n-- Alistair Cockburn (1987)")
        return listOf(example)
    }
}
