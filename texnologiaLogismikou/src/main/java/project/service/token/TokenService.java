package project.service.token;

import java.util.Optional;
import project.persistence.entity.TokenEntity;

public interface TokenService {
    TokenEntity createTokenForUser(String username, String password);

    TokenEntity save(TokenEntity tokenEntity);

    void deleteTokenForUser(String userName);

    Optional<TokenEntity> findByTokenCode(String tokenCode);
}
