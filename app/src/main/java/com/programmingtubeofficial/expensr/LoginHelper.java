package com.programmingtubeofficial.expensr;

public class LoginHelper {
    private String email;
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LoginHelper(){ }

    public LoginHelper(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public boolean login(){
        // Test User
        if(this.email.equals("test@ce.com") && this.password.equals("test")){
            return true;
        }
        return false;
    }
}
