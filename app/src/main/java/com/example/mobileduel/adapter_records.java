package com.example.mobileduel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class adapter_records extends RecyclerView.Adapter<adapter_records.RecordsViewHolder> {

    private static adapter_records instance;
    ArrayList<Player> TOP10_players;

    public static adapter_records getInstance() {
        return instance;
    }

    public static adapter_records initHelper(Context context, ArrayList<Player> players) {
        if (instance == null) {
            instance = new adapter_records(players);
        }
        return instance;
    }

    public adapter_records(ArrayList<Player> players) {
        this.TOP10_players = players;
    }

    public void setTOP10_players(ArrayList<Player> TOP10_players) {
        this.TOP10_players = TOP10_players;
    }

    @NonNull
    @Override
    public RecordsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.records_row, parent,false);
        return new RecordsViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull RecordsViewHolder holder, int position) {
        holder.row_LBL_turns.setText(" " + (position+1) + ". " + "TURNS:" + " " + TOP10_players.get(position).getTurns());
    }

    @Override
    public int getItemCount() {
        if (TOP10_players == null) {
            return 0;
        }
        return TOP10_players.size();
    }

    public class RecordsViewHolder extends RecyclerView.ViewHolder {

        private TextView row_LBL_turns;

        public RecordsViewHolder(@NonNull View itemView) {
            super(itemView);
            row_LBL_turns = itemView.findViewById(R.id.row_LBL_turns);
        }
    }
}
