package project.service.authentication;

public interface AuthenticationService {
    String authenticateUser(String username, String password);

    String verifyToken(String tokenCode, String userName);

    boolean verifyRole(String loggedInUserRole, String userName, String loggedInUserName, String role);

    boolean verifyAdminRole(String loggedInRole);

    void verifyOldPassword(String oldPassword, String username);

    boolean verifyEmployeeRole(String loggedInRole);
}
