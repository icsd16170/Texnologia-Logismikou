package project.api;

import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import javax.validation.Valid;
import javax.websocket.server.PathParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import project.dto.DVDDTO;
import project.dto.OrderDTO;
import project.dto.ShoppingCardDTO;

@RequestMapping("/shopping-card-managmement")
@Tag(name = "Shopping-Cards")
public interface ShoppingCardAPI {


    @PostMapping("/shopping-cards/create")
    ResponseEntity<ShoppingCardDTO> create(@Valid @RequestBody ShoppingCardDTO shoppingCardDTO, @RequestHeader("loggedInUserName") String loggedInUserName,
            @RequestHeader("token") String token);


    @DeleteMapping("/shopping-cards/{id}/delete")
    ResponseEntity<String> delete(@Valid @PathParam("id") long id, @RequestHeader("loggedInUserName") String loggedInUserName,
            @RequestHeader("token") String token, @RequestHeader("customerUserName") String customerUserName);

    @GetMapping("shopping-cards/{id}")
    ResponseEntity<ShoppingCardDTO> findById(Long id, @RequestHeader("loggedInUserName") String loggedInUserName,
            @RequestHeader("token") String token, @RequestParam("customerUserName") String customerUserName);

    @GetMapping("shopping-cards/{customerUserName}")
    ResponseEntity<List<ShoppingCardDTO>> findByUserName(String userName, @RequestHeader("loggedInUserName") String loggedInUserName,
            @RequestHeader("token") String token);

    @PostMapping("shopping-cards/{id}/submit")
    ResponseEntity<String> submit(Long id, @RequestHeader("loggedInUserName") String loggedInUserName,
            @RequestHeader("token") String token, @RequestParam("customerUserName") String customerUserName);


    @PutMapping("/shopping-cards/{id}")
    ResponseEntity<ShoppingCardDTO> update(@PathVariable Long id, @Valid @RequestBody ShoppingCardDTO orderDTO, @RequestHeader String accessToken,
            @RequestHeader String loggedInUser);
}
