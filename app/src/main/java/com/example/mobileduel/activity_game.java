package com.example.mobileduel;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import pub.devrel.easypermissions.PermissionRequest;

public class activity_game extends AppCompatActivity {

    private static final int LOCATION_REQUEST = 1234;
    private static final String[] LOCATION_PERMS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    private Button game_BTN_leftlight;
    private Button game_BTN_leftheavy;
    private Button game_BTN_leftheal;
    private Button game_BTN_rightlight;
    private Button game_BTN_rightheavy;
    private Button game_BTN_rightheal;
    private Button game_BTN_roll;

    private ImageView game_IMG_leftPlayer;
    private ImageView game_IMG_rightPlayer;
    private ImageView game_IMG_background;
    private ImageView game_IMG_dice1;
    private ImageView game_IMG_dice2;

    private ProgressBar game_BAR_leftPlayer;
    private ProgressBar game_BAR_rightPlayer;

    private Player left_player;
    private Player right_player;
    private Player current_auto_player;

    private boolean game_mode;
    private boolean isGameActive = false;

    private Runnable automatic_runnable;
    private Handler automatic_handler;
    private boolean isRunnableActive;

    private MediaPlayer mediaPlayer;
    private boolean isMusicPlaying = false;

    private LatLng latLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        activateLocation();

        game_mode = getIntent().getExtras().getBoolean("game_mode");

        left_player = new Player(50, true, 1, 0, latLng);
        right_player = new Player(50, true, 1, 1, latLng);

        findViews();
        glideIMGs();
        setButtonListeners();
        initializeProgressBars();

        updateButtons("left", false);
        updateButtons("right", false);

