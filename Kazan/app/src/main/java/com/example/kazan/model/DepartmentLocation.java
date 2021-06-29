package com.example.kazan.model;

import org.json.JSONException;
import org.json.JSONObject;

public class DepartmentLocation {
    int id, departmentID, locationID;
    String startDate, endDate;
    public Department Department;
    public Location Location;
    public DepartmentLocation(){}

    public DepartmentLocation(JSONObject jsonObject) throws JSONException {
        this.id = jsonObject.getInt("ID");
        this.departmentID = jsonObject.getInt("DepartmentID");
        this.locationID = jsonObject.getInt("LocationID");
        this.startDate = jsonObject.getString("StartDate");
        this.endDate = jsonObject.getString("EndDate");
        this.Department = new Department(jsonObject.getJSONObject("Department"));
        this.Location = new Location(jsonObject.getJSONObject("Location"));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDepartmentID() {
        return departmentID;
    }

    public void setDepartmentID(int departmentID) {
        this.departmentID = departmentID;
    }

    public int getLocationID() {
        return locationID;
    }

    public void setLocationID(int locationID) {
        this.locationID = locationID;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
