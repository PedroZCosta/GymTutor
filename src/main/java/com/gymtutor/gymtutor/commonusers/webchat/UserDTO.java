package com.gymtutor.gymtutor.commonusers.webchat;

import com.gymtutor.gymtutor.user.User;

public class UserDTO {
    private Integer  userId;
    private String userName;
    private String userEmail;

    public UserDTO() {
        // construtor padrão exigido pelo JSON
    }
    public UserDTO(User user) {
        this.userId = user.getUserId();
        this.userName = user.getUserName();
        this.userEmail = user.getUserEmail();
    }

    // getters e setters
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    public String getUserName() {return userName;}
    public void setUserName(String userName) {this.userName = userName;}
    public String getUserEmail() {return userEmail;}
    public void setUserEmail(String userEmail) {this.userEmail = userEmail;}
}