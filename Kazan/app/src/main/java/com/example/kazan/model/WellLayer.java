package com.example.kazan.model;

import org.json.JSONException;
import org.json.JSONObject;

public class WellLayer {
    int id, wellID, rockTypeID, startPoint, endPoint;
    public RockType RockType;
    public  Well Well;
    public  WellLayer(){};
    public WellLayer(JSONObject jsonObject) throws JSONException {
        this.id = jsonObject.getInt("ID");
        this.wellID = jsonObject.getInt("WellID");
        this.rockTypeID = jsonObject.getInt("RockTypeID");
        this.startPoint = jsonObject.getInt("StartPoint");
        this.endPoint = jsonObject.getInt("EndPoint");
        this.RockType = new RockType(jsonObject.getJSONObject("RockType"));
        this.Well = new Well(jsonObject.getJSONObject("Well"));

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWellID() {
        return wellID;
    }

    public void setWellID(int wellID) {
        this.wellID = wellID;
    }

    public int getRockTypeID() {
        return rockTypeID;
    }

    public void setRockTypeID(int rockTypeID) {
        this.rockTypeID = rockTypeID;
    }

    public int getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(int startPoint) {
        this.startPoint = startPoint;
    }

    public int getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(int endPoint) {
        this.endPoint = endPoint;
    }
}
