package com.example.kazan.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Employee {
    int id;
    String firstName;
    String lastName;
    String phone;
    String username;

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

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    String password;
    boolean isAdmin;
    public  Employee(){};
    public Employee(JSONObject jsonObject ) throws JSONException {
        this.id = jsonObject.getInt("ID");
        this.firstName = jsonObject.getString("FirstName");
        this.lastName = jsonObject.getString("LastName");
        this.phone = jsonObject.getString("Phone");
        this.username = jsonObject.getString("Username");
        this.password = jsonObject.getString("Password");
        if(jsonObject.getString("isAdmin").equals("null")){
            this.isAdmin = false;
        }else {
            isAdmin=true;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
