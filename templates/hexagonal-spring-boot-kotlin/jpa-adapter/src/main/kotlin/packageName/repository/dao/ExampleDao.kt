package packageName.repository.dao

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import packageName.repository.entity.ExampleEntity

@Repository
interface ExampleDao : JpaRepository<ExampleEntity, Long>
