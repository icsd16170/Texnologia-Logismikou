package project;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import javax.annotation.PreDestroy;
import org.springframework.stereotype.Component;
import project.persistence.entity.DVDEntity;
import project.persistence.entity.UserEntity;
import project.persistence.repository.DVDRepository;
import project.persistence.repository.TokenRepository;
import project.persistence.repository.UserRepository;
import project.service.Constants;
import project.service.encryption.EncryptionService;

@Component
public class DataBaseInit {

    private final UserRepository userRepository;

    private final TokenRepository tokenRepository;

    private final DVDRepository dvdRepository;

    private final EncryptionService encryptionService;

    public DataBaseInit(UserRepository userRepository, TokenRepository tokenRepository, DVDRepository dvdRepository,
            EncryptionService encryptionService) throws IOException {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.dvdRepository = dvdRepository;
        this.encryptionService = encryptionService;
        createAdmin();
        createCustomer();
        createEmployee();
        createPaymentsSystem();
        insertDVDsFromFile();
    }

    private void createAdmin() {
        Optional<UserEntity> adminOptional = userRepository.findByUserName("admin");
        UserEntity admin = new UserEntity();
        admin.setActive(true);
        admin.setPassword(encryptionService.encodeString("admin123A!"));
        admin.setUserName("admin");
        admin.setRole(Constants.ADMIN);
        admin.setName("Application");
        admin.setLastName("Administrator");
        adminOptional.ifPresent(userEntity -> admin.setId(userEntity.getId()));
        userRepository.save(admin);
    }

    private void createPaymentsSystem() {
        Optional<UserEntity> paymentsSystemOptional = userRepository.findByUserName("payments");
        UserEntity paymentsSystem = new UserEntity();
        paymentsSystem.setActive(true);
        paymentsSystem.setPassword(encryptionService.encodeString("payments123A!"));
        paymentsSystem.setUserName("payments");
        paymentsSystem.setRole(Constants.EMPLOYEE);
        paymentsSystem.setEmployeeType(Constants.CASHIER);
        paymentsSystem.setName("Payments");
        paymentsSystem.setLastName("System");
        paymentsSystemOptional.ifPresent(userEntity -> paymentsSystem.setId(userEntity.getId()));
        userRepository.save(paymentsSystem);
    }

    private void createEmployee() {
        Optional<UserEntity> employeeOptional = userRepository.findByUserName("employee");
        UserEntity demoEmployee = new UserEntity();
        demoEmployee.setActive(true);
        demoEmployee.setPassword(encryptionService.encodeString("employee123A!"));
        demoEmployee.setUserName("employee");
        demoEmployee.setRole(Constants.EMPLOYEE);
        demoEmployee.setEmployeeType(Constants.MANAGER);
        demoEmployee.setName("EmployeeName");
        demoEmployee.setLastName("EmployeeLastname");

        employeeOptional.ifPresent(userEntity -> demoEmployee.setId(userEntity.getId()));
        userRepository.save(demoEmployee);
    }

    private void createCustomer() {
        Optional<UserEntity> demoCustomerOptional = userRepository.findByUserName("customer");
        UserEntity demoCustomer = new UserEntity();
        demoCustomer.setActive(true);
        demoCustomer.setPassword(encryptionService.encodeString("customer123A!"));
        demoCustomer.setUserName("customer");
        demoCustomer.setRole(Constants.CUSTOMER);
        demoCustomer.setName("Demo");
        demoCustomer.setLastName("Customer");
        demoCustomer.setCardNumber("0000-1111-2222-3333-4444");
        demoCustomer.setCardNumber("Credit");
        demoCustomer.setCvc("123");
        demoCustomer.setAddress("Demo Customer Address 1");

        demoCustomerOptional.ifPresent(userEntity -> demoCustomer.setId(userEntity.getId()));
        userRepository.save(demoCustomer);
    }

    private void insertDVDsFromFile() throws IOException {
        List<DVDEntity> dvdRepositoryAll = dvdRepository.findAll();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        InputStream inJson = DataBaseInit.class.getResourceAsStream("/dvds/dvds.json");


        List<DVDEntity> dvdEntities = Arrays.asList(objectMapper.readValue(inJson, DVDEntity[].class));
        if (dvdRepositoryAll.isEmpty()) {
            dvdRepository.saveAll(dvdEntities);
        }

    }

    @PreDestroy
    public void destroy() {
        tokenRepository.deleteAll();
    }
}
