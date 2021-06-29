package com.example.kazan;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.kazan.model.Asset;
import com.example.kazan.model.AssetGroup;
import com.example.kazan.model.AssetTransferLog;
import com.example.kazan.model.Department;
import com.example.kazan.model.DepartmentLocation;
import com.example.kazan.model.Location;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AvtAssetManagement extends AppCompatActivity implements IOTask {

    Spinner spnAG,spnDM;
    EditText dtpkStart, dtpkEnd, edSearch;
    RecyclerView recListAsset;
    public static ArrayList<Asset> assets = new ArrayList<>();
    public static ArrayList<AssetGroup> assetGroups = new ArrayList<>();
    public static ArrayList<Department> departments = new ArrayList<>();
    public static ArrayList<Location> locations = new ArrayList<>();
    public static ArrayList<AssetTransferLog> assetTransferLogs = new ArrayList<>();
    public static ArrayList<DepartmentLocation> departmentLocations = new ArrayList<>();

    @Override
    protected void onRestart() {
        super.onResume();
        assets = new ArrayList<>();
        assetGroups = new ArrayList<>();
        departments = new ArrayList<>();
        locations = new ArrayList<>();
        assetTransferLogs = new ArrayList<>();
        departmentLocations = new ArrayList<>();

        showAsset(assets);
        spnDM.setAdapter(new ArrayAdapter<Department>(getApplicationContext(), R.layout.spinner_item, departments));
        spnAG.setAdapter(new ArrayAdapter<AssetGroup>(getApplicationContext(), R.layout.spinner_item, assetGroups));
        RequestGetData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avt_asset_management);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AvtAssetManagement.this, AvtAddAsset.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);

            }
        });



        findView();
        RequestGetData();
        dtpkStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int mDay = calendar.get(Calendar.DATE);
                int mMonth = calendar.get(Calendar.MONTH);
                int mYear = calendar.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog = new DatePickerDialog(AvtAssetManagement.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        dtpkStart.setText(year+"-"+String.format("%02d", (month+1))+"-"+String.format("%02d",dayOfMonth));
                    }
                }, mYear, mMonth,mDay);
                datePickerDialog.show();
            }
        });
        dtpkEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int mDay = calendar.get(Calendar.DATE);
                int mMonth = calendar.get(Calendar.MONTH);
                int mYear = calendar.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog = new DatePickerDialog(AvtAssetManagement.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        dtpkEnd.setText(year+"-"+String.format("%02d", (month+1))+"-"+String.format("%02d",dayOfMonth));
                    }
                }, mYear, mMonth,mDay);
                datePickerDialog.show();
            }
        });
        edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                search();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        dtpkStart.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                search();
            }
        });
        dtpkEnd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                search();
            }
        });
        spnDM.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                search();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spnAG.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                search();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void RequestGetData() {
        RequestAPI requestAssets = new RequestAPI(AvtAssetManagement.this, 1,"assets", "GET","");
        RequestAPI requestAG = new RequestAPI(AvtAssetManagement.this, 2,"assetgroups", "GET","");
        RequestAPI requestDM = new RequestAPI(AvtAssetManagement.this, 3,"departments", "GET","");
        RequestAPI requestDML = new RequestAPI(AvtAssetManagement.this, 4,"departmentlocations", "GET","");
        RequestAPI requestLoc = new RequestAPI(AvtAssetManagement.this, 5,"locations", "GET","");
        RequestAPI requestTrans = new RequestAPI(AvtAssetManagement.this, 6,"assettransferlogs", "GET","");
        requestAG.execute();
        requestAssets.execute();
        requestDM.execute();
        requestDML.execute();
        requestLoc.execute();
        requestTrans.execute();
    }

    private void findView() {
        spnAG = findViewById(R.id.spn_ag);
        spnDM = findViewById(R.id.spn_dm);
        dtpkStart = findViewById(R.id.ed_dtpk_start);
        dtpkEnd = findViewById(R.id.ed_dtpk_end);
        edSearch = findViewById(R.id.ed_search);
        recListAsset = findViewById(R.id.rec);
    }

    @Override
    public void doIt(int requestcode, String jsonData, int resultCode) {
        switch (requestcode){
            case 1:
                try {
                    getAssetList(jsonData);
                    showAsset(assets);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                try {
                    getAssetGroups(jsonData);
                    setSpnAG();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case 3:
                try {
                    getDepartments(jsonData);
                    setSpnDm();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case 4:
                try {
                    getDMLs(jsonData);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case 5:
                try {
                    getLocs(jsonData);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case 6:
                try {
                    getLogs(jsonData);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }

    }

    private void getLogs(String jsonData) throws JSONException {
        JSONArray jsonArray = new JSONArray(jsonData);
        for (int i = 0; i<jsonArray.length();i++){
            assetTransferLogs.add(new AssetTransferLog(jsonArray.getJSONObject(i)));
        }
    }

    private void getLocs(String jsonData) throws JSONException {
        JSONArray jsonArray = new JSONArray(jsonData);
        for (int i = 0; i<jsonArray.length();i++){
            locations.add(new Location(jsonArray.getJSONObject(i)));
        }
    }

    private void search() {
        if(assets.size()==0){
            return;
        }
        try{
            Date startDate = convertDate(dtpkStart.getText().toString());
            Date endDate = convertDate((String) dtpkEnd.getText().toString());

            if(startDate==null){
                startDate = new Date(Long.MIN_VALUE);
            }
            if (endDate==null){
                endDate = new Date(Long.MAX_VALUE);
            }
            ArrayList<Asset> list = new ArrayList<>();

            for (int i=0; i<assets.size(); i++){
                if(assets.get(i).getAssetSN().contains(edSearch.getText())||assets.get(i).getAssetName().toLowerCase().contains(edSearch.getText().toString().toLowerCase())){
                    if(assets.get(i).AssetGroup.getName().contains(spnAG.getSelectedItem().toString())&&
                            assets.get(i).DepartmentLocation.Department.getName().contains(spnDM.getSelectedItem().toString())){
                        if(assets.get(i).getWarrantyDate()=="null"){
                            list.add(assets.get(i));
                        }else {
                            Date date = convertDate((String) assets.get(i).getWarrantyDate());
                            if(startDate.compareTo(date)<=0&&endDate.compareTo(date)>=0){
                                Log.d("Date", new SimpleDateFormat("yyyy-MM-dd").format(startDate)+"   "+new SimpleDateFormat("yyyy-MM-dd").format(endDate));
                                list.add(assets.get(i));
                            }
                        }
                    }
                }
            }
            showAsset(list);
        }catch (Exception e){};
    }
    private void showAsset(ArrayList<Asset> list) {
        AssetAdapter assetAdapter = new AssetAdapter(getApplicationContext(),list);
        recListAsset.setLayoutManager(new LinearLayoutManager(AvtAssetManagement.this));
        recListAsset.setAdapter(assetAdapter);
    }
    Date convertDate(String strDate){
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = simpleDateFormat.parse(strDate);
        }catch (Exception e){
            date = null;
        };
        return date;
    }

    private void setSpnDm() {
        ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(), R.layout.spinner_item, departments){
            @Nullable
            @Override
            public Object getItem(int position) {
                return departments.get(position).getName();
            }
        };
        arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_border_blue);

        spnDM.setAdapter(arrayAdapter);
    }

    private void setSpnAG() {
        ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(), R.layout.spinner_item, assetGroups){
            @Nullable
            @Override
            public Object getItem(int position) {
                return assetGroups.get(position).getName();
            }

        };
        arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_border_blue);
        spnAG.setAdapter(arrayAdapter);
    }

    private void getDMLs(String jsonData) throws JSONException {
        JSONArray jsonArray = new JSONArray(jsonData);
        for (int i = 0; i<jsonArray.length();i++){
            departmentLocations.add(new DepartmentLocation(jsonArray.getJSONObject(i)));
        }
    }

    private void getDepartments(String jsonData) throws JSONException {
        JSONArray jsonArray = new JSONArray(jsonData);
        for (int i = 0; i<jsonArray.length();i++){
            departments.add(new Department(jsonArray.getJSONObject(i)));
        }
    }

    private void getAssetList(String jsonData) throws JSONException {
        JSONArray jsonArray = new JSONArray(jsonData);
        for (int i = 0; i<jsonArray.length();i++){
            assets.add(new Asset(jsonArray.getJSONObject(i)));
        }
    }

    private void getAssetGroups(String jsonData) throws JSONException {
        JSONArray jsonArray = new JSONArray(jsonData);
        for (int i = 0; i<jsonArray.length();i++){
            assetGroups.add(new AssetGroup(jsonArray.getJSONObject(i)));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_back_enter, R.anim.anim_back_exit);

    }

    public class AssetAdapter extends RecyclerView.Adapter<AssetAdapter.ViewHolder>{
        ArrayList<Asset> massets;
        Context mcontext;
        public AssetAdapter(Context mcontext,ArrayList<Asset> massets){
            this.mcontext = mcontext;
            this.massets = massets;
        }

        @NonNull
        @Override
        public AssetAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater =LayoutInflater.from(mcontext);
            View view = inflater.inflate(R.layout.list_asset, parent, false);
            AssetAdapter.ViewHolder viewHolder = new AssetAdapter.ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull AssetAdapter.ViewHolder holder, int position) {
            final Asset asset = massets.get(position);
            //Toast.makeText(null,asset.getAssetName(), Toast.LENGTH_LONG);

            if(position<0){
                Toast.makeText(null,asset.getAssetName(), Toast.LENGTH_LONG);
                return;
            }
            holder.tvDMname.setText(asset.DepartmentLocation.Department.getName());
            holder.tvAssetSN.setText(asset.getAssetSN());
            holder.tvAssetName.setText(asset.getAssetName());
            holder.icEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AvtAssetManagement.this, AvtEditAsset.class);
                    intent.putExtra("AssetID", asset.getId());
                    startActivity(intent);
                    overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);

                }
            });
            holder.icMove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AvtAssetManagement.this, AvtAssetTranfer.class);
                    intent.putExtra("AssetID", asset.getId());
                    startActivity(intent);
                    overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);

                }
            });
            holder.icHistory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
