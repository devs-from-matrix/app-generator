package packageName.repository;

import packageName.domain.model.ExamplePoemInfo;
import packageName.domain.port.ExampleObtainPoem;
import packageName.repository.dao.ExamplePoetryDao;

public class ExamplePoetryRepository implements ExampleObtainPoem {

  private ExamplePoetryDao poetryDao;

  public ExamplePoetryRepository(ExamplePoetryDao poetryDao) {
    this.poetryDao = poetryDao;
  }

  @Override
  public ExamplePoemInfo getMeSomePoetry() {
    return poetryDao.findAll().get(0).toModel();
  }
}
