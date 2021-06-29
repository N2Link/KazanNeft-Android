package com.example.kazan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.JsonWriter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kazan.model.RockType;
import com.example.kazan.model.Well;
import com.example.kazan.model.WellLayer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;

public class AvtAddWell extends AppCompatActivity implements IOTask{

    EditText edWellName, edDepth, edCapacity, edEndPoint;
    Spinner spnRockType;
    RecyclerView recyclerView;
    TextView tvStartPoint;
    ArrayList<RockType> rockTypes = new ArrayList<>();
    Button btnAddLayer, btnSubmit;
    ArrayList<WellLayer> listAdd = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avt_add_well);
        findView();
        getData();
        btnAddLayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edEndPoint.getText().toString().equals("")){
                    edEndPoint.setError("fill it");
                    return;
                }
                if(Integer.parseInt(edEndPoint.getText().toString())-Integer.parseInt(tvStartPoint.getText().toString())<100){
                    edEndPoint.setError("not layer depth less than 100 point");
                    return;
                }
                for (RockType item: rockTypes) {
                    if(item.getId()==spnRockType.getSelectedItemId()){
                        WellLayer wellLayer = new WellLayer();
                        wellLayer.setEndPoint(Integer.parseInt(edEndPoint.getText().toString()));
                        wellLayer.setStartPoint(Integer.parseInt(tvStartPoint.getText().toString()));
                        wellLayer.setRockTypeID(item.getId());
                        wellLayer.RockType = item;
                        listAdd.add(wellLayer);
                        setRockAdapter(listAdd);
                        tvStartPoint.setText(edEndPoint.getText());
                        return;
                    }
                }
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edWellName.getText().toString().isEmpty()||
                        edCapacity.getText().toString().isEmpty()||
                edDepth.getText().toString().isEmpty()){
                    edWellName.setError("");
                    edCapacity.setError("");
                    edDepth.setError("");
                    return;
                }
                for (Well well: AvtWellManager.wells) {
                    if(well.getWellName().equals(edWellName.getText().toString())){
                        edWellName.setError("Well name is exist");
                        break;
                    }
                }
                if(edDepth.getText().equals("")){
                    edDepth.setError("fill it");
                    return;
                }
                if(edCapacity.getText().equals("")){
                    edCapacity.setError("fill it");
                    return;
                }

                if(Integer.parseInt(edDepth.getText().toString())<listAdd.get(listAdd.size()-1).getEndPoint()){
                    edDepth.setError("have a layer depth than this. Check again!");
                    return;
                }
                if(listAdd.size()==0){
                    Toast.makeText(getApplicationContext(), "Cant add none layer to well", Toast.LENGTH_LONG).show();
                    return;
                }
                try {
                    submit();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void submit() throws IOException {
        Well well    = new Well();
        well.setCapacity(Integer.parseInt(edCapacity.getText().toString()));
        well.setGasOilDepth(Integer.parseInt(edDepth.getText().toString()));
        well.setWellName(edWellName.getText().toString());
        well.setWellTypeID(1);
        well.setId(AvtWellManager.wells.size());
        StringWriter writer = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(writer);
        jsonWriter.beginObject();
        jsonWriter.name("ID").value(well.getId());
        jsonWriter.name("GasOilDepth").value(well.getGasOilDepth());
        jsonWriter.name("WellName").value(well.getWellName());
        jsonWriter.name("Capacity").value(well.getCapacity());
        jsonWriter.name("WellTypeID").value(well.getWellTypeID());
        jsonWriter.endObject();
        RequestAPI api = new RequestAPI(AvtAddWell.this, 101, "wells", "POST", writer.toString());
        api.execute();
    }

    private void setRockAdapter(ArrayList<WellLayer> listAdd) {
        this.listAdd = listAdd;
        RockAdapter rockAdapter = new RockAdapter(AvtAddWell.this,  listAdd);
        recyclerView.setLayoutManager(new LinearLayoutManager(AvtAddWell.this));
        recyclerView.setAdapter(rockAdapter);
    }


    private void getData() {
        RequestAPI getRockType = new RequestAPI(AvtAddWell.this, 300, "rocktypes", "GET", "");
        getRockType.execute();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_back_enter, R.anim.anim_back_exit);

    }
    private void findView() {
        edCapacity = findViewById(R.id.ed_capacity);
        edDepth = findViewById(R.id.ed_depth);
        edEndPoint = findViewById(R.id.ed_end_point);
        edWellName = findViewById(R.id.ed_well_name);
        spnRockType = findViewById(R.id.spn_rock_type);
        recyclerView = findViewById(R.id.recyclerView);
        tvStartPoint = findViewById(R.id.tv_start_point);
        btnAddLayer = findViewById(R.id.btn_add_layer);
        btnSubmit = findViewById(R.id.button_submit);
    }
    int count=0;
    @Override
    public void doIt(int requestcode, String jsonData, int resultcode) {
        Log.d("Check", requestcode+"   "+resultcode+"   "+jsonData);
        if(requestcode==300){
            try {
                getListRockType(jsonData);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(requestcode==101&&resultcode==201){
            try {
                JSONObject jsonObject = new JSONObject(jsonData);
                int id = jsonObject.getInt("ID");
                for (WellLayer item: listAdd) {
                    item.setWellID(id);
                    StringWriter writer = new StringWriter();
                    JsonWriter jsonWriter = new JsonWriter(writer);
                    jsonWriter.beginObject();
                    jsonWriter.name("WellID").value(item.getWellID());
                    jsonWriter.name("StartPoint").value(item.getStartPoint());
                    jsonWriter.name("EndPoint").value(item.getEndPoint());
                    jsonWriter.name("RockTypeID").value(item.getRockTypeID());
                    jsonWriter.endObject();
                    RequestAPI requestAPI = new RequestAPI(AvtAddWell.this, 201, "welllayers", "POST", writer.toString());
                    requestAPI.execute();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(requestcode==201){
            count+=1;
            if(count==listAdd.size()){
                onBackPressed();
            }
        }
    }

    private void getListRockType(String jsonData) throws JSONException {
        JSONArray jsonArray = new JSONArray(jsonData);
        for (int i =0; i<jsonArray.length(); i++){
            rockTypes.add(new RockType(jsonArray.getJSONObject(i)));
        }
        setSpnRock(rockTypes);
    }

    private void setSpnRock(final ArrayList<RockType> rockTypes) {
        ArrayAdapter<RockType> adapter = new ArrayAdapter(AvtAddWell.this, R.layout.spinner_item_background_blue, rockTypes){
            @Nullable
            @Override
            public Object getItem(int position) {
                return rockTypes.get(position).getName();
            }

            @Override
            public long getItemId(int position) {
                return rockTypes.get(position).getId();
            }
        };
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_blue);
        spnRockType.setAdapter(adapter);
    }
    public class RockAdapter extends RecyclerView.Adapter<RockAdapter.ViewHolder>{

        private final Context mcontext;
        private final ArrayList<WellLayer> list;

        public RockAdapter(Context mcontext, ArrayList<WellLayer> list ){
            this.mcontext = mcontext;
            this.list = list;
        }
        @NonNull
        @Override
        public RockAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(mcontext);
            View view = inflater.inflate(R.layout.list_rock_type_blue, parent, false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull RockAdapter.ViewHolder holder, int position) {
            final WellLayer wellLayer = list.get(position);
            holder.tvRockName.setText(wellLayer.RockType.getName());
            holder.tvRange.setText("From "+ wellLayer.getStartPoint()+" To "+wellLayer.getEndPoint());
            holder.icDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i =0; i<list.size(); i++){
                        if(list.get(i).getRockTypeID()==wellLayer.getRockTypeID()){
                            try {
                                list.get(i+1).setStartPoint(list.get(i).getStartPoint());
                            }catch (Exception ex){}
                            list.remove(list.get(i));
                            if(list.size()==0){
                                tvStartPoint.setText("0");
                            }else{
                                tvStartPoint.setText(list.get(list.size()-1).getEndPoint()+"");
                            }
                            setRockAdapter(list);
                            break;
                        }
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class ViewHolder extends  RecyclerView.ViewHolder{
            TextView tvRockName, tvRange;
            ImageView icDelete;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tvRockName = itemView.findViewById(R.id.rock_name);
                icDelete = itemView.findViewById(R.id.delete_rock);
                tvRange = itemView.findViewById(R.id.tv_range);
            }
        }
    }
}