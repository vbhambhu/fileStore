package com.example.filestore.entities;

import org.hibernate.validator.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class PasswordUpdateForm {

    @NotEmpty
    @Size(min=2, max=30)
    private String password;

    @NotEmpty
    @Size(min=2, max=30)
    private String confirmPassword;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
