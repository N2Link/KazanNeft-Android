package com.example.kazan.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Well {
    int id, wellTypeID, gasOilDepth;
    long capacity;
    String wellName;
    public  WellType WellType;
    public Well(){};
    public Well(JSONObject jsonObject ) throws JSONException {
        this.id = jsonObject.getInt("ID");
        this.wellTypeID = jsonObject.getInt("WellTypeID");
        this.gasOilDepth = jsonObject.getInt("GasOilDepth");
        this.capacity = jsonObject.getLong("Capacity");
        this.wellName = jsonObject.getString("WellName");
        this.WellType = new WellType(jsonObject.getJSONObject("WellType"));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWellTypeID() {
        return wellTypeID;
    }

    public void setWellTypeID(int wellTypeID) {
        this.wellTypeID = wellTypeID;
    }

    public int getGasOilDepth() {
        return gasOilDepth;
    }

    public void setGasOilDepth(int gasOilDepth) {
        this.gasOilDepth = gasOilDepth;
    }

    public long getCapacity() {
        return capacity;
    }

    public void setCapacity(long capacity) {
        this.capacity = capacity;
    }

    public String getWellName() {
        return wellName;
    }

    public void setWellName(String wellName) {
        this.wellName = wellName;
    }
}
