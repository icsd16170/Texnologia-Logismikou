package project.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import project.api.DVDAPI;
import project.dto.DVDDTO;
import project.dto.DVDStatisticsDTO;
import project.errorhandling.exception.VerificationRoleException;
import project.service.Constants;
import project.service.authentication.AuthenticationService;
import project.service.dvd.DVDService;
import project.service.statistics.DVDStatisticsService;

@RestController
public class DVDController implements DVDAPI {

    private final DVDService dvdService;

    private final AuthenticationService authenticationService;

    private final DVDStatisticsService dvdStatisticsService;

    @Autowired
    public DVDController(DVDService dvdService, AuthenticationService authenticationService, DVDStatisticsService dvdStatisticsService) {
        this.dvdService = dvdService;
        this.authenticationService = authenticationService;
        this.dvdStatisticsService = dvdStatisticsService;
    }


    @Override
    public ResponseEntity<DVDDTO> create(DVDDTO dvdDTO, String loggedInUserName, String token) {
        String loggedInUserRole = authenticationService.verifyToken(token, loggedInUserName);
        authenticationService.verifyEmployeeRole(loggedInUserRole);
        DVDDTO dvddto1 = dvdService.create(dvdDTO);
        return new ResponseEntity<>(dvddto1, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<DVDDTO> findById(Long id, String loggedInUserName, String token) {
        String loggedInUserRole = authenticationService.verifyToken(token, loggedInUserName);
        if (Constants.EMPLOYEE.equals(loggedInUserRole) || Constants.CUSTOMER.equals(loggedInUserRole)) {
            return new ResponseEntity<>(dvdService.findById(id), HttpStatus.OK);
        }
        throw new VerificationRoleException();

    }

    @Override
    public ResponseEntity<List<DVDDTO>> findByAll(String loggedInUserName, String token) {
        String loggedInUserRole = authenticationService.verifyToken(token, loggedInUserName);
        if (Constants.EMPLOYEE.equals(loggedInUserRole) || Constants.CUSTOMER.equals(loggedInUserRole)) {
            return new ResponseEntity<>(dvdService.findAll(), HttpStatus.OK);
        }
        throw new VerificationRoleException();
    }



    @Override
    public ResponseEntity<String> delete(Long id, String loggedInUserName, String token) {
        String loggedInUserRole = authenticationService.verifyToken(token, loggedInUserName);
        authenticationService.verifyEmployeeRole(loggedInUserRole);
        dvdService.delete(id);
        return new ResponseEntity<>("DVD deleted", HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<DVDDTO> update(Long id, DVDDTO dvdDTO, String loggedInUserName, String token) {
        String loggedInUserRole = authenticationService.verifyToken(token, loggedInUserName);
        authenticationService.verifyEmployeeRole(loggedInUserRole);
        return new ResponseEntity<>(dvdService.update(id, dvdDTO), HttpStatus.OK);
    }


    @Override
    public ResponseEntity<DVDStatisticsDTO> getDVDStatistics(Long id, String loggedInUserName, String token) {
        return new ResponseEntity<>(dvdStatisticsService.getDvdStatistics(id), HttpStatus.OK);
    }
}
