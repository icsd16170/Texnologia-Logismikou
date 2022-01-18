package project.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.AccessMode;
import java.util.Objects;
import javax.validation.constraints.NotNull;
import project.errorhandling.validation.OnCreate;
import project.errorhandling.validation.user.password.ValidPassword;
import project.errorhandling.validation.user.role.ValidCustomer;
import project.errorhandling.validation.user.role.ValidEmployee;
import project.errorhandling.validation.user.role.ValidRole;
import project.errorhandling.validation.user.username.ValidUserName;

@ValidEmployee
@ValidCustomer
public class UserDTO {
    @Schema(accessMode = AccessMode.READ_ONLY)
    private Long id;

    @NotNull(message = "must not be null")
    @ValidUserName
    private String userName;

    @NotNull(message = "must not be null")
    @ValidPassword(groups = OnCreate.class)
    private String password;

    @NotNull(groups = OnCreate.class, message = "must not be null")
    private String passwordVerification;

    @NotNull(message = "must not be null")
    private String name;

    @NotNull(message = "must not be null")
    private String lastName;

    @NotNull(message = "must not be null")
    @ValidRole
    private String role;

    private String employeeType;

    private String address;

    private String cardType;

    private String cardNumber;

    private String expirationDate;

    private String cvc;

    @Schema(accessMode = AccessMode.READ_ONLY)
    private boolean active;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordVerification() {
        return passwordVerification;
    }

    public void setPasswordVerification(String passwordVerification) {
        this.passwordVerification = passwordVerification;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmployeeType() {
        return employeeType;
    }

    public void setEmployeeType(String employeeType) {
        this.employeeType = employeeType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getCvc() {
        return cvc;
    }

    public void setCvc(String cvc) {
        this.cvc = cvc;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDTO userDTO = (UserDTO) o;
        return active == userDTO.active && Objects.equals(id, userDTO.id) && Objects.equals(userName, userDTO.userName)
                && Objects.equals(password, userDTO.password) && Objects.equals(passwordVerification, userDTO.passwordVerification)
                && Objects.equals(name, userDTO.name) && Objects.equals(lastName, userDTO.lastName) && Objects.equals(role,
                userDTO.role) && Objects.equals(employeeType, userDTO.employeeType) && Objects.equals(address, userDTO.address)
                && Objects.equals(cardType, userDTO.cardType) && Objects.equals(cardNumber, userDTO.cardNumber) && Objects.equals(
                expirationDate, userDTO.expirationDate) && Objects.equals(cvc, userDTO.cvc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userName, password, passwordVerification, name, lastName, role, employeeType, address, cardType, cardNumber, expirationDate,
                cvc,
                active);
    }
}
