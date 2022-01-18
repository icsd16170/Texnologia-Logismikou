package project.persistence.repository;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import project.persistence.entity.TokenEntity;

@Repository
public interface TokenRepository extends CrudRepository<TokenEntity, String> {

    Optional<TokenEntity> findByUserName(String userName);

    Optional<TokenEntity> findByTokenCode(String tokenCode);

    boolean existsByUserName(String userName);

    void deleteByUserName(String UserName);
}
