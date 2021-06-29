package com.example.kazan.model;

import org.json.JSONException;
import org.json.JSONObject;

public class RockType {
    int id;
    String name, backgroundColor;
    public RockType(){};
    public RockType(JSONObject jsonObject ) throws JSONException {
        this.id = jsonObject.getInt("ID");
        this.name = jsonObject.getString("Name");
        this.backgroundColor = jsonObject.getString("BackgroundColor");
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

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
}
