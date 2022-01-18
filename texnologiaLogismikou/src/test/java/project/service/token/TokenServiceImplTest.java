package project.service.token;

import static org.mockito.ArgumentMatchers.any;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import project.persistence.entity.TokenEntity;
import project.persistence.repository.TokenRepository;
import project.service.encryption.EncryptionService;

@ExtendWith(MockitoExtension.class)
class TokenServiceImplTest {

    public static final LocalDateTime LOCAL_DATE_TIME = LocalDateTime.of(2021, 1, 1, 0, 0);

    @Mock
    private EncryptionService encryptionService;

    @Mock
    private TokenRepository tokenRepository;

    @InjectMocks
    private TokenServiceImpl testee;

    private static final String USER_NAME = "userName";

    private static final String TOKEN_CODE = "tokenCode";

    @Test
    void createTokenForUser_returnsToken() {


        TokenEntity expected = new TokenEntity();
        expected.setUserName(USER_NAME);
        expected.setTokenCode(TOKEN_CODE);

        Mockito.when(encryptionService.encodeString(any())).thenReturn(TOKEN_CODE);
        TokenEntity actual = testee.createTokenForUser(USER_NAME, "password");

        LocalDateTime expectedValidTo = actual.getCreatedDateTime().plusHours(1L);

        Assertions.assertEquals(expected.getUserName(), actual.getUserName());
        Assertions.assertEquals(expected.getTokenCode(), actual.getTokenCode());
        Assertions.assertEquals(expectedValidTo, actual.getValidTo());
        Mockito.verify(encryptionService, Mockito.times(1)).encodeString(any());
    }

    @Test
    void save_returnsEntity() {
        TokenEntity expected = new TokenEntity();
        expected.setUserName(USER_NAME);
        expected.setTokenCode(TOKEN_CODE);
        expected.setValidTo(LocalDateTime.of(2021,1,1,1,1));

        Mockito.when(tokenRepository.save(expected)).thenReturn(expected);

        TokenEntity actual = testee.save(expected);

        Assertions.assertEquals(expected,actual);
    }

}