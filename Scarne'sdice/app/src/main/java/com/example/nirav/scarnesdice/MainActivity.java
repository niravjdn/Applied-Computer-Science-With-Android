package com.example.nirav.scarnesdice;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;
import java.util.logging.LogRecord;

public class MainActivity extends AppCompatActivity {

    private static int USER1_SCORE = 0;
    private static int USER2_SCORE = 0;
    private static int USER1_TURN = 0;
    private static int USER2_TURN = 0;
    private static boolean userturn = true;
    private static Button btn_roll, btn_hold, btn_reset;
    private static TextView tv_status;
    private static ImageView iv_dice;
    private Animation animFadein;
    private android.os.Handler timerHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_roll = (Button) findViewById(R.id.btn_roll);
        btn_hold = (Button) findViewById(R.id.btn_hold);
        btn_reset = (Button) findViewById(R.id.btn_reset);
        iv_dice = (ImageView) findViewById(R.id.imageView);
        tv_status = (TextView) findViewById(R.id.textView);
        tv_status.setText("Your SCORE = " + USER1_SCORE + " COM SCORE = " + USER2_SCORE + " TURN SCORE =" + USER1_TURN);
        btn_hold.setEnabled(false);
        animFadein = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.anim);



        btn_roll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_hold.setEnabled(true);
                int dicenumber = new Random().nextInt(6) + 1;
                int img_res_id = getResources().getIdentifier("@drawable/dice" + dicenumber, null, getPackageName());
                iv_dice.startAnimation(animFadein);
                iv_dice.setImageDrawable(getResources().getDrawable(img_res_id));
                if (dicenumber != 1) {
                    USER1_TURN += dicenumber;
                    tv_status.setText("Your SCORE = " + USER1_SCORE + " COM SCORE = " + USER2_SCORE + " TURN SCORE =" + USER1_TURN);
                } else {
                    USER1_TURN = 0;
                    tv_status.setText("Your SCORE = " + USER1_SCORE + " COM SCORE = " + USER2_SCORE + " TURN SCORE =" + USER1_TURN);
                    btn_hold.performClick();
                }
            }
        });

        btn_hold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                USER1_SCORE += USER1_TURN;
                Log.i("user", USER1_TURN + "");
                USER1_TURN = 0;

                if (USER1_SCORE >= 100) {
                    Toast.makeText(getApplicationContext(), "YOU WON", Toast.LENGTH_LONG).show();
                    btn_reset.performClick();
                } else {
                    tv_status.setText("Your SCORE = " + USER1_SCORE + " COM SCORE = " + USER2_SCORE + " TURN SCORE =" + USER1_TURN);
                    Toast.makeText(getApplicationContext(), "COM TURN", Toast.LENGTH_LONG).show();
                    computerTurn();
                }
            }
        });

        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_roll.setEnabled(true);
                USER1_SCORE = USER2_SCORE = USER1_TURN = USER2_TURN = 0;
                tv_status.setText("Your SCORE = " + USER1_SCORE + " COM SCORE = " + USER2_SCORE + " TURN SCORE =" + USER1_TURN);
                Toast.makeText(getApplicationContext(), "NEW GAME", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void computerTurn() {
        btn_roll.setEnabled(false);
        btn_hold.setEnabled(false);

        final Handler myHandler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                int dicenumber = new Random().nextInt(6) + 1;
                int img_res_id = getResources().getIdentifier("@drawable/dice" + dicenumber, null, getPackageName());
                iv_dice.startAnimation(animFadein);
                iv_dice.setImageDrawable(getResources().getDrawable(img_res_id));

                if (dicenumber != 1) {
                    USER2_TURN += dicenumber;

                    if(USER2_SCORE+USER2_TURN>100 || USER2_TURN>10) {
                        myHandler.removeCallbacks(this);
                        btn_roll.setEnabled(true);
                        USER2_SCORE += USER2_TURN;
                        Toast.makeText(getApplicationContext(), "YOUR TURN", Toast.LENGTH_LONG).show();
                    }
                    else if(USER2_TURN<=10) {
                        myHandler.postDelayed(this, 700);
                    }
                }
                else {
                    USER2_TURN = 0;
                    btn_roll.setEnabled(true);
                    Toast.makeText(getApplicationContext(), "YOUR TURN", Toast.LENGTH_LONG).show();
                }
                tv_status.setText("Your SCORE = " + USER1_SCORE + " COM SCORE = " + USER2_SCORE + " COM TURN =" + USER2_TURN);
                if (USER2_SCORE >= 100) {
                    Toast.makeText(getApplicationContext(), "COMPUTER WON", Toast.LENGTH_LONG).show();
                    btn_reset.performClick();
                }
            }
        };
        myHandler.postDelayed(runnable,700);
        Log.i("com", USER2_TURN + "");
        USER2_TURN = 0;


    }
}
