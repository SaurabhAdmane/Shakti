package org.shakticoin.api.auth;

public class PasswordResetRequest {
    private String email;

    public PasswordResetRequest(String emailAddress) {
        email = emailAddress;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
