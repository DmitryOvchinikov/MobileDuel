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

public class activity_manualGame extends AppCompatActivity {

    //TODO: maybe delete resetGame method since it is not useful if one game is played.

    private Button manual_BTN_leftlight;
    private Button manual_BTN_leftheavy;
    private Button manual_BTN_leftheal;
    private Button manual_BTN_rightlight;
    private Button manual_BTN_rightheavy;
    private Button manual_BTN_rightheal;

    private ImageView manual_IMG_leftPlayer;
    private ImageView manual_IMG_rightPlayer;
    private ImageView manual_IMG_background;

    private ProgressBar manual_BAR_leftPlayer;
    private ProgressBar manual_BAR_rightPlayer;

    private Player left_player;
    private Player right_player;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual);

        left_player = new Player(50, true);
        right_player = new Player(50,true);

        findViews();
        glideIMGs();
        setButtonListeners();
        initializeProgressBars();

        //Left player always starts first, as per the instructions.
        activateButtons("left");
        deactivateButtons("right");
    }

    private void initializeProgressBars() {
        manual_BAR_leftPlayer.setMax(left_player.getHealth());
        manual_BAR_leftPlayer.setProgress(left_player.getHealth());

        manual_BAR_rightPlayer.setMax(right_player.getHealth());
        manual_BAR_rightPlayer.setProgress(right_player.getHealth());

        manual_BAR_leftPlayer.getProgressDrawable().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
        manual_BAR_rightPlayer.getProgressDrawable().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
    }

    //Decided to use the switch onClickListener method regardless of the fact you have said its bad in one of the lectures, so I won't have to duplicate a lot of code.
    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.manual_BTN_leftlight:
                    playTurn(right_player, 0, "right", "left");
                    break;
                case R.id.manual_BTN_leftheavy:
                    playTurn(right_player, 1, "right", "left");
                    break;
                case R.id.manual_BTN_leftheal:
                    playTurn(left_player, 2, "right", "left");
                    break;
                case R.id.manual_BTN_rightlight:
                    playTurn(left_player, 0, "left", "right");
                    break;
                case R.id.manual_BTN_rightheavy:
                    playTurn(left_player, 1, "left", "right");
                    break;
                case R.id.manual_BTN_rightheal:
                    playTurn(right_player, 2, "left", "right");
                    break;
                default:
                    break;
            }
        }
    };

    //Play a single turn.
    private void playTurn(Player player, int action_number, String activate_side, String deactivate_side) {
        // action_number:
        // 0 - Light attack
        // 1 - Heavy attack
        // 2 - Heal

        //play an action, activate and deactivate the relevant buttons
        player.action(action_number);
        activateButtons(activate_side);
        deactivateButtons(deactivate_side);

        updateProgressBars();

        checkWin();
    }

    //Update the progress bars.
    private void updateProgressBars() {
        manual_BAR_leftPlayer.setProgress(left_player.getHealth());
        manual_BAR_rightPlayer.setProgress(right_player.getHealth());

        updateProgressBarColors();
    }

    //Change the color of the progress bars if they are below 15 to RED, otherwise to GREEN.
    private void updateProgressBarColors() {
        if (manual_BAR_leftPlayer.getProgress() <= 15) {
            manual_BAR_leftPlayer.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
        } else {
            manual_BAR_leftPlayer.getProgressDrawable().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
        }

        if (manual_BAR_rightPlayer.getProgress() <= 15) {
            manual_BAR_rightPlayer.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
        } else {
            manual_BAR_rightPlayer.getProgressDrawable().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
        }
    }

    //Check if a player won the game.
    private void checkWin() {
        if (!left_player.checkState()) {
            Toast.makeText(this, "Right player won!", Toast.LENGTH_SHORT).show();
            resetGame();
            finish();
        } else if (!right_player.checkState()) {
            Toast.makeText(this, "Left player won!", Toast.LENGTH_SHORT).show();
            resetGame();
            finish();
        }
    }

    //Reset the game into its default position.
    private void resetGame() {
        left_player.setHealth(50);
        left_player.setState(true);
        right_player.setHealth(50);
        right_player.setState(true);

        initializeProgressBars();

        //Left player always starts first
        activateButtons("left");
        deactivateButtons("right");
    }

    private void deactivateButtons(String side) {
        if (side.equals("left")) {
            manual_BTN_leftlight.setEnabled(false);
            manual_BTN_leftheavy.setEnabled(false);
            manual_BTN_leftheal.setEnabled(false);
        } else {
            manual_BTN_rightlight.setEnabled(false);
            manual_BTN_rightheavy.setEnabled(false);
            manual_BTN_rightheal.setEnabled(false);
        }
    }

    private void activateButtons(String side) {
        if (side.equals("left")) {
            manual_BTN_leftlight.setEnabled(true);
            manual_BTN_leftheavy.setEnabled(true);
            manual_BTN_leftheal.setEnabled(true);
        } else {
            manual_BTN_rightlight.setEnabled(true);
            manual_BTN_rightheavy.setEnabled(true);
            manual_BTN_rightheal.setEnabled(true);
        }
    }

    private void setButtonListeners() {
        manual_BTN_leftlight.setOnClickListener(clickListener);
        manual_BTN_leftheavy.setOnClickListener(clickListener);
        manual_BTN_leftheal.setOnClickListener(clickListener);
        manual_BTN_rightlight.setOnClickListener(clickListener);
        manual_BTN_rightheavy.setOnClickListener(clickListener);
        manual_BTN_rightheal.setOnClickListener(clickListener);
    }

    private void glideIMGs() {
        Glide.with(this).load(R.drawable.left_player).into(manual_IMG_leftPlayer);
        Glide.with(this).load(R.drawable.right_player).into(manual_IMG_rightPlayer);
        Glide.with(this).load(R.drawable.main_background).into(manual_IMG_background);
    }

    private void findViews() {
        manual_BTN_leftlight = findViewById(R.id.manual_BTN_leftlight);
        manual_BTN_leftheavy = findViewById(R.id.manual_BTN_leftheavy);
        manual_BTN_leftheal = findViewById(R.id.manual_BTN_leftheal);
        manual_BTN_rightlight = findViewById(R.id.manual_BTN_rightlight);
        manual_BTN_rightheavy = findViewById(R.id.manual_BTN_rightheavy);
        manual_BTN_rightheal = findViewById(R.id.manual_BTN_rightheal);

        manual_IMG_leftPlayer = findViewById(R.id.manual_IMG_leftPlayer);
        manual_IMG_rightPlayer = findViewById(R.id.manual_IMG_rightPlayer);
        manual_IMG_background = findViewById(R.id.manual_IMG_background);

        manual_BAR_leftPlayer = findViewById(R.id.manual_BAR_leftPlayer);
        manual_BAR_rightPlayer = findViewById(R.id.manual_BAR_rightPlayer);
    }
}