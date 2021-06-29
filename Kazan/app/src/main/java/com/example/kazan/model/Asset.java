package com.example.kazan.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Asset {
    int id,employeeID, assetGroupID, departmentLocationID;
    String assetSN, assetName, description, warrantyDate;
    public Employee Employee;
    public AssetGroup AssetGroup;
    public DepartmentLocation DepartmentLocation;
    public Asset(){};
    public  Asset(JSONObject jsonObject) throws JSONException {
        this.id = jsonObject.getInt("ID");
        this.employeeID = jsonObject.getInt("EmployeeID");
        this.assetGroupID = jsonObject.getInt("AssetGroupID");
        this.departmentLocationID = jsonObject.getInt("DepartmentLocationID");
        this.assetSN = jsonObject.getString("AssetSN");
        this.assetName = jsonObject.getString("AssetName");
        this.description = jsonObject.getString("Description");
        this.warrantyDate = jsonObject.getString("WarrantyDate");
        this.Employee = new Employee(jsonObject.getJSONObject("Employee"));
        this.AssetGroup = new AssetGroup(jsonObject.getJSONObject("AssetGroup"));
        this.DepartmentLocation = new DepartmentLocation(jsonObject.getJSONObject("DepartmentLocation"));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    public int getAssetGroupID() {
        return assetGroupID;
    }

    public void setAssetGroupID(int assetGroupID) {
        this.assetGroupID = assetGroupID;
    }

    public int getDepartmentLocationID() {
        return departmentLocationID;
    }

    public void setDepartmentLocationID(int departmentLocationID) {
        this.departmentLocationID = departmentLocationID;
    }

    public String getAssetSN() {
        return assetSN;
    }

    public void setAssetSN(String assetSN) {
        this.assetSN = assetSN;
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWarrantyDate() {
        return warrantyDate;
    }

    public void setWarrantyDate(String warrantyDate) {
        this.warrantyDate = warrantyDate;
    }
}
