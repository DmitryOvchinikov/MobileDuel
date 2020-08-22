package com.example.mobileduel;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class activity_finish extends AppCompatActivity {

    private TextView finish_LBL_title;
    private TextView finish_LBL_turns;

    private Button finish_BTN_back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);

        findViews();
        updateTitle();
        finish_LBL_turns.append(" " + getIntent().getExtras().getInt("player_turns"));
        finish_BTN_back.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
        }
    };

    private void updateTitle() {
        if (getIntent().getExtras().getInt("player_side") == 0) {
            finish_LBL_title.setText(R.string.left_won);
        } else {
            finish_LBL_title.setText(R.string.right_won);
        }
    }

    @Override
    protected void onPause() {
        //update records when going back to the main menu
        adapter_records.getInstance().setTOP10_players(MySharedPreferences.getInstance().getPlayers());
        adapter_records.getInstance().notifyDataSetChanged();
        super.onPause();
    }

    private void findViews() {
        finish_LBL_title = findViewById(R.id.finish_LBL_title);
        finish_LBL_turns = findViewById(R.id.finish_LBL_turns);
        finish_BTN_back = findViewById(R.id.finish_BTN_back);
    }
}
