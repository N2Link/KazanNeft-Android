package com.example.kazan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kazan.model.RockType;
import com.example.kazan.model.Well;
import com.example.kazan.model.WellLayer;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class AvtWellManager extends AppCompatActivity implements IOTask{
    RecyclerView recLayer;
    Spinner spnWellName;
    TextView tvCapacity,tvDepth;
    Button btnEdit;
    FloatingActionButton fab;
    static public ArrayList<Well> wells = new ArrayList<>();
    static public  ArrayList<WellLayer> wellLayers = new ArrayList<>();
    private static final int GET_WELL = 1, GET_WELL_LAYER =2;

    @Override
    protected void onRestart() {
        super.onRestart();
        wells = new ArrayList<>();
        wellLayers = new ArrayList<>();
        spnWellName.setAdapter(new ArrayAdapter<Well>(getApplicationContext(),android.R.layout.simple_spinner_item, wells));
        getData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avt_well_manager);
        findView();
        getData();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AvtWellManager.this, AvtAddWell.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);

            }
        });
        //thread.start();
        spnWellName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                showListLayer();
                tvCapacity.setText(wells.get(position).getCapacity()+"");
                tvDepth.setText(wells.get(position).getGasOilDepth()+"");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AvtWellManager.this, AvtEditWell.class);
                intent.putExtra("WellID", spnWellName.getSelectedItemId());
                Log.d("WellID", spnWellName.getSelectedItemId()+"");
                startActivity(intent);
                overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);

            }
        });
    }
    public  void writeToFile(String fileName,String json) throws IOException {
        File file = new File(AvtWellManager.this.getExternalFilesDir(null).getAbsolutePath()+"/"+fileName);
        if(!file.exists()){
            file.createNewFile();
        }else{
            file.delete();
            file.createNewFile();
        }

        FileOutputStream fileOutputStream = new FileOutputStream(file);
        byte[] bytes = json.getBytes();
        fileOutputStream.write(bytes, 0, bytes.length);
        fileOutputStream.close();
    }
    public  String readFile(String filename) throws IOException {
        File file = new File(AvtWellManager.this.getExternalFilesDir(null).getAbsolutePath() + "/" + filename);
        if (!file.exists()) {
            Toast.makeText(AvtWellManager.this, "Need wifi or 3g in first run app", Toast.LENGTH_LONG).show();
        }
        FileInputStream fileInputStream = new FileInputStream(file);
        return new BufferedReader(new InputStreamReader(fileInputStream)).readLine();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_back_enter, R.anim.anim_back_exit);

    }

    private void getData() {
        wellLayers.clear();
        wells.clear();
        RequestAPI apiWell = new RequestAPI(AvtWellManager.this, GET_WELL, "wells", "GET", "" );
        RequestAPI apiWellLayer = new RequestAPI(AvtWellManager.this, GET_WELL_LAYER, "welllayers", "GET", "" );
        apiWell.execute();
        apiWellLayer.execute();
    }

    private void findView() {
        recLayer = findViewById(R.id.rec_list_layer);
        spnWellName = findViewById(R.id.spn_well);
        tvCapacity  = findViewById(R.id.tv_capacity);
        btnEdit = findViewById(R.id.btn_edit);
        fab = findViewById(R.id.fab_add);
        tvDepth = findViewById(R.id.tv_depth);
    }

    @Override
    public void doIt(int requestcode, String jsonData, int resultcode) {

        if(requestcode==GET_WELL){
            try {
                getListWell(jsonData);
                setSpnWell();
                //writeToFile("wells.txt", jsonData);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(requestcode == GET_WELL_LAYER){
            try {
                getListLayer(jsonData);
                showListLayer();
                //writeToFile("welllayers.txt", jsonData);
            } catch (JSONException  e) {
                e.printStackTrace();
            }
        }
    }

    private void showListLayer() {
        ArrayList<WellLayer> list = new ArrayList<>();
        Well well = new Well();
        for (WellLayer item : wellLayers) {
            if(item.getWellID() == spnWellName.getSelectedItemId()){
                well=item.Well;
                list.add(item);
            }
        }

        try{
            RockType rockType = new RockType();
            rockType.setName("Gas/Oil");
            rockType.setBackgroundColor("#000000");

            WellLayer wellLayer = new WellLayer();
            wellLayer.setEndPoint(well.getGasOilDepth());
            wellLayer.setStartPoint(wellLayers.get(wellLayers.size()-1).getEndPoint());
            wellLayer.RockType = rockType;
            wellLayer.Well = well;
            list.add(wellLayer);
        }catch (Exception ex){}


        WellAdapter adapter = new WellAdapter(AvtWellManager.this, list);
        recLayer.setLayoutManager(new LinearLayoutManager(AvtWellManager.this));
        recLayer.setAdapter(adapter);

    }

    private void getListLayer(String jsonData) throws JSONException {
        JSONArray jsonArray = new JSONArray(jsonData);
        for(int i =0; i<jsonArray.length(); i++){
            wellLayers.add(new WellLayer(jsonArray.getJSONObject(i)));
        }
    }

    private void setSpnWell() {
        ArrayAdapter<Well> adapter = new ArrayAdapter(AvtWellManager.this, android.R.layout.simple_spinner_item, wells){
            @Override
            public long getItemId(int position) {
                return wells.get(position).getId();
            }

            @Nullable
            @Override
            public Object getItem(int position) {
                return wells.get(position).getWellName();
            }
        };

        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spnWellName.setAdapter(adapter);
    }


    private void getListWell(String jsonData) throws JSONException {
        JSONArray jsonArray = new JSONArray(jsonData);
        for (int i =0; i<jsonArray.length(); i++){
            wells.add(new Well(jsonArray.getJSONObject(i)));
        }
    }
    public boolean isOnline(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = connectivityManager.getActiveNetworkInfo();
        if(ni!= null && ni.isConnected()){
            return true;
        }
        return false;
    }

    public class WellAdapter extends  RecyclerView.Adapter<WellAdapter.ViewHolder>{
        Context mcontext;
        ArrayList<WellLayer> mList;
        public WellAdapter(Context mcontext, ArrayList<WellLayer> mList){
            this.mcontext = mcontext;
            this.mList = mList;
        }

        @NonNull
        @Override
        public WellAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(mcontext);
            View view = inflater.inflate(R.layout.list_well_layer, parent, false);
            WellAdapter.ViewHolder viewHolder = new WellAdapter.ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull WellAdapter.ViewHolder holder, int position) {
            WellLayer wellLayer = mList.get(position);
            ViewGroup.LayoutParams params = holder.tvWLName.getLayoutParams();
/*            if(position== mList.size()-1){
                holder.tvWLName.setText("Gas/oil");
                holder.tvWLName.setBackgroundColor(Color.BLACK);
                holder.tvStartPoint.setText(mList.get(position-1).getEndPoint()+"");
                holder.tvWLName.setTextColor(Color.WHITE);
                params.height = 100;
                return;
            }*/


            holder.tvWLName.setText(wellLayer.RockType.getName());
            holder.tvStartPoint.setText(wellLayer.getStartPoint()+"");
            holder.tvWLName.setBackgroundColor(Color.parseColor(wellLayer.RockType.getBackgroundColor()));
            int x = (wellLayer.Well.getGasOilDepth()/900);
            int xx = (wellLayer.getEndPoint()-wellLayer.getStartPoint());
            Log.d("Layer", x+"        "+xx+"       "+xx/x);
            if (position==mList.size()-1){
                holder.tvWLName.setTextColor(Color.WHITE);
            }
            params.height = xx/x;
            if(params.height<50){
                params.height=50;
            }
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvWLName, tvStartPoint;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tvWLName = itemView.findViewById(R.id.tv_well_layer);
                tvStartPoint = itemView.findViewById(R.id.tv_start_point);
            }
        }
    }
}