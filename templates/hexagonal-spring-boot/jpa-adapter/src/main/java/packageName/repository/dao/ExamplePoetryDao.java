package packageName.repository.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import packageName.repository.entity.ExamplePoemEntity;

@Repository
public interface ExamplePoetryDao extends JpaRepository<ExamplePoemEntity, Long> {

}
