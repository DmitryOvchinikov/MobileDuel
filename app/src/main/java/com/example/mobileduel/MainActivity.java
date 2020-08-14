package com.example.mobileduel;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;

public class MainActivity extends AppCompatActivity {

    private Button main_BTN_left1;
    private Button main_BTN_left2;
    private Button main_BTN_left3;
    private Button main_BTN_right1;
    private Button main_BTN_right2;
    private Button main_BTN_right3;

    private ImageView main_IMG_leftPlayer;
    private ImageView main_IMG_rightPlayer;
    private ImageView main_IMG_background;

    private ProgressBar main_BAR_leftPlayer;
    private ProgressBar main_BAR_rightPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        glideIMGs();
    }

    private void glideIMGs() {
        Glide.with(this).load(R.drawable.left_player).into(main_IMG_leftPlayer);
        Glide.with(this).load(R.drawable.right_player).into(main_IMG_rightPlayer);

    }

    private void findViews() {
        main_BTN_left1 = findViewById(R.id.main_BTN_left1);
        main_BTN_left2 = findViewById(R.id.main_BTN_left2);
        main_BTN_left3 = findViewById(R.id.main_BTN_left3);
        main_BTN_right1 = findViewById(R.id.main_BTN_right1);
        main_BTN_right2 = findViewById(R.id.main_BTN_right2);
        main_BTN_right3 = findViewById(R.id.main_BTN_right3);

        main_IMG_leftPlayer = findViewById(R.id.main_IMG_leftPlayer);
        main_IMG_rightPlayer = findViewById(R.id.main_IMG_rightPlayer);

        main_BAR_leftPlayer = findViewById(R.id.main_BAR_leftPlayer);
        main_BAR_rightPlayer = findViewById(R.id.main_BAR_rightPlayer);
    }
}