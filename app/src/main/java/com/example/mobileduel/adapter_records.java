package com.example.mobileduel;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

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
        holder.location = TOP10_players.get(position).getLocation();
    }

    @Override
    public int getItemCount() {
        if (TOP10_players == null) {
            return 0;
        }
        return TOP10_players.size();
    }

    public static class RecordsViewHolder extends RecyclerView.ViewHolder {

        private ImageView row_IMG_map;
        private TextView row_LBL_turns;
        private LatLng location;

        public RecordsViewHolder(@NonNull final View itemView) {
            super(itemView);
            row_LBL_turns = itemView.findViewById(R.id.row_LBL_turns);
            row_IMG_map = itemView.findViewById(R.id.row_IMG_map);
            Glide.with(itemView.getContext()).load(R.drawable.map_icon).into(row_IMG_map);

            row_IMG_map.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //MapFragment fragment = new MapFragment(location);
                    Dialog mapDialog = new MapDialog(view.getContext(), location);
                    //AppCompatActivity activity = (AppCompatActivity) view.getContext();
                    //activity.getSupportFragmentManager().beginTransaction().replace(R.id.map_layout, fragment).addToBackStack(null).commit();
                    mapDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    mapDialog.getWindow().setDimAmount(0.8f);
                    mapDialog.show();
                }
            });
        }

    }
}
