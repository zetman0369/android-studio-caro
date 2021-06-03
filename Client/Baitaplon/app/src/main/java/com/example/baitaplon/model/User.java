package com.example.baitaplon.model;

import java.io.Serializable;

public class User implements Serializable {

    private int id;

    private String username;

    private String password;

    private String email;

    private String phone;

    private int point;

    private int status;

    private String listFriend;

    private String addFriendRequest;

    public String getAddFriendRequest() {
        return addFriendRequest;
    }

    public void setAddFriendRequest(String addFriendRequest) {
        this.addFriendRequest = addFriendRequest;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getListFriend() {
        return listFriend;
    }

    public void setListFriend(String listFriend) {
        this.listFriend = listFriend;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}



