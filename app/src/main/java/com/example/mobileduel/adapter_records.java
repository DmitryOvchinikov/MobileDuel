package com.example.mobileduel;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class adapter_records extends RecyclerView.Adapter<adapter_records.RecordsViewHolder> {

    Context context;

    public adapter_records(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RecordsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.records_row, parent,false);
        return new RecordsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordsViewHolder holder, int position) {
        holder.records_LBL_turns.setText("YIKES!");
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class RecordsViewHolder extends RecyclerView.ViewHolder {

        private TextView records_LBL_turns;

        public RecordsViewHolder(@NonNull View itemView) {
            super(itemView);
            records_LBL_turns = itemView.findViewById(R.id.row_LBL_turns);
        }
    }
}
