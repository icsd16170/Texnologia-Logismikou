package project.controller;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import project.api.OrderAPI;
import project.dto.OrderDTO;
import project.dto.SystemStatisticsDTO;
import project.dto.UserStatisticsDTO;
import project.errorhandling.exception.VerificationRoleException;
import project.service.Constants;
import project.service.authentication.AuthenticationService;
import project.service.order.OrderService;
import project.service.statistics.OrderStatisticsService;
import project.service.statistics.PDFService;

@RestController
public class OrderController implements OrderAPI {

    private final OrderService orderService;

    private final AuthenticationService authenticationService;

    private final OrderStatisticsService orderStatisticsService;

    private final PDFService pdfService;

    @Autowired
    public OrderController(OrderService orderService, AuthenticationService authenticationService,
            OrderStatisticsService orderStatisticsService, PDFService pdfService) {
        this.orderService = orderService;
        this.authenticationService = authenticationService;
        this.orderStatisticsService = orderStatisticsService;
        this.pdfService = pdfService;
    }

    @Override
    public ResponseEntity<OrderDTO> create(OrderDTO orderDTO, String accessToken, String loggedInUser) {
        String loggedInRole = authenticationService.verifyToken(accessToken, loggedInUser);
        if (Constants.EMPLOYEE.equals(loggedInRole)) {
            return new ResponseEntity<>(orderService.create(orderDTO), HttpStatus.OK);
        }
        throw new VerificationRoleException();
    }

    @Override
    public ResponseEntity<OrderDTO> findById(Long id, String accessToken, String loggedInUser, String customerUserName) {
        String loggedInUserRole = authenticationService.verifyToken(accessToken, loggedInUser);
        authenticationService.verifyRole(loggedInUserRole, customerUserName, loggedInUser, Constants.EMPLOYEE);
        return new ResponseEntity<>(orderService.findById(id), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> delete(Long id, String cancelReason, String accessToken, String loggedInUser, String customerUserName) {
        String loggedInUserRole = authenticationService.verifyToken(accessToken, loggedInUser);
        authenticationService.verifyRole(loggedInUserRole, customerUserName, loggedInUser, Constants.EMPLOYEE);
        orderService.delete(id, cancelReason);
        return new ResponseEntity<>("Order Deleted", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<OrderDTO> update(Long id, OrderDTO orderDTO, String accessToken, String loggedInUser) {
        String loggedInRole = authenticationService.verifyToken(accessToken, loggedInUser);
        if (Constants.EMPLOYEE.equals(loggedInRole)) {
            return new ResponseEntity<>(orderService.update(orderDTO, id), HttpStatus.OK);
        }
        throw new VerificationRoleException();
    }

    @Override
    public ResponseEntity<List<OrderDTO>> findByUserName(String customerUserName, String accessToken, String loggedInUser) {
        String loggedInUserRole = authenticationService.verifyToken(accessToken, loggedInUser);
        authenticationService.verifyRole(loggedInUserRole, customerUserName, loggedInUser, Constants.EMPLOYEE);
        return new ResponseEntity<>(orderService.findByUserName(customerUserName), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<OrderDTO>> search(String customerUserName, String status, String createdDate,
            String address, String accessToken, String loggedInUser) {
        String loggedInUserRole = authenticationService.verifyToken(accessToken, loggedInUser);
        authenticationService.verifyRole(loggedInUserRole, customerUserName, loggedInUser, Constants.EMPLOYEE);

        return new ResponseEntity<>(
                orderService.search(customerUserName, status, getLocalDateFromString(createdDate), address),
                HttpStatus.OK);
    }

    @Override
    public ResponseEntity<InputStreamResource> downloadStatistics() {

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(pdfService.getStatisticsPDF());

        InputStreamResource resource = new InputStreamResource(byteArrayInputStream);

        return ResponseEntity.ok()
                             .header("Content-Disposition", "attachment; filename=\"statistics.pdf\"")
                             .contentType(MediaType.APPLICATION_PDF)
                             .body(resource);

    }

    @Override
    public ResponseEntity<UserStatisticsDTO> getStatisticsForUser(String userName, String loggedInUser, String accessToken) {
        return new ResponseEntity<>(orderStatisticsService.findStatisticsForUser(userName), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<SystemStatisticsDTO> getStatisticsForSystem(String loggedInUser, String accessToken) {
        return new ResponseEntity<>(orderStatisticsService.findSystemStatistics(), HttpStatus.OK);
    }


    private LocalDate getLocalDateFromString(String date) {
        if (date != null) {
            return LocalDate.parse(date);
        }
        return null;
    }
}
