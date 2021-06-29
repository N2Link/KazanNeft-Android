package com.example.kazan.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Department {
    int id;
    String name;
    public Department(){};
    public  Department(JSONObject jsonObject) throws JSONException {
        this.id = jsonObject.getInt("ID");
        this.name = jsonObject.getString("Name");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
