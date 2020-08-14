package com.example.mobileduel;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import static android.graphics.BlendMode.COLOR;

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

    private Player left_player;
    private Player right_player;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        left_player = new Player(50, true);
        right_player = new Player(50,true);

        findViews();
        glideIMGs();
        setButtonListeners();
        initializeProgressBars();

        activateButtons("left");
        deactivateButtons("right");
    }

    private void setButtonListeners() {
        main_BTN_left1.setOnClickListener(clickListener);
        main_BTN_left2.setOnClickListener(clickListener);
        main_BTN_left3.setOnClickListener(clickListener);
        main_BTN_right1.setOnClickListener(clickListener);
        main_BTN_right2.setOnClickListener(clickListener);
        main_BTN_right3.setOnClickListener(clickListener);
    }

    private void initializeProgressBars() {
        main_BAR_leftPlayer.setMax(left_player.getHealth());
        main_BAR_leftPlayer.setProgress(left_player.getHealth());

        main_BAR_rightPlayer.setMax(right_player.getHealth());
        main_BAR_rightPlayer.setProgress(right_player.getHealth());

        main_BAR_leftPlayer.getProgressDrawable().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);
        main_BAR_rightPlayer.getProgressDrawable().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);
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

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.main_BTN_left1:
                    right_player.action(0);
                    activateButtons("right");
                    deactivateButtons("left");
                    break;
                case R.id.main_BTN_left2:
                    right_player.action(1);
                    activateButtons("right");
                    deactivateButtons("left");
                    break;
                case R.id.main_BTN_left3:
                    left_player.action(2);
                    activateButtons("right");
                    deactivateButtons("left");
                    break;
                case R.id.main_BTN_right1:
                    left_player.action(0);
                    activateButtons("left");
                    deactivateButtons("right");
                    break;
                case R.id.main_BTN_right2:
                    left_player.action(1);
                    activateButtons("left");
                    deactivateButtons("right");
                    break;
                case R.id.main_BTN_right3:
                    right_player.action(2);
                    activateButtons("left");
                    deactivateButtons("right");
                    break;
                default:
                    break;
            }
            updateProgressBars();
            boolean left_state = left_player.checkState();
            boolean right_state = right_player.checkState();
            checkWin(left_state, right_state);
        }
    };

    private void updateProgressBars() {
        main_BAR_leftPlayer.setProgress(left_player.getHealth());
        main_BAR_rightPlayer.setProgress(right_player.getHealth());
        if (main_BAR_leftPlayer.getProgress() <= 15) {
            main_BAR_leftPlayer.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
        }
        if (main_BAR_rightPlayer.getProgress() <= 15) {
            main_BAR_rightPlayer.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
        }
    }

    private void checkWin(boolean left_state, boolean right_state) {
        if (left_state == false) {
            Toast.makeText(this, "Right player won!", Toast.LENGTH_SHORT).show();
            resetGame();
        } else if (right_state == false) {
            Toast.makeText(this, "Left player won!", Toast.LENGTH_SHORT).show();
            resetGame();
        }
    }

    private void resetGame() {
        left_player.setHealth(50);
        left_player.setState(true);
        right_player.setHealth(50);
        right_player.setState(true);

        initializeProgressBars();
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