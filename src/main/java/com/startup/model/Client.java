package com.startup.model;

public class Client {
    private Long id;
    private String username;
    private String gmail;
    private String passwordHash;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getGmail() { return gmail; }
    public void setGmail(String gmail) { this.gmail = gmail; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
}
