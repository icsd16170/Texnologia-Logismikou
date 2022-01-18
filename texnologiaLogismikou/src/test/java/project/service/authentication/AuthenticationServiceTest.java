package project.service.authentication;

import static org.mockito.ArgumentMatchers.anyString;

import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import project.dto.UserDTO;
import project.errorhandling.exception.AuthenticationFailedException;
import project.errorhandling.exception.TokenVerificationFailedException;
import project.errorhandling.exception.VerificationRoleException;
import project.persistence.entity.TokenEntity;
import project.service.encryption.EncryptionService;
import project.service.token.TokenService;
import project.service.user.UserService;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private TokenService tokenService;

    @Mock
    private EncryptionService encryptionService;

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthenticationServiceImpl testee;

    private static final String USERNAME = "username";

    private static final String PASSSWORD = "password";

    private static final LocalDateTime sampleDate = LocalDateTime.of(2021, 1, 1, 12, 0);


    @Test
    void authenticateUser_userInactive_throwsAuthenticationFailedException() {
        Mockito.when(userService.findByUserName(USERNAME)).thenReturn(getUser());

        Assertions.assertThrows(AuthenticationFailedException.class, () -> testee.authenticateUser(USERNAME, PASSSWORD));
    }


    @Test
    void authenticateUser_userActiveAndValidCredentials_returnsToken() {
        UserDTO user = getUser();
        user.setActive(true);
        Mockito.when(userService.findByUserName(USERNAME)).thenReturn(user);
        Mockito.when(encryptionService.stringsMatch(anyString(), anyString())).thenReturn(true);
        TokenEntity token = getToken();
        Mockito.when(tokenService.createTokenForUser(anyString(), anyString())).thenReturn(token);

        testee.authenticateUser(USERNAME, PASSSWORD);
        Mockito.verify(tokenService).deleteTokenForUser(USERNAME);
        Mockito.verify(tokenService).save(token);
    }

    @Test
    void authenticateUser_userActiveAndInValidCredentials_throwsAuthenticationFailedException() {
        UserDTO user = getUser();
        user.setActive(true);
        Mockito.when(userService.findByUserName(USERNAME)).thenReturn(user);
        Mockito.when(encryptionService.stringsMatch(anyString(), anyString())).thenReturn(false);

        Assertions.assertThrows(AuthenticationFailedException.class, () -> testee.authenticateUser(USERNAME, PASSSWORD));
    }

    @Test
    void authenticateUser_userActiveAndInValidCredentials_deactivatesUser() {
        UserDTO user = getUser();
        user.setActive(true);
        Mockito.when(userService.findByUserName(USERNAME)).thenReturn(user);
        Mockito.when(encryptionService.stringsMatch(anyString(), anyString())).thenReturn(false);

        Assertions.assertThrows(AuthenticationFailedException.class, () -> testee.authenticateUser(USERNAME, PASSSWORD));
        Assertions.assertThrows(AuthenticationFailedException.class, () -> testee.authenticateUser(USERNAME, PASSSWORD));
        Assertions.assertThrows(AuthenticationFailedException.class, () -> testee.authenticateUser(USERNAME, PASSSWORD));

        Mockito.verify(userService).deactivateUser(USERNAME);
        Mockito.verify(tokenService).deleteTokenForUser(USERNAME);
    }


    @Test
    void verifyToken_validToken_returnsRole() {
        TokenEntity token = getToken();
        UserDTO user = getUser();

        Mockito.when(tokenService.findByTokenCode("tokenCode")).thenReturn(Optional.of(token));
        Mockito.when(userService.findByUserName(USERNAME)).thenReturn(user);

        String actual = testee.verifyToken("tokenCode", USERNAME);

        Assertions.assertEquals("role", actual);

    }

    @Test
    void verifyToken_expiredToken_throwsTokenVerificationFailedException() {
        TokenEntity token = getToken();
        token.setValidTo(LocalDateTime.now().minusDays(1));

        Mockito.when(tokenService.findByTokenCode("tokenCode")).thenReturn(Optional.of(token));

        TokenVerificationFailedException ex =
                Assertions.assertThrows(TokenVerificationFailedException.class, () -> testee.verifyToken("tokenCode", USERNAME));
        Assertions.assertEquals("Token has expired.", ex.getMessage());
    }

    @Test
    void verifyToken_tokenDoesNotBelongToUser_throwsTokenVerificationFailedException() {
        TokenEntity token = getToken();

        Mockito.when(tokenService.findByTokenCode("tokenCode")).thenReturn(Optional.of(token));

        TokenVerificationFailedException ex =
                Assertions.assertThrows(TokenVerificationFailedException.class, () -> testee.verifyToken("tokenCode", "otherUser"));
        Assertions.assertEquals("Token does not belong to user", ex.getMessage());
    }

    @Test
    void verifyToken_invalidToken_throwsTokenVerificationFailedException() {
        Mockito.when(tokenService.findByTokenCode("tokenCode")).thenReturn(Optional.empty());

        TokenVerificationFailedException ex =
                Assertions.assertThrows(TokenVerificationFailedException.class, () -> testee.verifyToken("tokenCode", USERNAME));
        Assertions.assertEquals("Invalid token.", ex.getMessage());
    }


    @Test
    void verifyToken_invalidToken3Times_blocksUser() {
        TokenEntity token = getToken();

        Mockito.when(tokenService.findByTokenCode("tokenCode")).thenReturn(Optional.of(token));

        for (int i = 0; i < 2; i++) {
            try {
                testee.verifyToken("tokenCode", "otherUser");
            } catch (TokenVerificationFailedException ex) {
                System.out.println(i + 1 + " call");
            }
        }


        TokenVerificationFailedException ex =
                Assertions.assertThrows(TokenVerificationFailedException.class, () -> testee.verifyToken("tokenCode", "otherUser"));

        Mockito.verify(userService).deactivateUser("otherUser");
        Mockito.verify(userService).deactivateUser(USERNAME);
        Mockito.verify(tokenService).deleteTokenForUser(USERNAME);
        Mockito.verify(tokenService).deleteTokenForUser("otherUser");
    }

    @Test
    void verifyRole_validCase_returnsTrue() {
        Assertions.assertTrue(testee.verifyRole("loggedRole", "user", "loggedUser", "loggedRole"));
    }

    @Test
    void verifyRole_wrongRole_throwsVerificationRoleException() {
        Assertions.assertThrows(VerificationRoleException.class, () -> testee.verifyRole("loggedRole", "user", "loggedUser", "role"));
    }

    @Test
    void verifyRole_sameUser_returnsTrue() {
        Assertions.assertTrue(testee.verifyRole("loggedRole", "user", "user", "role"));
    }

    @Test
    void verifyAdminRole_validCase_returnsTrue() {
        Assertions.assertTrue(testee.verifyAdminRole("ADMIN"));
    }

    @Test
    void verifyAdminRole_userNotAdmin_throwsVerificationRoleException() {
        Assertions.assertThrows(VerificationRoleException.class, () -> testee.verifyAdminRole("notADMIN"));
    }

    @Test
    void verifyEmployeeRole_validCase_returnsTrue() {
        Assertions.assertTrue(testee.verifyEmployeeRole("EMPLOYEE"));
    }

    @Test
    void verifyEmployeeRole_userNotAdmin_throwsVerificationRoleException() {
        Assertions.assertThrows(VerificationRoleException.class, () -> testee.verifyEmployeeRole("notEmployee"));
    }

    @Test
    void verifyOldPassword_validOldPassword_deletesToken() {
        UserDTO user = getUser();
        Mockito.when(userService.findByUserName(USERNAME)).thenReturn(user);
        Mockito.when(encryptionService.stringsMatch(anyString(), anyString())).thenReturn(true);

        testee.verifyOldPassword("oldPassword", USERNAME);
        Mockito.verify(tokenService).deleteTokenForUser(USERNAME);
    }


    @Test
    void verifyOldPassword_invalidOldPassword_throwsAuthenticationFailedException() {
        UserDTO user = getUser();
        Mockito.when(userService.findByUserName(USERNAME)).thenReturn(user);
        Mockito.when(encryptionService.stringsMatch(anyString(), anyString())).thenReturn(false);

        Assertions.assertThrows(AuthenticationFailedException.class, () -> testee.verifyOldPassword("oldPassword", USERNAME));
    }

    @Test
    void verifyOldPassword_invalidOldPassword3times_blocksUser() {
        UserDTO user = getUser();
        Mockito.when(userService.findByUserName(USERNAME)).thenReturn(user);
        Mockito.when(encryptionService.stringsMatch(anyString(), anyString())).thenReturn(false);

        Assertions.assertThrows(AuthenticationFailedException.class, () -> testee.verifyOldPassword("oldPassword", USERNAME));
        Assertions.assertThrows(AuthenticationFailedException.class, () -> testee.verifyOldPassword("oldPassword", USERNAME));
        Assertions.assertThrows(AuthenticationFailedException.class, () -> testee.verifyOldPassword("oldPassword", USERNAME));
        Mockito.verify(userService).deactivateUser(USERNAME);
        Mockito.verify(tokenService).deleteTokenForUser(USERNAME);
    }

    private UserDTO getUser() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserName("username");
        userDTO.setPassword("password");
        userDTO.setRole("role");
        return userDTO;
    }

    private TokenEntity getToken() {
        TokenEntity token = new TokenEntity();
        token.setTokenCode("tokenCode");
        token.setUserName(USERNAME);
        token.setCreatedDateTime(sampleDate);
        token.setValidTo(LocalDateTime.now().plusDays(1));
        return token;
    }
}
