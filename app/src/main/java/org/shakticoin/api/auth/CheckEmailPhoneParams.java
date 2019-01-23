package org.shakticoin.api.auth;

public class CheckEmailPhoneParams {
    private String email;
    private String phone_number;

    public CheckEmailPhoneParams(String email, String phone_number) {
        this.email = email;
        this.phone_number = phone_number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }
}
