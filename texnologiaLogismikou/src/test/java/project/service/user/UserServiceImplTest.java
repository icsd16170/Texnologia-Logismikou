package project.service.user;

import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import project.dto.UserDTO;
import project.errorhandling.exception.UserNameAlreadyExistsException;
import project.errorhandling.exception.UserNotFoundException;
import project.persistence.entity.UserEntity;
import project.persistence.repository.UserRepository;
import project.service.encryption.EncryptionService;
import project.service.token.TokenService;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private EncryptionService encryptionService;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private UserServiceImpl testee;

    @Test
    void register_userNotExists_savesUser() {
        String password = "pass";

        UserDTO input = new UserDTO();
        input.setPassword(password);
        input.setPasswordVerification(password);

        UserDTO expected = new UserDTO();
        expected.setPassword(password);

        UserEntity userEntity = new UserEntity();
        userEntity.setPassword(password);

        Mockito.when(userRepository.findByUserName(any())).thenReturn(Optional.empty());
        Mockito.when(encryptionService.encodeString(any())).thenReturn(password);
        Mockito.when(userRepository.save(userEntity)).thenReturn(userEntity);

        UserDTO actual = testee.register(input);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void register_userExists_throwsUserNameAlreadyExistsException() {
        String password = "pass";
        UserDTO input = new UserDTO();
        input.setPassword(password);
        input.setPasswordVerification(password);

        Mockito.when(encryptionService.encodeString(any())).thenReturn("password");
        Mockito.when(userRepository.findByUserName(any())).thenReturn(Optional.of(new UserEntity()));

        Assertions.assertThrows(UserNameAlreadyExistsException.class, () -> testee.register(input));
    }

    @Test
    void findByUserName_userExists_returnsUser() {
        UserDTO expected = getUserDTO();
        Mockito.when(userRepository.findByUserName(any())).thenReturn(Optional.of(getUserEntity()));

        UserDTO actual = testee.findByUserName("username");

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void findByUserName_userNotExists_throwsUserNotFoundException() {
        Mockito.when(userRepository.findByUserName(any())).thenReturn(Optional.empty());
        Assertions.assertThrows(UserNotFoundException.class, () -> testee.findByUserName("username"));
    }

    @Test
    void activateUser_userExists_activatesUser() {
        UserEntity expected = getUserEntity();
        expected.setActive(true);
        Mockito.when(userRepository.findByUserName(any())).thenReturn(Optional.of(getUserEntity()));

        testee.activateUser("username");
        Mockito.verify(userRepository).save(expected);
    }

    @Test
    void deactivateUser_userExists_deactivatesUser() {
        UserEntity expected = getUserEntity();
        Mockito.when(userRepository.findByUserName(any())).thenReturn(Optional.of(getUserEntity()));

        testee.deactivateUser("username");
        Mockito.verify(userRepository).save(expected);
    }

    @Test
    void activateUser_userNotExists_throwsUserNotFoundException() {
        Mockito.when(userRepository.findByUserName(any())).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> testee.activateUser("username"));

    }

    @Test
    void deactivateUser_userNotExists_throwsUserNotFoundException() {
        Mockito.when(userRepository.findByUserName(any())).thenReturn(Optional.empty());
        Assertions.assertThrows(UserNotFoundException.class, () -> testee.deactivateUser("username"));

    }

    @Test
    void delete_userNotExists_throwsUserNotFoundException() {
        Mockito.when(userRepository.existsByUserName(any())).thenReturn(false);
        Assertions.assertThrows(UserNotFoundException.class, () -> testee.delete("username"));

    }

    @Test
    void delete_userExists_deletesUser() {
        Mockito.when(userRepository.existsByUserName(any())).thenReturn(true);

        testee.delete("username");
        Mockito.verify(userRepository).deleteByUserName("username");
    }

    @Test
    void changePassword_userExists_savesUserswithNewPassword(){
        UserEntity expected = getUserEntity();
        expected.setPassword("newPassword");

        Mockito.when(userRepository.findByUserName(any())).thenReturn(Optional.of(getUserEntity()));
        Mockito.when(encryptionService.encodeString(any())).thenReturn("newPassword");

        testee.changePassword("username","newPassword");
        Mockito.verify(userRepository).save(expected);
    }


    private UserDTO getUserDTO() {
        UserDTO userDTO = new UserDTO();
        userDTO.setRole("role");
        userDTO.setPassword("password");
        userDTO.setName("name");
        userDTO.setLastName("lastname");

        return userDTO;
    }


    private UserEntity getUserEntity() {
        UserEntity userEntity = new UserEntity();
        userEntity.setRole("role");
        userEntity.setPassword("password");
        userEntity.setName("name");
        userEntity.setLastName("lastname");
        userEntity.setAddress("address");

        return userEntity;
    }
}