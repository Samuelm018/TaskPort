package com.startup.model;

public class Admin {
    private Long id;
    private String name;
    private String gmail;
    private String passwordHash;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getGmail() { return gmail; }
    public void setGmail(String gmail) { this.gmail = gmail; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
}
