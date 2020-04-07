package packageName.repository.entity

import packageName.domain.model.Example
import javax.persistence.GenerationType.AUTO

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Table(name = "T_EXAMPLE")
@Entity
data class ExampleEntity(
        @Id
        @GeneratedValue(strategy = AUTO)
        private val id: Long? = null,
        @Column(name = "DESCRIPTION")
        private val description: String? = null) {
    fun toModel(): Example {
        return Example(id, description)
    }
}
