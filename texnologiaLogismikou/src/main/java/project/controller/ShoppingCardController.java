package project.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import project.api.ShoppingCardAPI;
import project.dto.OrderDTO;
import project.dto.ShoppingCardDTO;
import project.errorhandling.exception.VerificationRoleException;
import project.service.Constants;
import project.service.authentication.AuthenticationService;
import project.service.shoppingCard.ShoppingCardService;
import project.service.user.UserService;

@RestController
public class ShoppingCardController implements ShoppingCardAPI {

    private final ShoppingCardService shoppingCardService;

    private final AuthenticationService authenticationService;

    private final UserService userService;

    @Autowired
    public ShoppingCardController(ShoppingCardService shoppingCardService, AuthenticationService authenticationService,
            UserService userService) {
        this.shoppingCardService = shoppingCardService;
        this.authenticationService = authenticationService;
        this.userService = userService;
    }


    @Override
    public ResponseEntity<ShoppingCardDTO> create(ShoppingCardDTO shoppingCardDTO, String loggedInUserName, String token) {
        String loggedInUserRole = authenticationService.verifyToken(token, loggedInUserName);
        authenticationService.verifyRole(loggedInUserRole, shoppingCardDTO.getCustomerUserName(), loggedInUserName, Constants.EMPLOYEE);
        userService.findByUserName(shoppingCardDTO.getCustomerUserName());
        ShoppingCardDTO shoppingCardDTO1 = shoppingCardService.create(shoppingCardDTO);
        return new ResponseEntity<>(shoppingCardDTO1, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<String> delete(long id, String loggedInUserName, String token, String customerUserName) {
        String loggedInUserRole = authenticationService.verifyToken(token, loggedInUserName);
        authenticationService.verifyRole(loggedInUserRole, customerUserName, loggedInUserName, Constants.EMPLOYEE);
        userService.findByUserName(customerUserName);
        shoppingCardService.delete(id);


        return new ResponseEntity<>("Shopping Card deleted", HttpStatus.OK

        );
    }

    @Override
    public ResponseEntity<ShoppingCardDTO> findById(Long id, String loggedInUserName, String token, String customerUserName) {
        String loggedInUserRole = authenticationService.verifyToken(token, loggedInUserName);
        authenticationService.verifyRole(loggedInUserRole, customerUserName, loggedInUserName, Constants.EMPLOYEE);
        return new ResponseEntity<>(shoppingCardService.findById(id), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<ShoppingCardDTO>> findByUserName(String customerUserName, String loggedInUserName, String token) {
        String loggedInUserRole = authenticationService.verifyToken(token, loggedInUserName);
        authenticationService.verifyRole(loggedInUserRole, customerUserName, loggedInUserName, Constants.EMPLOYEE);
        return new ResponseEntity<>(shoppingCardService.findByUserName(customerUserName), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> submit(Long id, String loggedInUserName, String token, String customerUserName) {
        String loggedInUserRole = authenticationService.verifyToken(token, loggedInUserName);
        authenticationService.verifyRole(loggedInUserRole, customerUserName, loggedInUserName, Constants.EMPLOYEE);
        shoppingCardService.submit(id);
        return new ResponseEntity<>("Shopping Card submitted", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ShoppingCardDTO> update(Long id, ShoppingCardDTO shoppingCardDTO, String accessToken, String loggedInUser) {
        String loggedInUserRole = authenticationService.verifyToken(accessToken, loggedInUser);
        authenticationService.verifyRole(loggedInUserRole, shoppingCardDTO.getCustomerUserName(), loggedInUser, Constants.EMPLOYEE);

        return new ResponseEntity<>(shoppingCardService.update(shoppingCardDTO, id), HttpStatus.OK);

    }

}
