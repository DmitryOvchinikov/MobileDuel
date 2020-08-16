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

public class MainActivity extends AppCompatActivity {

    private Button main_BTN_leftlight;
    private Button main_BTN_leftheavy;
    private Button main_BTN_leftheal;
    private Button main_BTN_rightlight;
    private Button main_BTN_rightheavy;
    private Button main_BTN_rightheal;

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

    private void initializeProgressBars() {
        main_BAR_leftPlayer.setMax(left_player.getHealth());
        main_BAR_leftPlayer.setProgress(left_player.getHealth());

        main_BAR_rightPlayer.setMax(right_player.getHealth());
        main_BAR_rightPlayer.setProgress(right_player.getHealth());

        main_BAR_leftPlayer.getProgressDrawable().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);
        main_BAR_rightPlayer.getProgressDrawable().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);
    }

    //Decided to use the switch onClickListener method regardless of the fact you have said its bad in one of the lectures, so I won't have to duplicate a lot of code.
    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.main_BTN_leftlight:
                    playTurn(right_player, 0, "right", "left");
                    break;
                case R.id.main_BTN_leftheavy:
                    playTurn(right_player, 1, "right", "left");
                    break;
                case R.id.main_BTN_leftheal:
                    playTurn(left_player, 2, "right", "left");
                    break;
                case R.id.main_BTN_rightlight:
                    playTurn(left_player, 0, "left", "right");
                    break;
                case R.id.main_BTN_rightheavy:
                    playTurn(left_player, 1, "left", "right");
                    break;
                case R.id.main_BTN_rightheal:
                    playTurn(right_player, 2, "left", "right");
                    break;
                default:
                    break;
            }
        }
    };

    //Play a single turn.
    private void playTurn(Player player, int x, String activated_side, String deactivated_side) {
        // x:
        // 0 - Light attack
        // 1 - Heavy attack
        // 2 - Heal

        //play an action, activate and deactivate the relevant buttons
        player.action(x);
        activateButtons(activated_side);
        deactivateButtons(deactivated_side);

        updateProgressBars();

        checkWin();
    }

    //Update the progress bars.
    private void updateProgressBars() {
        main_BAR_leftPlayer.setProgress(left_player.getHealth());
        main_BAR_rightPlayer.setProgress(right_player.getHealth());

        updateProgressBarColors();
    }

    //Change the color of the progress bars if they are below 15 to RED, otherwise to BLUE.
    private void updateProgressBarColors() {
        if (main_BAR_leftPlayer.getProgress() <= 15) {
            main_BAR_leftPlayer.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
        } else {
            main_BAR_leftPlayer.getProgressDrawable().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);
        }

        if (main_BAR_rightPlayer.getProgress() <= 15) {
            main_BAR_rightPlayer.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
        } else {
            main_BAR_rightPlayer.getProgressDrawable().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);
        }
    }

    //Check if a player won the game.
    private void checkWin() {
        if (!left_player.checkState()) {
            Toast.makeText(this, "Right player won!", Toast.LENGTH_SHORT).show();
            resetGame();
        } else if (!right_player.checkState()) {
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
            main_BTN_leftlight.setEnabled(false);
            main_BTN_leftheavy.setEnabled(false);
            main_BTN_leftheal.setEnabled(false);
        } else {
            main_BTN_rightlight.setEnabled(false);
            main_BTN_rightheavy.setEnabled(false);
            main_BTN_rightheal.setEnabled(false);
        }
    }

    private void activateButtons(String side) {
        if (side.equals("left")) {
            main_BTN_leftlight.setEnabled(true);
            main_BTN_leftheavy.setEnabled(true);
            main_BTN_leftheal.setEnabled(true);
        } else {
            main_BTN_rightlight.setEnabled(true);
            main_BTN_rightheavy.setEnabled(true);
            main_BTN_rightheal.setEnabled(true);
        }
    }

    private void setButtonListeners() {
        main_BTN_leftlight.setOnClickListener(clickListener);
        main_BTN_leftheavy.setOnClickListener(clickListener);
        main_BTN_leftheal.setOnClickListener(clickListener);
        main_BTN_rightlight.setOnClickListener(clickListener);
        main_BTN_rightheavy.setOnClickListener(clickListener);
        main_BTN_rightheal.setOnClickListener(clickListener);
    }

    private void glideIMGs() {
        Glide.with(this).load(R.drawable.left_player).into(main_IMG_leftPlayer);
        Glide.with(this).load(R.drawable.right_player).into(main_IMG_rightPlayer);
        Glide.with(this).load(R.drawable.main_background).into(main_IMG_background);
    }

    private void findViews() {
        main_BTN_leftlight = findViewById(R.id.main_BTN_leftlight);
        main_BTN_leftheavy = findViewById(R.id.main_BTN_leftheavy);
        main_BTN_leftheal = findViewById(R.id.main_BTN_leftheal);
        main_BTN_rightlight = findViewById(R.id.main_BTN_rightlight);
        main_BTN_rightheavy = findViewById(R.id.main_BTN_rightheavy);
        main_BTN_rightheal = findViewById(R.id.main_BTN_rightheal);

        main_IMG_leftPlayer = findViewById(R.id.main_IMG_leftPlayer);
        main_IMG_rightPlayer = findViewById(R.id.main_IMG_rightPlayer);
        main_IMG_background = findViewById(R.id.main_IMG_background);

        main_BAR_leftPlayer = findViewById(R.id.main_BAR_leftPlayer);
        main_BAR_rightPlayer = findViewById(R.id.main_BAR_rightPlayer);
    }
}