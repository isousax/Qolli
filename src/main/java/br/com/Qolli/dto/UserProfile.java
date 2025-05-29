package br.com.Qolli.dto;

import java.util.Date;

public class UserProfile {
    private String name;
    private String email;
    private String uid;
    private Date createdAt;

    public UserProfile(String name, String email, String uid, Date createdAt) {
        this.name = name;
        this.email = email;
        this.uid = uid;
        this.createdAt = createdAt;
    }

    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getUid() { return uid; }
    public Date getCreatedAt() { return createdAt; }
}