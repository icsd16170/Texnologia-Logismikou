package project.service.user;



import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.dto.UserDTO;
import project.errorhandling.exception.PasswordChangedException;
import project.errorhandling.exception.PasswordNotMatchException;
import project.errorhandling.exception.RoleChangedException;
import project.errorhandling.exception.UserNameAlreadyExistsException;
import project.errorhandling.exception.UserNotFoundException;
import project.mapper.UserMapper;
import project.persistence.entity.UserEntity;
import project.persistence.repository.UserRepository;
import project.service.encryption.EncryptionService;
import project.service.token.TokenService;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final EncryptionService encryptionService;

    private final TokenService tokenService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, EncryptionService encryptionService, TokenService tokenService) {
        this.userRepository = userRepository;
        this.encryptionService = encryptionService;
        this.tokenService = tokenService;
    }

    @Override
    public UserDTO register(UserDTO userDTO) {
        UserEntity userEntity = UserMapper.mapDTOToEntity(userDTO);
        String encryptedPassword = encryptionService.encodeString(userDTO.getPassword());
        userEntity.setPassword(encryptedPassword);

        Optional<UserEntity> userEntityOptional = userRepository.findByUserName(userDTO.getUserName());

        checkPasswords(userDTO.getPassword(), userDTO.getPasswordVerification());

        if (userEntityOptional.isEmpty()) {
            userEntity.setActive(false);

            return UserMapper.mapEntityToDTO(userRepository.save(userEntity));
        }
        throw new UserNameAlreadyExistsException(userDTO.getUserName());
    }

    @Override
    public UserDTO findByUserName(String userName) {
        Optional<UserEntity> userEntityOptional = userRepository.findByUserName(userName);
        if (userEntityOptional.isPresent()) {
            return UserMapper.mapEntityToDTO(userEntityOptional.get());
        }
        throw new UserNotFoundException(userName);
    }

    @Override
    public void deactivateUser(String username) {
        Optional<UserEntity> userEntity = userRepository.findByUserName(username);
        userEntity.ifPresentOrElse(user -> {
            user.setActive(false);
            userRepository.save(user);
        }, () -> {
            throw new UserNotFoundException(username);
        });
    }

    @Override
    public void activateUser(String username) {
        Optional<UserEntity> userEntity = userRepository.findByUserName(username);
        userEntity.ifPresentOrElse(user -> {
            user.setActive(true);
            userRepository.save(user);
        }, () -> {
            throw new UserNotFoundException(username);
        });
    }

    @Override
    @Transactional
    public void delete(String userName) {
        if (userRepository.existsByUserName(userName)) {
            userRepository.deleteByUserName(userName);
            return;
        }
        throw new UserNotFoundException(userName);
    }

    @Override
    public void changePassword(String userName, String newPassword) {
        Optional<UserEntity> userOptional = userRepository.findByUserName(userName);
        userOptional.ifPresent(user -> {
            user.setPassword(encryptionService.encodeString(newPassword));
            userRepository.save(user);
        });
    }

    @Override
    @Transactional
    public UserDTO update(UserDTO userDTO, String userName) {
        UserDTO userBeforeUpdate = findByUserName(userName);

        if (!encryptionService.stringsMatch(userBeforeUpdate.getPassword(), userDTO.getPassword())) {
            throw new PasswordChangedException();
        }
        if (!userBeforeUpdate.getRole().equals(userDTO.getRole())) {
            throw new RoleChangedException();
        }
        if (!userDTO.getUserName().equals(userName)) {
            if (userRepository.existsByUserName(userDTO.getUserName())) {
                throw new UserNameAlreadyExistsException(userDTO.getUserName());
            }
            tokenService.deleteTokenForUser(userName);
        }


        UserEntity entityToUpdate = UserMapper.mapDTOToEntity(userDTO);
        entityToUpdate.setId(userBeforeUpdate.getId());
        entityToUpdate.setActive(userBeforeUpdate.isActive());
        entityToUpdate.setPassword(userBeforeUpdate.getPassword());

        UserEntity updatedUserEntity = userRepository.save(entityToUpdate);
        return UserMapper.mapEntityToDTO(updatedUserEntity);

    }

    @Override
    public boolean existsByUserName(String userName) {
        return userRepository.existsByUserName(userName);
    }

    @Override
    public List<UserDTO> findAllUsers() {
        return UserMapper.mapEntityListToDTOList(userRepository.findAll());
    }

    private void checkPasswords(String password, String passwordVerification) {
        if (!password.equals(passwordVerification)) {
            throw new PasswordNotMatchException();
        }
    }

}
