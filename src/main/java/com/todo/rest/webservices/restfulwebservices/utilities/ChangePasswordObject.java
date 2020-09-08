package com.todo.rest.webservices.restfulwebservices.utilities;

import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class ChangePasswordObject implements Serializable{

    public ChangePasswordObject(){

    }

    public ChangePasswordObject(String currentPassword, String newPassword) {
        this.setCurrentPassword(currentPassword);
        this.setNewPassword(newPassword);
    }
    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }


    String currentPassword;
    String newPassword;
}
