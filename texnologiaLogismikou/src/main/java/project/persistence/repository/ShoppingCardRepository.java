package project.persistence.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import project.persistence.entity.ShoppingCardEntity;

@Repository
public interface ShoppingCardRepository extends CrudRepository<ShoppingCardEntity, Long> {
    List<ShoppingCardEntity> findByCustomerUserName(String customerUserName);

    List<ShoppingCardEntity> findByStatus(String status);
}
