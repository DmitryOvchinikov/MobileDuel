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

        Player left_player = new Player(50, true);
        Player right_player = new Player(50,true);

        findViews();
        glideIMGs();
        initializeProgressBars(left_player, right_player);
        playGame();
    }

    private void initializeProgressBars(Player left_player, Player right_player) {
        main_BAR_leftPlayer.setMax(left_player.getHealth());
        main_BAR_leftPlayer.setProgress(left_player.getHealth());

        main_BAR_rightPlayer.setMax(right_player.getHealth());
        main_BAR_rightPlayer.setProgress(right_player.getHealth());
    }

    private void playGame() {
        activateButtons("left");
        deactivateButtons("right");
    }

    private void activateButtons(String side) {
        if (side.equals("left")) {
            main_BTN_left1.setEnabled(true);
            main_BTN_left2.setEnabled(true);
            main_BTN_left3.setEnabled(true);
        } else {
            main_BTN_right1.setEnabled(true);
            main_BTN_right2.setEnabled(true);
            main_BTN_right3.setEnabled(true);
        }
    }

    private void deactivateButtons(String side) {
        if (side.equals("left")) {
            main_BTN_left1.setEnabled(false);
            main_BTN_left2.setEnabled(false);
            main_BTN_left3.setEnabled(false);
        } else {
            main_BTN_right1.setEnabled(false);
            main_BTN_right2.setEnabled(false);
            main_BTN_right3.setEnabled(false);
        }
    }

    private void glideIMGs() {
        Glide.with(this).load(R.drawable.left_player).into(main_IMG_leftPlayer);
        Glide.with(this).load(R.drawable.right_player).into(main_IMG_rightPlayer);
        Glide.with(this).load(R.drawable.main_background).into(main_IMG_background);

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
        main_IMG_background = findViewById(R.id.main_IMG_background);

        main_BAR_leftPlayer = findViewById(R.id.main_BAR_leftPlayer);
        main_BAR_rightPlayer = findViewById(R.id.main_BAR_rightPlayer);
    }
}