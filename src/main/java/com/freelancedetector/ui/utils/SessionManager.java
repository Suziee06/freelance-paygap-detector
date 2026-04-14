package com.freelancedetector.ui.utils;

public class SessionManager {
    private static SessionManager instance;

    private Long loggedInUserId;
    private String loggedInUserName;
    private String loggedInUserGender;

    private SessionManager() {}

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void setSession(Long userId, String name, String gender) {
        this.loggedInUserId = userId;
        this.loggedInUserName = name;
        this.loggedInUserGender = gender;
    }

    public void clearSession() {
        this.loggedInUserId = null;
        this.loggedInUserName = null;
        this.loggedInUserGender = null;
    }

    public Long getLoggedInUserId() { return loggedInUserId; }
    public String getLoggedInUserName() { return loggedInUserName; }
    public String getLoggedInUserGender() { return loggedInUserGender; }
}
