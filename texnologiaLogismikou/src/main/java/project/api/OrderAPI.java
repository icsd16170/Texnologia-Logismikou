package project.api;

import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import javax.validation.Valid;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
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
import project.dto.OrderDTO;
import project.dto.SystemStatisticsDTO;
import project.dto.UserStatisticsDTO;

@RequestMapping("/order-managmement")
@Tag(name = "Orders")
public interface OrderAPI {

    @PostMapping("/orders/create")
    ResponseEntity<OrderDTO> create(@Valid @RequestBody OrderDTO orderDTO, @RequestHeader String accessToken, @RequestHeader String loggedInUser);

    @GetMapping("/orders/{id}")
    ResponseEntity<OrderDTO> findById(@PathVariable Long id, @RequestHeader String accessToken, @RequestHeader String loggedInUser,
            @RequestParam String customerUserName);

    @GetMapping("{customerUserName}/orders")
    ResponseEntity<List<OrderDTO>> findByUserName(@PathVariable String customerUserName, @RequestHeader String accessToken, @RequestHeader String loggedInUser);

    @DeleteMapping("/orders/{id}/delete")
    ResponseEntity<String> delete(@PathVariable Long id, @RequestParam String cancelReason, @RequestHeader String accessToken,
            @RequestHeader String loggedInUser,
            @RequestParam String customerUserName);

    @PutMapping("/orders/{id}")
    ResponseEntity<OrderDTO> update(@PathVariable Long id, @Valid @RequestBody OrderDTO orderDTO, @RequestHeader String accessToken,
            @RequestHeader String loggedInUser);

    @GetMapping("/orders")
    ResponseEntity<List<OrderDTO>> search(@RequestParam(required = false) String customerUserName, @RequestParam(required = false) String status,
            @RequestParam(required = false) String createdDate,
            @RequestParam(required = false) String address, @RequestHeader String accessToken, @RequestHeader String loggedInUser);

    @GetMapping(path = "statistics/download", produces = MediaType.APPLICATION_PDF_VALUE)
    ResponseEntity<InputStreamResource> downloadStatistics();

    @GetMapping(path = "statistics/{userName}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<UserStatisticsDTO> getStatisticsForUser(@PathVariable String userName, @RequestHeader String loggedInUser,
            @RequestHeader String accessToken);

    @GetMapping(path = "statistics/system", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<SystemStatisticsDTO> getStatisticsForSystem(@RequestHeader String loggedInUser,
            @RequestHeader String accessToken);
}