        game_IMG_dice1.setVisibility(View.INVISIBLE);
        game_IMG_dice2.setVisibility(View.INVISIBLE);
    }

    @AfterPermissionGranted(LOCATION_REQUEST)
    private void activateLocation() {
        if (EasyPermissions.hasPermissions(this, LOCATION_PERMS)) {
            getLocation();
        } else {
            EasyPermissions.requestPermissions( new PermissionRequest.Builder(this, LOCATION_REQUEST, LOCATION_PERMS)
                    .setRationale(R.string.location_rationale)
                    .setPositiveButtonText(R.string.location_yes)
                    .setNegativeButtonText(R.string.location_no)
                    .build());
        }
    }

    //Get the last known location of the user
    private void getLocation() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //Irrelevant to check permissions at this stage since this method is being invoked only AFTER the permissions have been granted.
        @SuppressLint("MissingPermission") Location loc = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        double[] location = new double[2];
        location[0] = 0;
        location[1] = 0;
        if (loc != null) {
            location[0] = loc.getLongitude();
            location[1] = loc.getLatitude();
        }
        latLng = new LatLng(location[1], location[0]);
        Log.d("oof", "" + latLng.toString());
    }

    //Play the game automatically via a handler with a delay of 1 second per turn.
    private void playAutomatic(Player player) {

        //If the game is already active, return from the second instance of this method.
        if (isGameActive) {
            return;
        }
        isGameActive = true;
        isRunnableActive = true;

        final Random rand = new Random();
        final int delay = 1000;
        current_auto_player = player;

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

                //Stop the runnable when it is supposed to stop, aka when onStop changes the boolean
                if (!isRunnableActive) {
                    return;
                }

                int random_bound;
                int action;

                //Not using heal at maximum health
                if (current_auto_player.getHealth() == 50) {
                    random_bound = 2;
                } else {
                    random_bound = 3;
                }

                action = rand.nextInt(random_bound);
                if (action != 2) {
                    if (current_auto_player.equals(left_player)) {
                        right_player.action(action);
                    } else {
                        left_player.action(action);
                    }
                } else {
                    current_auto_player.action(action);
                }

                //Change player to play a turn
                if (current_auto_player.equals(left_player)) {
                    current_auto_player = right_player;
                } else {
                    current_auto_player = left_player;
                }

                updateProgressBars();
                current_auto_player.incrementTurn();
                automatic_handler.postDelayed(automatic_runnable, delay);
                }
            };
        automatic_handler.postDelayed(automatic_runnable, delay);
        }

    //Play a single manual turn.
    private void playManualTurn(Player player, int action_number, String activate_side, String deactivate_side) {
        // action_number:
        // 0 - Light attack
        // 1 - Heavy attack
        // 2 - Heal

        //play an action, activate and deactivate the relevant buttons
        player.action(action_number);
        updateButtons(activate_side, true);
        updateButtons(deactivate_side, false);

        updateProgressBars();

        Player winning_player = checkWin();
        if (winning_player != null) {
            finishGame(winning_player);
        }
    }

    //choose the starting player via checking the pseudo-random values.
    private boolean chooseStartingPlayer(int left, int right) {
        return left > right;
    }

    //Initialize the progress bars
    private void initializeProgressBars() {
        game_BAR_leftPlayer.setMax(left_player.getHealth());
        game_BAR_leftPlayer.setProgress(left_player.getHealth());

        game_BAR_rightPlayer.setMax(right_player.getHealth());
        game_BAR_rightPlayer.setProgress(right_player.getHealth());
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

    private View.OnClickListener rollClickListener = new View.OnClickListener() {
        @Override
        public void onClick(final View view) {
            final Animation anim1 = AnimationUtils.loadAnimation(activity_game.this, R.anim.shake);
            final Animation anim2 = AnimationUtils.loadAnimation(activity_game.this, R.anim.shake);

            Random rand = new Random();
            final int left_value = rand.nextInt(6) + 1;
            final int right_value = rand.nextInt(6) + 1;

            final Animation.AnimationListener animationListener = new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    game_IMG_dice1.setVisibility(View.VISIBLE);
                    game_IMG_dice2.setVisibility(View.VISIBLE);
                    game_BTN_roll.setEnabled(false);
                }

                @Override
                public void onAnimationEnd(Animation animation) {

                    if (animation == anim1) {
                        int res = getResources().getIdentifier("dice_number" + left_value, "drawable", "com.example.mobileduel");
                        Glide.with(view).load(res).into(game_IMG_dice1);
                    } else if (animation == anim2) {
                        int res = getResources().getIdentifier("dice_number" + right_value, "drawable", "com.example.mobileduel");
                        Glide.with(view).load(res).into(game_IMG_dice2);
                    }


                    boolean isLeftStarting = chooseStartingPlayer(left_value, right_value);

                    if (left_value == right_value) {
                        Toast.makeText(activity_game.this,"DRAW! ROLL AGAIN!", Toast.LENGTH_SHORT).show();
                        game_BTN_roll.setEnabled(true);
                        return;
                    }

                    //Starting the Manual/Automatic game depending on who "won".
                    if (isLeftStarting) {
                        if (game_mode) {
                            updateButtons("left", true);
                            updateButtons("right", false);
                        } else {
                            playAutomatic(left_player);
                        }
                        Toast.makeText(activity_game.this,"LEFT PLAYER WON!", Toast.LENGTH_SHORT).show();
                    } else {
                        if (game_mode) {
                            updateButtons("right", true);
                            updateButtons("left", false);
                        } else {
                            playAutomatic(right_player);
                        }
                        Toast.makeText(activity_game.this,"RIGHT PLAYER WON!", Toast.LENGTH_SHORT).show();
                    }
                    game_BTN_roll.setVisibility(View.INVISIBLE);
                    setDicesInvisible();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

            };
            anim1.setAnimationListener(animationListener);
            anim2.setAnimationListener(animationListener);

            game_IMG_dice1.startAnimation(anim1);
            game_IMG_dice2.startAnimation(anim2);
        }
    };

    //Set dices to invisible after 1.5 seconds.
    private void setDicesInvisible() {
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                game_IMG_dice1.setVisibility(View.INVISIBLE);
                game_IMG_dice2.setVisibility(View.INVISIBLE);
                timer.cancel();
            }
        }, 1500);
    }

    //Update the progress bars.
    private void updateProgressBars() {
        game_BAR_leftPlayer.setProgress(left_player.getHealth());
        game_BAR_rightPlayer.setProgress(right_player.getHealth());

        updateProgressBarColors();
    }

    //Update the color of the progress bars if they are below 15 to RED, otherwise to GREEN.
    private void updateProgressBarColors() {
        if (game_BAR_leftPlayer.getProgress() <= 15) {
            game_BAR_leftPlayer.setProgressTintList(getColorStateList(R.color.red));
        } else {
            game_BAR_leftPlayer.setProgressTintList(getColorStateList(R.color.green));
        }
        if (game_BAR_rightPlayer.getProgress() <= 15) {
            game_BAR_rightPlayer.setProgressTintList(getColorStateList(R.color.red));
        } else {
            game_BAR_rightPlayer.setProgressTintList(getColorStateList(R.color.green));
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
        if (!game_mode) {
            updateButtons("Left", false);
            updateButtons("Right", false);
        }

        saveToSharedPreferences(p);

        Intent intent = new Intent(this, activity_finish.class);
        intent.putExtra("player_side", p.getSide());
        intent.putExtra("player_turns", p.getTurns());
        startActivity(intent);
        finish();
    }

    private void updateButtons(String side, boolean state) {
        if (side.equals("left")) {
            game_BTN_leftlight.setEnabled(state);
            game_BTN_leftheavy.setEnabled(state);
            game_BTN_leftheal.setEnabled(state);
        } else {
            game_BTN_rightlight.setEnabled(state);
            game_BTN_rightheavy.setEnabled(state);
            game_BTN_rightheal.setEnabled(state);
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

        game_BTN_roll.setOnClickListener(rollClickListener);
    }

    private void glideIMGs() {
        Glide.with(this).load(R.drawable.left_player).into(game_IMG_leftPlayer);
        Glide.with(this).load(R.drawable.right_player).into(game_IMG_rightPlayer);
        Glide.with(this).load(R.drawable.main_background).into(game_IMG_background);
        Glide.with(this).load(R.drawable.dice_number1).into(game_IMG_dice1);
        Glide.with(this).load(R.drawable.dice_number6).into(game_IMG_dice2);
    }

    private void findViews() {
        game_BTN_leftlight = findViewById(R.id.game_BTN_leftlight);
        game_BTN_leftheavy = findViewById(R.id.game_BTN_leftheavy);
        game_BTN_leftheal = findViewById(R.id.game_BTN_leftheal);
        game_BTN_rightlight = findViewById(R.id.game_BTN_rightlight);
        game_BTN_rightheavy = findViewById(R.id.game_BTN_rightheavy);
        game_BTN_rightheal = findViewById(R.id.game_BTN_rightheal);
        game_BTN_roll = findViewById(R.id.game_BTN_roll);

        game_IMG_leftPlayer = findViewById(R.id.game_IMG_leftPlayer);
        game_IMG_rightPlayer = findViewById(R.id.game_IMG_rightPlayer);
        game_IMG_background = findViewById(R.id.game_IMG_background);
        game_IMG_dice1 = findViewById(R.id.game_IMG_dice1);
        game_IMG_dice2 = findViewById(R.id.game_IMG_dice2);

        game_BAR_leftPlayer = findViewById(R.id.game_BAR_leftPlayer);
        game_BAR_rightPlayer = findViewById(R.id.game_BAR_rightPlayer);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    protected void onStop() {
        super.onStop();

        //if the runnable is active and the game mode is automatic, stop the runnable
        if (isRunnableActive && !game_mode) {
            automatic_handler.removeCallbacksAndMessages(automatic_runnable);
            isRunnableActive = false;
        }

        //stop playing music
        mediaPlayer.reset();
        mediaPlayer.release();

        Log.d("oof", "ON STOP");
    }

    @Override
    protected void onResume() {
        Log.d("oof", "ON RESUME");

        //If the runnable is not active and the game mode is automatic - start up the automatic runnable.
        if (!isRunnableActive && !game_mode) {
            isRunnableActive = true;
            if (automatic_handler != null) {
                automatic_handler.postDelayed(automatic_runnable, 1000);
            }
        }

        //play music
        if (!isMusicPlaying) {
            mediaPlayer = MediaPlayer.create(this, R.raw.activity_game_music);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
            isMusicPlaying = true;
        }

        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}