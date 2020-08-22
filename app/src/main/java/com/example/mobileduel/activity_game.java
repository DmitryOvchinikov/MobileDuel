package com.example.mobileduel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

//TODO: change the activate/deactivate button methods to a single one

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

    private Player current_auto_player;

    private boolean game_mode;

    private Runnable automatic_runnable;
    private Handler automatic_handler;

    private boolean isRunnableActive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        game_mode = getIntent().getExtras().getBoolean("game_mode");

        left_player = new Player(50, true, 1, 0);
        right_player = new Player(50, true, 1, 1);

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

        isRunnableActive = true;

        final Random rand = new Random();
        final int delay = 1000;
        current_auto_player = chooseStartingPlayer();

        automatic_handler = new Handler();
        automatic_runnable = new Runnable() {
            @Override
            public void run() {
                Player p = checkWin();
                if (p != null) {
                    automatic_handler.removeCallbacksAndMessages(automatic_runnable);
                    finishGame(p);
                    return;
                }
                if (!isRunnableActive) {
                    return;
                }

                //Change player to play a turn
                if (current_auto_player == left_player) {
                    current_auto_player = right_player;
                } else {
                    current_auto_player = left_player;
                }

                if (current_auto_player.getHealth() == 50) {
                    current_auto_player.action(rand.nextInt(2)); //not using heal at max health
                } else {
                    current_auto_player.action(rand.nextInt(3));
                }

                updateProgressBars();
                current_auto_player.incrementTurn();
                Log.d("oof", "TURN PLAYED");
                automatic_handler.postDelayed(automatic_runnable, delay);
                }
            };
        automatic_handler.postDelayed(automatic_runnable, delay);
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

        Player winning_player = checkWin();
        if (winning_player != null) {
            finishGame(winning_player);
        }
    }

    //Pseudo-randomize a starting side via nextDouble.
    private Player chooseStartingPlayer() {
        Random rand = new Random();
        double num = rand.nextDouble();
        if (num >= 0.5) {
            return left_player;
        } else {
            return right_player;
        }
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
                    left_player.incrementTurn();
                    break;
                case R.id.game_BTN_leftheavy:
                    playManualTurn(right_player, 1, "right", "left");
                    left_player.incrementTurn();
                    break;
                case R.id.game_BTN_leftheal:
                    playManualTurn(left_player, 2, "right", "left");
                    left_player.incrementTurn();
                    break;
                case R.id.game_BTN_rightlight:
                    playManualTurn(left_player, 0, "left", "right");
                    right_player.incrementTurn();
                    break;
                case R.id.game_BTN_rightheavy:
                    playManualTurn(left_player, 1, "left", "right");
                    right_player.incrementTurn();
                    break;
                case R.id.game_BTN_rightheal:
                    playManualTurn(right_player, 2, "left", "right");
                    right_player.incrementTurn();
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
    private Player checkWin() {
        if (left_player.checkState()) { //left player died, right player won
            return right_player;
        } else if (right_player.checkState()) { //right player died, left player won
            return left_player;
        }
        return null;
    }

    private void finishGame(Player p) {
        deactivateButtons("Left");
        deactivateButtons("Right");

        saveToSharedPreferences(p);

        Intent intent = new Intent(this, activity_finish.class);
        intent.putExtra("player_side", p.getSide());
        intent.putExtra("player_turns", p.getTurns());
        startActivity(intent);
        finish();
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
        ArrayList<Player> players = MySharedPreferences.getInstance().getPlayers();
        players.add(player);
        Collections.sort(players, new Comparator<Player>(){
            @Override
            public int compare(Player p1, Player p2) {
                return p1.getTurns() - p2.getTurns();
            }
        });

        if (players.size() >= 6) { //forcing a top 5
            players.remove(players.size()-1);
        }

        MySharedPreferences.getInstance().putPlayers(players);
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

    @Override
    protected void onStop() {
        super.onStop();

        //if the runnable is active and the game mode is automatic, stop the runnable
        if (isRunnableActive && !game_mode) {
            automatic_handler.removeCallbacksAndMessages(automatic_runnable);
            isRunnableActive = false;
        }
        Log.d("oof", "ON STOP");
    }

    @Override
    protected void onResume() {
        Log.d("oof", "ON RESUME");

        //If the runnable is not active and the game mode is automatic - start up the automatic runnable.
        if (!isRunnableActive && !game_mode) {
            isRunnableActive = true;
            automatic_handler.postDelayed(automatic_runnable, 1000);
        }

        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onStop();
    }
}