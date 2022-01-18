package project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import project.api.UserAPI;
import project.dto.ChangePasswordRequestDTO;
import project.dto.UserDTO;
import project.errorhandling.exception.VerificationRoleException;
import project.service.Constants;
import project.service.authentication.AuthenticationService;
import project.service.token.TokenService;
import project.service.user.UserService;

@RestController
public class UserController implements UserAPI {
    private final UserService userService;

    private final AuthenticationService authenticationService;

    private final TokenService tokenService;

    @Autowired
    public UserController(UserService userService, AuthenticationService authenticationService, TokenService tokenService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
        this.tokenService = tokenService;
    }

    @Override
    public ResponseEntity<UserDTO> findByUserName(String userName, String accessToken, String loggedInUser) {
        String loggedInUserRole = authenticationService.verifyToken(accessToken, loggedInUser);
        if (userName.equals(loggedInUser) || authenticationService.verifyAdminRole(loggedInUserRole)) {
            return new ResponseEntity<>(userService.findByUserName(userName), HttpStatus.OK);
        }
        throw new VerificationRoleException();
    }

    @Override
    public ResponseEntity<UserDTO> register(UserDTO userDTO) {
        UserDTO savedUser = userService.register(userDTO);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<String> login(String userName, String password) {
        return new ResponseEntity<>(authenticationService.authenticateUser(userName, password), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> delete(String userName, String accessToken, String loggedInUser) {
        String loggedInUserRole = authenticationService.verifyToken(accessToken, loggedInUser);
        if (!userName.equals(loggedInUser) && !Constants.ADMIN.equals(loggedInUserRole)) {
            return new ResponseEntity<>("Not authorized to perform this action", HttpStatus.FORBIDDEN);
        }
        userService.delete(userName);
        tokenService.deleteTokenForUser(userName);

        return new ResponseEntity<>("User deleted", HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<String> activate(String userName, String accessToken, String loggedInUser) {
        String loggedInUserRole = authenticationService.verifyToken(accessToken, loggedInUser);
        if (!Constants.ADMIN.equals(loggedInUserRole)) {
            return new ResponseEntity<>("Not authorized to perform this action", HttpStatus.FORBIDDEN);
        }
        userService.activateUser(userName);
        return new ResponseEntity<>("User activated", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> deactivate(String userName, String accessToken, String loggedInUser) {
        String loggedInUserRole = authenticationService.verifyToken(accessToken, loggedInUser);
        if (!Constants.ADMIN.equals(loggedInUserRole)) {
            return new ResponseEntity<>("Not authorized to perform this action", HttpStatus.FORBIDDEN);
        }
        userService.deactivateUser(userName);
        tokenService.deleteTokenForUser(userName);
        return new ResponseEntity<>("User deactivated", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> changePassword(ChangePasswordRequestDTO changePasswordRequestDTO, String userName, String accessToken) {
        authenticationService.verifyToken(accessToken, userName);
        authenticationService.verifyOldPassword(changePasswordRequestDTO.getOldPassword(), userName);
        userService.changePassword(userName, changePasswordRequestDTO.getNewPassword());
        return new ResponseEntity<>("Password Changed", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<UserDTO> update(UserDTO userDTO, String userName, String accessToken, String loggedInUser) {
        String loggedInUserRole = authenticationService.verifyToken(accessToken, loggedInUser);

        if (userName.equals(loggedInUser) || authenticationService.verifyAdminRole(loggedInUserRole)) {
            return new ResponseEntity<>(userService.update(userDTO, userName), HttpStatus.OK);
        }
        throw new VerificationRoleException();
    }

}
