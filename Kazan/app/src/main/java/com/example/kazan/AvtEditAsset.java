package com.example.kazan;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.JsonWriter;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.kazan.model.Asset;
import com.example.kazan.model.Employee;

import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AvtEditAsset extends AppCompatActivity  implements IOTask {

    EditText edAssetName, dtpkWarranty, edDescription;
    Spinner spnEm;
    TextView tvAssetSN, tvDM, tvAG, tvLoc;
    Button btnSubmit, btnCancel, btnBack;
    int assetID;
    public Asset asset;
    ArrayList<Employee> employees = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avt_edit_asset);
        Intent  intent = getIntent();
        assetID =  intent.getExtras().getInt("AssetID");
        for (Asset item: AvtAssetManagement.assets) {
            if(item.getId()==assetID){
                asset = item;
                break;
            }
        }
        findView();
        setData();
        edAssetName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                btnSubmit.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                for (Asset item: AvtAssetManagement.assets) {
                    if(item.getAssetName().equals(editable.toString())&&item.DepartmentLocation.getLocationID()==asset.DepartmentLocation.getLocationID()){
                        edAssetName.setError("In location, this asset name is exist , please fill another name!");
                        btnSubmit.setVisibility(View.INVISIBLE);
                        return;
                    }
                }
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AvtEditAsset.super.onBackPressed();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AvtEditAsset.super.onBackPressed();
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateAsset();
            }
        });
        dtpkWarranty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int mDate = calendar.get(Calendar.DATE);
                int mMonth = calendar.get(Calendar.MONTH);
                int mYear = calendar.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog = new DatePickerDialog(AvtEditAsset.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        dtpkWarranty.setText(String.format("%04d",i)+"-"+String.format("%02d",i1+1)+"-"+String.format("%02d",i2));
                    }
                },mYear, mMonth, mDate);
                datePickerDialog.show();
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_back_enter, R.anim.anim_back_exit);

    }
    private void updateAsset() {
        asset.setAssetName(edAssetName.getText().toString());
        asset.setEmployeeID((int) spnEm.getSelectedItemId());
        if (dtpkWarranty.getText().toString().equals("")) {
            asset.setWarrantyDate(null);
        } else {
            asset.setWarrantyDate(dtpkWarranty.getText().toString());
        }
        asset.setDescription(edDescription.getText().toString());
        StringWriter writer = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(writer);
        try {
            jsonWriter.beginObject();
            jsonWriter.name("ID").value(asset.getId());
            jsonWriter.name("AssetSN").value(asset.getAssetSN());
            jsonWriter.name("AssetName").value(asset.getAssetName());
            jsonWriter.name("DepartmentLocationID").value(asset.getDepartmentLocationID());
            jsonWriter.name("EmployeeID").value(asset.getEmployeeID());
            jsonWriter.name("AssetGroupID").value(asset.getAssetGroupID());
            jsonWriter.name("Description").value(asset.getDescription());
            jsonWriter.name("WarrantyDate").value(asset.getWarrantyDate());
            jsonWriter.endObject();
            String jsonStr = writer.toString();
            Log.d("Jsonstr", jsonStr);
            RequestAPI requestAPI = new RequestAPI(AvtEditAsset.this, 100, "assets/"+assetID, "PUT", jsonStr);
            requestAPI.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setData() {
        edAssetName.setText(asset.getAssetName());
        tvLoc.setText(asset.DepartmentLocation.Location.getName());
        tvDM.setText(asset.DepartmentLocation.Department.getName());
        tvAG.setText(asset.AssetGroup.getName());

        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(asset.getWarrantyDate());
            dtpkWarranty.setText(new SimpleDateFormat("yyyy-MM-dd").format(date));
        }catch (Exception ex){}
        tvAssetSN.setText(asset.getAssetSN());
        edDescription.setText(asset.getDescription());
        setSpnEM();
    }

    private void setSpnEM() {
        for (Employee item: MainActivity.employees) {
            if(!item.isAdmin()){
                employees.add(item);
            }
        }
        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), R.layout.spinner_item_red, employees){
            @Nullable
            @Override
            public Object getItem(int position) {
                return employees.get(position).getFirstName()+" "+employees.get(position).getLastName();
            }
            @Override
            public long getItemId(int position) {
                return employees.get(position).getId();
            }
        };
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_border_red);

        spnEm.setAdapter(adapter);
    }

    private void findView() {
        tvAssetSN = findViewById(R.id.tv_asset_sn);
        edAssetName = findViewById(R.id.ed_asset_name);
        tvAG = findViewById(R.id.tv_asset_group);
        tvDM = findViewById(R.id.tv_department);
        spnEm = findViewById(R.id.spn_employee);
        tvLoc = findViewById(R.id.tv_location);
        btnSubmit = findViewById(R.id.btn_submit);
        btnCancel = findViewById(R.id.btn_cancel);
        dtpkWarranty = findViewById(R.id.warranty);
        edDescription = findViewById(R.id.ed_description);
        btnBack = findViewById(R.id.btn_back);
    }

    @Override
    public void doIt(int requestcode, String jsonData, int resultCode) {
        Log.d("Resultcode", resultCode+"  ");
        if(requestcode==100&&resultCode==204){
            onBackPressed();
        }
    }
}