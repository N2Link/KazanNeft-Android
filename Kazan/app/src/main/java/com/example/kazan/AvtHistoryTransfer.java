package com.example.kazan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.ReceiverCallNotAllowedException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.kazan.model.AssetTransferLog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AvtHistoryTransfer extends AppCompatActivity {

    RecyclerView recListTransfer;
    Button btnBack;
    int assetID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avt_history_transfer);
        recListTransfer = findViewById(R.id.list_transfer);
        btnBack = findViewById(R.id.btn_back_history);
        Intent intent = getIntent();
        assetID = intent.getExtras().getInt("AssetID");
        showRec(getListTransfer());
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AvtHistoryTransfer.super.onBackPressed();
            }
        });
    }

    private void showRec(ArrayList<AssetTransferLog> listTransfer) {
        TransferAdapter adapter = new TransferAdapter(getApplicationContext(), listTransfer);
        recListTransfer.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recListTransfer.setAdapter(adapter);
    }

    private ArrayList<AssetTransferLog> getListTransfer() {
        ArrayList<AssetTransferLog> assetTransferLogs = new ArrayList<>();
        for (AssetTransferLog assetTransferLog: AvtAssetManagement.assetTransferLogs) {
            if(assetTransferLog.getAssetID()==assetID){
                assetTransferLogs.add(assetTransferLog);
            }
        }
        return assetTransferLogs;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_back_enter, R.anim.anim_back_exit);

    }
    public class TransferAdapter extends RecyclerView.Adapter<TransferAdapter.ViewHolder>{

        Context mContext;
        ArrayList<AssetTransferLog> mAssetTransferLogs;

        public TransferAdapter(Context mContext, ArrayList<AssetTransferLog> mAssetTransferLogs) {
            this.mContext = mContext;
            this.mAssetTransferLogs = mAssetTransferLogs;
        }

        @NonNull
        @Override
        public TransferAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View view = inflater.inflate(R.layout.list_history, parent, false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull TransferAdapter.ViewHolder holder, int position) {
            AssetTransferLog assetTransferLog = mAssetTransferLogs.get(position);
            try {
                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(assetTransferLog.getTransferDate());
                holder.tvDateTrans.setText(new SimpleDateFormat("yyyy-MM-dd").format(date));
                holder.tvOldDMName.setText(assetTransferLog.getFromDMName());
                holder.tvOldSN.setText(assetTransferLog.getFromAssetSN());
                holder.tvCurDMName.setText(assetTransferLog.getToDMName());
                holder.tvCurSN.setText(assetTransferLog.getToAssetSN());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return mAssetTransferLogs.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvDateTrans, tvOldDMName, tvOldSN, tvCurDMName, tvCurSN;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tvDateTrans = itemView.findViewById(R.id.date_trans);
                tvCurDMName = itemView.findViewById(R.id.cur_dm_name);
                tvCurSN = itemView.findViewById(R.id.cur_sn);
                tvOldDMName = itemView.findViewById(R.id.old_dm_name);
                tvOldSN = itemView.findViewById(R.id.old_sn);
            }
        }
    }
}