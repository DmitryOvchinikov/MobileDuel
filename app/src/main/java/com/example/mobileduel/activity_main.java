package com.example.mobileduel;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

//TODO: Map in the records sliding panel
//TODO: Coin flip / throwing dice animation for the random player choice
//TODO: fix healing other player
//TODO: fix progress bar border shenanigans
//TODO: fix animation disappearing instantly

public class activity_main extends AppCompatActivity {

    private Button main_BTN_play;
    private Button main_BTN_reset;

    private SwitchCompat main_SWT_mode;

    private ImageView main_IMG_background;

    private TextView main_LBL_auto;
    private TextView main_LBL_manual;

    private RecyclerView main_recycler_records;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        glideIMGs();

        main_BTN_play.setOnClickListener(playListener);
        main_BTN_reset.setOnClickListener(resetListener);

        main_SWT_mode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    main_LBL_manual.setTypeface(null, Typeface.BOLD);
                    main_LBL_auto.setTypeface(null, Typeface.NORMAL);
                } else {
                    main_LBL_auto.setTypeface(null, Typeface.BOLD);
                    main_LBL_manual.setTypeface(null, Typeface.NORMAL);
                }
            }
        });

        main_recycler_records.setAdapter(adapter_records.getInstance());
        main_recycler_records.setLayoutManager(new LinearLayoutManager(this));
        main_recycler_records.getAdapter().notifyDataSetChanged();

    }

    private void resetRecords() {
        ArrayList<Player> players = new ArrayList<>();
        MySharedPreferences.getInstance().putPlayers(players);
        adapter_records.getInstance().setTOP10_players(players);
        adapter_records.getInstance().notifyDataSetChanged();
    }

    private void glideIMGs() {
        Glide.with(this).load(R.drawable.main_background).into(main_IMG_background);
    }

    private void findViews() {
        main_BTN_play = findViewById(R.id.main_BTN_play);
        main_SWT_mode = findViewById(R.id.main_SWT_mode);
        main_IMG_background = findViewById(R.id.main_IMG_background);
        main_LBL_auto = findViewById(R.id.main_LBL_auto);
        main_LBL_manual = findViewById(R.id.main_LBL_manual);
        main_recycler_records = findViewById(R.id.main_recycler_records);
        main_BTN_reset = findViewById(R.id.main_BTN_reset);
    }

    private View.OnClickListener resetListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            resetRecords();
        }
    };

    private View.OnClickListener playListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            playGame();
        }
    };

    private void playGame() {
        Intent intent = new Intent(this, activity_game.class);
        intent.putExtra("game_mode", main_SWT_mode.isChecked());
        startActivity(intent);
    }
}
