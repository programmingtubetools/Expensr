package com.programmingtubeofficial.expensr;

public class RegisterHelper {
    private String password;
    private String confirm_password;
    private String name;
    private String email;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirm_password() {
        return confirm_password;
    }

    public void setConfirm_password(String confirm_password) {
        this.confirm_password = confirm_password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public RegisterHelper(){}
    public RegisterHelper(String email, String password, String confirm_password, String name) {
        this.email = email;
        this.password = password;
        this.confirm_password = confirm_password;
        this.name = name;
    }
}