/*                    for (int i = assetTransferLogs.size()-1; i>=0; i--){
                        if(assetTransferLogs.get(i).getAssetID()==asset.getId()){
                            try {
                                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(assetTransferLogs.get(i).getTransferDate());
                                long l = System.currentTimeMillis()-date.getTime();
                                if(l/1000 >= 31556952){
                                    Toast.makeText(getApplicationContext(), "This asset hasn't recent transfers in the last twelve months",Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            } catch (ParseException e) {}
                            break;
                        }
                    }*/
                    Intent intent = new Intent(AvtAssetManagement.this, AvtHistoryTransfer.class);
                    intent.putExtra("AssetID",asset.getId());
                    startActivity(intent);
                    overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);

                }
            });

        }

        @Override
        public int getItemCount() {
            return massets.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvAssetName, tvAssetSN, tvDMname;
            ImageView icEdit, icHistory, icMove;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tvDMname = itemView.findViewById(R.id.department_name);
                tvAssetName = itemView.findViewById(R.id.asset_name);
                tvAssetSN = itemView.findViewById(R.id.asset_sn);
                icEdit = itemView.findViewById(R.id.ic_edit);
                icHistory = itemView.findViewById(R.id.ic_history);
                icMove = itemView.findViewById(R.id.ic_move);
            }
        }
    }
}
