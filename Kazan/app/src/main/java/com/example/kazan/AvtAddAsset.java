package com.example.kazan;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.JsonWriter;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.kazan.model.Asset;
import com.example.kazan.model.Department;
import com.example.kazan.model.DepartmentLocation;
import com.example.kazan.model.Employee;
import com.example.kazan.model.Location;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.ref.ReferenceQueue;
import java.util.ArrayList;
import java.util.Calendar;

public class AvtAddAsset extends AppCompatActivity implements IOTask{

    EditText edAssetName, dtpkWarranty, edDescription;
    Spinner spnDM, spnAG, spnLoc,spnEm;
    TextView tvAssetSN;
    Button btnSubmit, btnCancel, btnBack;
    ArrayList<Department> departments = new ArrayList<>();
    ArrayList<Employee> employees = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avt_add_asset);
        findView();
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
        setSpnDM();
        setSpnAG();
        setSpnEm();
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
                setSpnLoc(locations);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spnAG.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                setAssetSN();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
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
                for (Asset asset: AvtAssetManagement.assets) {
                    if(asset.getAssetName().equals(editable.toString())&&spnLoc.getSelectedItemId()==asset.DepartmentLocation.getLocationID()){
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
                AvtAddAsset.super.onBackPressed();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AvtAddAsset.super.onBackPressed();
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitNewAsset();
            }
        });
        dtpkWarranty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int mDate = calendar.get(Calendar.DATE);
                int mMonth = calendar.get(Calendar.MONTH);
                int mYear = calendar.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog = new DatePickerDialog(AvtAddAsset.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        dtpkWarranty.setText(String.format("%04d",i)+"-"+String.format("%02d",i1+1)+"-"+String.format("%02d",i2));
                    }
                },mYear, mMonth, mDate);
                datePickerDialog.show();
            }
        });
    }

    private void submitNewAsset() {
        Asset asset = new Asset();
        if (dtpkWarranty.getText().toString().equals("")) {
            asset.setWarrantyDate(null);
        } else {
            asset.setWarrantyDate(dtpkWarranty.getText().toString());
        }
        asset.setAssetSN(tvAssetSN.getText().toString());
        asset.setAssetName(edAssetName.getText().toString());
        asset.setDescription(edDescription.getText().toString());
        asset.setEmployeeID((int) spnEm.getSelectedItemId());
        for (DepartmentLocation item : AvtAssetManagement.departmentLocations) {
            if (item.getDepartmentID() == spnDM.getSelectedItemId() && item.getLocationID() == spnLoc.getSelectedItemId()) {
                asset.setDepartmentLocationID(item.getId());
                break;
            }
        }
        asset.setAssetGroupID((int) spnAG.getSelectedItemId());


        StringWriter writer = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(writer);
        try {
            jsonWriter.beginObject();
            jsonWriter.name("AssetSN").value(asset.getAssetSN());
            jsonWriter.name("AssetName").value(asset.getAssetName());
            jsonWriter.name("DepartmentLocationID").value(asset.getDepartmentLocationID());
            jsonWriter.name("EmployeeID").value(asset.getEmployeeID());
            jsonWriter.name("AssetGroupID").value(asset.getAssetGroupID());
            jsonWriter.name("Description").value(asset.getDescription());
            jsonWriter.name("WarrantyDate").value(asset.getWarrantyDate());
            jsonWriter.endObject();
            String jsonStr = writer.toString();
            Log.d("JsonStr", jsonStr);
            RequestAPI requestAPI = new RequestAPI(AvtAddAsset.this, 100, "assets", "POST", jsonStr);
            requestAPI.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

        private void setAssetSN() {
        try {
            String assetSN = String.format("%02d", spnDM.getSelectedItemId())+"/"+String.format("%02d", spnAG.getSelectedItemId())+"/";
            for (int i=AvtAssetManagement.assets.size()-1; i>=0; i--){
                if(AvtAssetManagement.assets.get(i).getAssetSN().contains(assetSN)){
                    int n = Integer.parseInt(AvtAssetManagement.assets.get(i).getAssetSN().substring(6))+1;
                    assetSN+=String.format("%04d",n);
                    tvAssetSN.setText(assetSN);
                    return;
                }
            }
            assetSN+="0001";
            tvAssetSN.setText(assetSN);
        }catch (Exception ex){}
    }

    private void setSpnLoc(final ArrayList<Location> locations) {
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

    private void setSpnEm() {
        for (Employee item: MainActivity.employees ) {
            Log.d("isAdmin", item.isAdmin()+"adsasdasdasd");

            if(!item.isAdmin()){
                employees.add(item);
            }
        }
        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), R.layout.spinner_item, employees){
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
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_border_blue);

        spnEm.setAdapter(adapter);
    }

    private void setSpnAG() {
        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), R.layout.spinner_item, AvtAssetManagement.assetGroups){
            @Nullable
            @Override
            public Object getItem(int position) {
                return AvtAssetManagement.assetGroups.get(position).getName();
            }

            @Override
            public long getItemId(int position) {
                return AvtAssetManagement.assetGroups.get(position).getId();
            }
        };
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_border_blue);

        spnAG.setAdapter(adapter);
    }

    private void setSpnDM() {
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
        tvAssetSN = findViewById(R.id.tv_asset_sn);
        edAssetName = findViewById(R.id.ed_asset_name);
        spnAG = findViewById(R.id.spn_ag);
        spnDM = findViewById(R.id.spn_department);
        spnEm = findViewById(R.id.spn_employee);
        spnLoc = findViewById(R.id.spn_location);
        btnSubmit = findViewById(R.id.btn_submit);
        btnCancel = findViewById(R.id.btn_cancel);
        dtpkWarranty = findViewById(R.id.warranty);
        edDescription = findViewById(R.id.ed_description);
        btnBack = findViewById(R.id.btn_back);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_back_enter, R.anim.anim_back_exit);

    }


    @Override
    public void doIt(int requestcode, String jsonData, int resultCode) {
        Log.d("ResponeCode", requestcode+"   "+resultCode);
        if(requestcode==100 && resultCode==201){
            onBackPressed();
        }
    }
}