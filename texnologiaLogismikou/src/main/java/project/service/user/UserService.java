package project.service.user;

import java.util.List;
import project.dto.UserDTO;

public interface UserService {

    UserDTO register(UserDTO userDTO);

    UserDTO findByUserName(String userName);

    void deactivateUser(String userName);

    void activateUser(String userName);

    void delete(String userName);

    void changePassword(String userName, String newPassword);

    UserDTO update(UserDTO userDTO, String userName);

    boolean existsByUserName(String userName);

    List<UserDTO> findAllUsers();
}