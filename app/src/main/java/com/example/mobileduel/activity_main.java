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

import com.bumptech.glide.Glide;

//TODO: activity after game that tells how many actions were used and who won (ONLY AUTOMATIC)
//TODO: top 10 record layout with a MAP in the records layout
//TODO: sounds / music
//TODO: save data to sharedPreferences

public class activity_main extends AppCompatActivity {

    private Button main_BTN_play;

    private SwitchCompat main_SWT_mode;

    private ImageView main_IMG_background;

    private TextView main_LBL_auto;
    private TextView main_LBL_manual;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        glideIMGs();

        main_BTN_play.setOnClickListener(onClickListener);

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
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (main_SWT_mode.isChecked()) {
                playManual();
            } else {
                playAutomatic();
            }
        }
    };

    //Play the automatic version of the game, exercise 2.
    private void playAutomatic() {
        //TODO: Automatic mode
    }

    //Play the manual version of the game, exercise 1.
    private void playManual() {
        Intent intent = new Intent(this, activity_manualGame.class);
        startActivity(intent);
    }
}
