package com.example.kazan;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.JsonWriter;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kazan.model.Asset;
import com.example.kazan.model.AssetTransferLog;
import com.example.kazan.model.Department;
import com.example.kazan.model.DepartmentLocation;
import com.example.kazan.model.Location;

import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AvtAssetTranfer extends AppCompatActivity implements IOTask{
    Asset asset;
    TextView tvAssetName, tvCurDm, tvCurSN, tvNewSn;
    Spinner spnDM, spnLoc;
    Button btnBack, btnSubmit, btnCancel;
    private ArrayList<Department> departments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avt_asset_tranfer);
        Intent intent = getIntent();
        Integer id = intent.getExtras().getInt("AssetID");
        for (Asset item: AvtAssetManagement.assets) {
            if(item.getId()==id){
                asset= item;
            }
        }
        departments = AvtAssetManagement.departments;
        for (Department department: departments) {
            int count = 0;
            for (DepartmentLocation departmentLocation: AvtAssetManagement.departmentLocations) {
                if(departmentLocation.getDepartmentID()==department.getId()&&departmentLocation.getEndDate()=="null"){
                    count+=1;
                    break;
                }
            }
            if(count==0){
                departments.remove(department);
            }
        }
        findView();
        setData();
        spnDM.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                setAssetSN();
                ArrayList<Location> locations = new ArrayList<>();
                for (DepartmentLocation departmentLocation: AvtAssetManagement.departmentLocations) {
                    if(departmentLocation.getDepartmentID()==spnDM.getSelectedItemId()&&departmentLocation.getEndDate()=="null"){
                        locations.add(departmentLocation.Location);
                    }
                }
                setSpnNewLoc(locations);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AvtAssetTranfer.super.onBackPressed();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AvtAssetTranfer.super.onBackPressed();
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();
            }
        });
    }

    private void submit() {
        AssetTransferLog assetTransferLog= new AssetTransferLog();
        assetTransferLog.setAssetID(asset.getId());
        assetTransferLog.setFromAssetSN(asset.getAssetSN());
        assetTransferLog.setFromDepartmentLocationID(asset.getDepartmentLocationID());
        assetTransferLog.setFromDMName(asset.DepartmentLocation.Department.getName());
        assetTransferLog.setToDMName(spnDM.getSelectedItem().toString());
        assetTransferLog.setToAssetSN(tvNewSn.getText().toString());
        assetTransferLog.setTransferDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis())));

        for (DepartmentLocation departmentLocation :AvtAssetManagement.departmentLocations) {
            if(departmentLocation.getLocationID()==spnLoc.getSelectedItemId()&&
            departmentLocation.getDepartmentID()==spnDM.getSelectedItemId()&&
            departmentLocation.getEndDate().equals("null")){
                assetTransferLog.setToDepartmentLocationID(departmentLocation.getId());
                asset.setDepartmentLocationID(departmentLocation.getId());
                break;
            }
        }
        asset.setAssetSN(tvNewSn.getText().toString());

        StringWriter writer = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(writer);
        try {
            jsonWriter.beginObject();
            jsonWriter.name("AssetID").value(assetTransferLog.getAssetID());
            jsonWriter.name("TransferDate").value(assetTransferLog.getTransferDate());
            jsonWriter.name("FromAssetSN").value(assetTransferLog.getFromAssetSN());
            jsonWriter.name("FromDepartmentLocationID").value(assetTransferLog.getFromDepartmentLocationID());
            jsonWriter.name("ToAssetSN").value(assetTransferLog.getToAssetSN());
            jsonWriter.name("ToDepartmentLocationID").value(assetTransferLog.getToDepartmentLocationID());
            jsonWriter.endObject();
            String jsonStr = writer.toString();
            Log.d("JsonStr", jsonStr);
            RequestAPI requestAPI = new RequestAPI(AvtAssetTranfer.this, 200, "assettransferlogs", "POST", jsonStr);
            requestAPI.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        writer = new StringWriter();
        jsonWriter = new JsonWriter(writer);
        try {
            jsonWriter.beginObject();
            jsonWriter.name("ID").value(asset.getId());
            jsonWriter.name("AssetSN").value(asset.getAssetSN());
            jsonWriter.name("AssetName").value(asset.getAssetName());
            jsonWriter.name("DepartmentLocationID").value(asset.getDepartmentLocationID());
            jsonWriter.name("EmployeeID").value(asset.getEmployeeID());
            jsonWriter.name("AssetGroupID").value(asset.getAssetGroupID());
            jsonWriter.name("Description").value(asset.getDescription());
            if(asset.getWarrantyDate().equals("null")){
                jsonWriter.name("WarrantyDate").value("");
            }else{
                jsonWriter.name("WarrantyDate").value(asset.getWarrantyDate());
            }
            jsonWriter.endObject();
            String jsonStr = writer.toString();
            RequestAPI requestAPI = new RequestAPI(AvtAssetTranfer.this, 100, "assets/"+asset.getId(), "PUT", jsonStr);
            Toast.makeText(getApplicationContext(),asset.getId()+"", Toast.LENGTH_SHORT).show();
            Log.d("Jsonstr", jsonStr);
            requestAPI.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void setAssetSN() {
        try {
            String assetSN = String.format("%02d", spnDM.getSelectedItemId())+"/"+String.format("%02d", asset.getAssetGroupID())+"/";
            for (int i=AvtAssetManagement.assets.size()-1; i>=0; i--){
                if(AvtAssetManagement.assets.get(i).getAssetSN().contains(assetSN)){
                    int n = Integer.parseInt(AvtAssetManagement.assets.get(i).getAssetSN().substring(6))+1;
                    assetSN+=String.format("%04d",n);
                    tvNewSn.setText(assetSN);
                    return;
                }
            }
            assetSN+="0001";
            tvNewSn.setText(assetSN);
        }catch (Exception ex){}
    }

    private void setData() {
        tvAssetName.setText(asset.getAssetName());
        tvCurSN.setText(asset.getAssetSN());
        tvCurDm.setText(asset.DepartmentLocation.Department.getName());
        setSpnNewDM();

    }

    private void setSpnNewLoc(final ArrayList<Location> locations) {
        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), R.layout.spinner_item, locations){
            @Nullable
            @Override
            public Object getItem(int position) {
                return locations.get(position).getName();
            }

            @Override
            public long getItemId(int position) {
                return locations.get(position).getId();
            }
        };
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_border_blue);

        spnLoc.setAdapter(adapter);
    }

    private void setSpnNewDM() {
        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), R.layout.spinner_item, departments){
            @Nullable
            @Override
            public Object getItem(int position) {
                return departments.get(position).getName();
            }

            @Override
            public long getItemId(int position) {
                return departments.get(position).getId();
            }
        };
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_border_blue);

        spnDM.setAdapter(adapter);
    }

    private void findView() {
        tvAssetName = findViewById(R.id.tv_asset_name);
        tvCurDm = findViewById(R.id.tv_department);
        tvCurSN = findViewById(R.id.tv_assetsn_transfer);
        tvNewSn = findViewById(R.id.tv_new_asset_sn);
        spnDM = findViewById(R.id.spn_dm_new);
        spnLoc = findViewById(R.id.spn_location_new);
        btnBack = findViewById(R.id.btn_back_transfer);
        btnSubmit = findViewById(R.id.btn_submit_transfer);
        btnCancel = findViewById(R.id.btn_cancel_transfer);
    }

    static boolean isPost = false, isPut = false;
    String checkUpadte(){
        if(isPost&&isPut){
            super.onBackPressed();
        }
        return isPost +"  "+isPut;
    }
    @Override
    public void doIt(int requestcode, String jsonData, int resultCode) {
        Log.d("Check update",requestcode+"     "+resultCode+" "+jsonData);

        if(requestcode==200&&resultCode==201){
            isPost = true;
            checkUpadte();
            Log.d("Check update",requestcode+checkUpadte()+resultCode);
        }
        if(requestcode==100&&resultCode==204){
            isPut = true;
            checkUpadte();
            Log.d("Check update",requestcode+checkUpadte()+resultCode);

        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_back_enter, R.anim.anim_back_exit);

    }
}