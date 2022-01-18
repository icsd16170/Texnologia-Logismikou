package project.dto;

import javax.validation.constraints.NotNull;
import project.errorhandling.validation.user.password.ValidPassword;


public class ChangePasswordRequestDTO {

    @NotNull(message = "must not be null")
    private String oldPassword;

    @NotNull(message = "must not be null")
    @ValidPassword
    private String newPassword;

    @NotNull(message = "must not be null")
    private String newPasswordVerification;

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getNewPasswordVerification() {
        return newPasswordVerification;
    }

    public void setNewPasswordVerification(String newPasswordVerification) {
        this.newPasswordVerification = newPasswordVerification;
    }
}
