package project.persistence.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import project.persistence.entity.DVDEntity;

@Repository
public interface DVDRepository extends CrudRepository<DVDEntity, Long> {

    List<DVDEntity> findByTitleLike(String title);

    List<DVDEntity> findAll();
}
