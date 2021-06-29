package com.example.kazan.model;

import org.json.JSONException;
import org.json.JSONObject;

public class AssetTransferLog {
    int id, assetID, fromDepartmentLocationID, toDepartmentLocationID;
    String fromAssetSN, toAssetSN, fromDMName, toDMName,transferDate;

    public String getTransferDate() {
        return transferDate;
    }

    public void setTransferDate(String transferDate) {
        this.transferDate = transferDate;
    }

    public AssetTransferLog() {
    }
    public AssetTransferLog(JSONObject jsonObject) throws JSONException {
        this.id = jsonObject.getInt("ID");
        this.assetID = jsonObject.getInt("AssetID");
        this.fromDepartmentLocationID = jsonObject.getInt("FromDepartmentLocationID");
        this.toDepartmentLocationID = jsonObject.getInt("ToDepartmentLocationID");
        this.fromAssetSN = jsonObject.getString("FromAssetSN");
        this.toAssetSN = jsonObject.getString("ToAssetSN");
        this.fromDMName = jsonObject.getJSONObject("DepartmentLocation").getJSONObject("Department").getString("Name");
        this.toDMName = jsonObject.getJSONObject("DepartmentLocation1").getJSONObject("Department").getString("Name");
        this.transferDate = jsonObject.getString("TransferDate");

    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAssetID() {
        return assetID;
    }

    public void setAssetID(int assetID) {
        this.assetID = assetID;
    }

    public int getFromDepartmentLocationID() {
        return fromDepartmentLocationID;
    }

    public void setFromDepartmentLocationID(int fromDepartmentLocationID) {
        this.fromDepartmentLocationID = fromDepartmentLocationID;
    }

    public int getToDepartmentLocationID() {
        return toDepartmentLocationID;
    }

    public void setToDepartmentLocationID(int toDepartmentLocationID) {
        this.toDepartmentLocationID = toDepartmentLocationID;
    }

    public String getFromAssetSN() {
        return fromAssetSN;
    }

    public void setFromAssetSN(String fromAssetSN) {
        this.fromAssetSN = fromAssetSN;
    }

    public String getToAssetSN() {
        return toAssetSN;
    }

    public void setToAssetSN(String toAssetSN) {
        this.toAssetSN = toAssetSN;
    }

    public String getFromDMName() {
        return fromDMName;
    }

    public void setFromDMName(String fromDMName) {
        this.fromDMName = fromDMName;
    }

    public String getToDMName() {
        return toDMName;
    }

    public void setToDMName(String toDMName) {
        this.toDMName = toDMName;
    }
}
