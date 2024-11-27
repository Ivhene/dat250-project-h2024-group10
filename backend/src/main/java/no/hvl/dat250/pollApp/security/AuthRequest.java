package no.hvl.dat250.pollApp.security;

public class AuthRequest {

    private String username;
    private String password;

    public AuthRequest(String user, String password) {
        this.username = user;
        this.password = password;
    }

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
