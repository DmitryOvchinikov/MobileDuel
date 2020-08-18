package com.example.mobileduel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.Random;

public class activity_game extends AppCompatActivity {

    private Button game_BTN_leftlight;
    private Button game_BTN_leftheavy;
    private Button game_BTN_leftheal;
    private Button game_BTN_rightlight;
    private Button game_BTN_rightheavy;
    private Button game_BTN_rightheal;

    private ImageView game_IMG_leftPlayer;
    private ImageView game_IMG_rightPlayer;
    private ImageView game_IMG_background;

    private ProgressBar game_BAR_leftPlayer;
    private ProgressBar game_BAR_rightPlayer;

    private Player left_player;
    private Player right_player;

    private boolean game_mode;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        game_mode = getIntent().getExtras().getBoolean("game_mode");

        left_player = new Player(50, true, 0);
        right_player = new Player(50, true, 0);

        findViews();
        glideIMGs();
        setButtonListeners();
        initializeProgressBars();

        if (game_mode) {
            //Left player always starts first, as per the instructions of a manual game.
            activateButtons("left");
            deactivateButtons("right");
        } else {
            playAutomatic();
        }
    }

    private void playAutomatic() {
        deactivateButtons("left");
        deactivateButtons("right");
        boolean choice = chooseStartingSide();
        //choice false = right
        //choice true = left
    }

    private void playAutomaticTurn(Player player, int action_number, boolean choice) {
        // action_number:
        // 0 - Light attack
        // 1 - Heavy attack
        // 2 - Heal

        if (choice) {
            left_player.action(action_number);
        } else {
            right_player.action(action_number);
        }

        player.setTurns( player.getTurns() + 1 );
    }

    //Play a single turn.
    private void playManualTurn(Player player, int action_number, String activate_side, String deactivate_side) {
        // action_number:
        // 0 - Light attack
        // 1 - Heavy attack
        // 2 - Heal

        //play an action, activate and deactivate the relevant buttons
        player.action(action_number);
        activateButtons(activate_side);
        deactivateButtons(deactivate_side);

        updateProgressBars();

        player.setTurns( player.getTurns() + 1 );
        checkWin(game_mode);
    }

    //Pseudo-randomize a starting side via nextDouble.
    private boolean chooseStartingSide() {
        Random rand = new Random();
        double num = rand.nextDouble();
        return num >= 0.5;
    }

    private void initializeProgressBars() {
        game_BAR_leftPlayer.setMax(left_player.getHealth());
        game_BAR_leftPlayer.setProgress(left_player.getHealth());

        game_BAR_rightPlayer.setMax(right_player.getHealth());
        game_BAR_rightPlayer.setProgress(right_player.getHealth());

        game_BAR_leftPlayer.getProgressDrawable().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
        game_BAR_rightPlayer.getProgressDrawable().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
    }

    //Click listener for the manual game.
    private View.OnClickListener manualClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.game_BTN_leftlight:
                    playManualTurn(right_player, 0, "right", "left");
                    break;
                case R.id.game_BTN_leftheavy:
                    playManualTurn(right_player, 1, "right", "left");
                    break;
                case R.id.game_BTN_leftheal:
                    playManualTurn(left_player, 2, "right", "left");
                    break;
                case R.id.game_BTN_rightlight:
                    playManualTurn(left_player, 0, "left", "right");
                    break;
                case R.id.game_BTN_rightheavy:
                    playManualTurn(left_player, 1, "left", "right");
                    break;
                case R.id.game_BTN_rightheal:
                    playManualTurn(right_player, 2, "left", "right");
                    break;
                default:
                    break;
            }
        }
    };

    //Update the progress bars.
    private void updateProgressBars() {
        game_BAR_leftPlayer.setProgress(left_player.getHealth());
        game_BAR_rightPlayer.setProgress(right_player.getHealth());

        updateProgressBarColors();
    }

    //Change the color of the progress bars if they are below 15 to RED, otherwise to GREEN.
    private void updateProgressBarColors() {
        if (game_BAR_leftPlayer.getProgress() <= 15) {
            game_BAR_leftPlayer.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
        } else {
            game_BAR_leftPlayer.getProgressDrawable().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
        }

        if (game_BAR_rightPlayer.getProgress() <= 15) {
            game_BAR_rightPlayer.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
        } else {
            game_BAR_rightPlayer.getProgressDrawable().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
        }
    }

    //Check if a player won the game.
    private void checkWin(boolean game_mode) {
        if (!left_player.checkState()) {
            Intent intent = new Intent(this, activity_finish.class);
            intent.putExtra("player_side", "Left");
            intent.putExtra("player_turns", left_player.getTurns());
            startActivity(intent);
            finish();
        } else if (!right_player.checkState()) {
            Intent intent = new Intent(this, activity_finish.class);
            intent.putExtra("player_side", "Right");
            intent.putExtra("player_turns", right_player.getTurns());
            startActivity(intent);
            finish();
        }
    }

    private void deactivateButtons(String side) {
        if (side.equals("left")) {
            game_BTN_leftlight.setEnabled(false);
            game_BTN_leftheavy.setEnabled(false);
            game_BTN_leftheal.setEnabled(false);
        } else {
            game_BTN_rightlight.setEnabled(false);
            game_BTN_rightheavy.setEnabled(false);
            game_BTN_rightheal.setEnabled(false);
        }
    }

    private void activateButtons(String side) {
        if (side.equals("left")) {
            game_BTN_leftlight.setEnabled(true);
            game_BTN_leftheavy.setEnabled(true);
            game_BTN_leftheal.setEnabled(true);
        } else {
            game_BTN_rightlight.setEnabled(true);
            game_BTN_rightheavy.setEnabled(true);
            game_BTN_rightheal.setEnabled(true);
        }
    }

    private void saveToSharedPreferences(Player player) {
        sharedPreferences = getSharedPreferences("GAME", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String playerJson = gson.toJson(player);
        editor.putString("TOP10_PLAYERS", playerJson);
        editor.apply();
    }

    private void setButtonListeners() {
        game_BTN_leftlight.setOnClickListener(manualClickListener);
        game_BTN_leftheavy.setOnClickListener(manualClickListener);
        game_BTN_leftheal.setOnClickListener(manualClickListener);
        game_BTN_rightlight.setOnClickListener(manualClickListener);
        game_BTN_rightheavy.setOnClickListener(manualClickListener);
        game_BTN_rightheal.setOnClickListener(manualClickListener);
    }

    private void glideIMGs() {
        Glide.with(this).load(R.drawable.left_player).into(game_IMG_leftPlayer);
        Glide.with(this).load(R.drawable.right_player).into(game_IMG_rightPlayer);
        Glide.with(this).load(R.drawable.main_background).into(game_IMG_background);
    }

    private void findViews() {
        game_BTN_leftlight = findViewById(R.id.game_BTN_leftlight);
        game_BTN_leftheavy = findViewById(R.id.game_BTN_leftheavy);
        game_BTN_leftheal = findViewById(R.id.game_BTN_leftheal);
        game_BTN_rightlight = findViewById(R.id.game_BTN_rightlight);
        game_BTN_rightheavy = findViewById(R.id.game_BTN_rightheavy);
        game_BTN_rightheal = findViewById(R.id.game_BTN_rightheal);

        game_IMG_leftPlayer = findViewById(R.id.game_IMG_leftPlayer);
        game_IMG_rightPlayer = findViewById(R.id.game_IMG_rightPlayer);
        game_IMG_background = findViewById(R.id.game_IMG_background);

        game_BAR_leftPlayer = findViewById(R.id.game_BAR_leftPlayer);
        game_BAR_rightPlayer = findViewById(R.id.game_BAR_rightPlayer);
    }
}