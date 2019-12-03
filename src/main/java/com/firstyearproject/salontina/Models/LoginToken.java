package com.firstyearproject.salontina.Models;

public class LoginToken {

    private String loginTokenUsername;
    private String loginTokenPassword;

    public LoginToken() {
    }

    public LoginToken(String loginTokenUsername, String loginTokenPassword) {
        this.loginTokenUsername = loginTokenUsername;
        this.loginTokenPassword = loginTokenPassword;
    }

    public String getLoginTokenUsername() {
        return loginTokenUsername;
    }

    public void setLoginTokenUsername(String loginTokenUsername) {
        this.loginTokenUsername = loginTokenUsername;
    }

    public String getLoginTokenPassword() {
        return loginTokenPassword;
    }

    public void setLoginTokenPassword(String loginTokenPassword) {
        this.loginTokenPassword = loginTokenPassword;
    }
}