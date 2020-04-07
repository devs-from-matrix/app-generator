package packageName.repository.entity

import packageName.domain.model.Example
import javax.persistence.*
import javax.persistence.GenerationType.AUTO

@Table(name = "T_EXAMPLE")
@Entity
data class ExampleEntity(
        @Id
        @GeneratedValue(strategy = AUTO)
        private val id: Long? = null,
        @Column(name = "DESCRIPTION")
        private val description: String) {
    fun toModel(): Example {
        return Example(id, description)
    }
}
