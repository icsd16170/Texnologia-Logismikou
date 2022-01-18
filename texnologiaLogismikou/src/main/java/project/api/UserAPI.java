package project.api;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import project.dto.ChangePasswordRequestDTO;
import project.dto.UserDTO;
import project.errorhandling.validation.OnCreate;

@RequestMapping("/user-managmement/users")
@Tag(name = "Users")
public interface UserAPI {


    @GetMapping(value = "{userName}", produces = APPLICATION_JSON_VALUE)
    ResponseEntity<UserDTO> findByUserName(@PathVariable String userName, @RequestHeader("accessToken") String accessToken,
            @RequestHeader("loggedInUser") String loggedInUser);

    @PostMapping("/register")
    ResponseEntity<UserDTO> register(@Validated(OnCreate.class) @RequestBody UserDTO userDTO);

    @PostMapping("/login")
    @Schema(name = "login")
    ResponseEntity<String> login(@RequestHeader("userName") String userName, @RequestHeader("password") String password);

    @DeleteMapping("{userName}/delete")
    ResponseEntity<String> delete(@PathVariable String userName, @RequestHeader("accessToken") String accessToken,
            @RequestHeader("loggedInUser") String loggedInUser);

    @PostMapping("{userName}/activate")
    ResponseEntity<String> activate(@PathVariable String userName, @RequestHeader("accessToken") String accessToken,
            @RequestHeader("loggedInUser") String loggedInUser);

    @PostMapping("{userName}/deactivate")
    ResponseEntity<String> deactivate(@PathVariable String userName, @RequestHeader("accessToken") String accessToken,
            @RequestHeader("loggedInUser") String loggedInUser);

    @PostMapping("{userName}/change-password")
    ResponseEntity<String> changePassword(@Valid @RequestBody ChangePasswordRequestDTO changePasswordRequestDTO, @PathVariable String userName,
            @RequestHeader("accessToken") String accessToken);

    @PutMapping("{userName}/update")
    ResponseEntity<UserDTO> update(@Valid @RequestBody UserDTO userDTO, @PathVariable String userName,
            @RequestHeader("accessToken") String accessToken, @RequestHeader("loggedInUser") String loggedInUser);

}
