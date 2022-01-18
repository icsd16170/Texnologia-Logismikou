package project.service.token;

import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.persistence.entity.TokenEntity;
import project.persistence.repository.TokenRepository;
import project.service.encryption.EncryptionService;

@Service
public class TokenServiceImpl implements TokenService {

    private static final int ONE_HOUR = 60;

    private final EncryptionService encryptionService;

    private final TokenRepository tokenRepository;

    public TokenServiceImpl(EncryptionService encryptionService, TokenRepository tokenRepository) {
        this.encryptionService = encryptionService;
        this.tokenRepository = tokenRepository;
    }

    @Override
    public TokenEntity createTokenForUser(String username, String password) {
        TokenEntity tokenEntity = new TokenEntity();
        tokenEntity.setUserName(username);
        tokenEntity.setTokenCode(encryptionService.encodeString(username + password + tokenEntity.getCreatedDateTime()));
        tokenEntity.setValidTo(tokenEntity.getCreatedDateTime().plusMinutes(ONE_HOUR));
        return tokenEntity;
    }

    @Override
    public TokenEntity save(TokenEntity tokenEntity) {
        return tokenRepository.save(tokenEntity);
    }

    @Override
    @Transactional
    public void deleteTokenForUser(String userName) {
        tokenRepository.deleteByUserName(userName);
    }

    @Override
    public Optional<TokenEntity> findByTokenCode(String tokenCode) {
        return tokenRepository.findByTokenCode(tokenCode);
    }
}
