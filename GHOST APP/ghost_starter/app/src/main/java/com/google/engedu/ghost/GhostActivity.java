package com.google.engedu.ghost;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;


public class GhostActivity extends AppCompatActivity {
    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN = "Your turn";
    private GhostDictionary dictionary;
    private SimpleDictionary simpledict;
    private boolean userTurn = false;
    private Random random = new Random();
    private TextView tv_word,tv_status;
    private Button btn_challenge,btn_restart;
    private  Handler h = new Handler();
    private boolean b;

    private boolean firstcomputer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghost);
        AssetManager assetManager = getAssets();
        btn_challenge = (Button)findViewById(R.id.btn_challenge);
        btn_restart = (Button)findViewById(R.id.btn_restart);
        try {
            InputStream inputStream = assetManager.open("words.txt");
            simpledict = new SimpleDictionary(inputStream);
            //dictionary = new FastDictionary(inputStream);
        } catch (IOException e) {
            Toast toast = Toast.makeText(this, "Could not load dictionary", Toast.LENGTH_SHORT);
            toast.show();
        }
       if(!b)
            onStart(null);
        btn_restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onStart(view);
                firstcomputer = false;
                Toast.makeText(getApplicationContext(),"NEW GAME",Toast.LENGTH_SHORT).show();
            }
        });

        btn_challenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                h = new Handler();
                String ans = simpledict.getAnyWordStartingWith(tv_word.getText().toString());
                if(tv_word.getText().length()>=4 && simpledict.isWord(tv_word.getText().toString())) // challenge for perfect word
                {
                    tv_status.setText(Html.fromHtml(String.format("<font color=%s>%s</font>","#29cc00","YOU WON")));
                    Toast.makeText(getApplicationContext(),"You found the word : "+tv_word.getText(),Toast.LENGTH_SHORT).show();
                    h.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            btn_restart.performClick();
                        }
                    },2000);
                }
                else if(ans != null) //more words are there
                {
                    tv_status.setText(Html.fromHtml(String.format("<font color=%s>%s</font>","#cc0029","COM WON")));
                    Toast.makeText(getApplicationContext(),"Sorry, this is a word : "+tv_word.getText(),Toast.LENGTH_SHORT).show();
                    h.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            btn_restart.performClick();
                        }
                    },2000);
                }
                //computer will never add wrond letter s don't need to implement it

            }
        });
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        Log.d("turn","user");
        if(keyCode<=54 && keyCode>=29)
        {
            char ch = (char)event.getUnicodeChar();
            tv_word.append(ch+"");
            computerTurn();
        }

        return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ghost, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Handler for the "Reset" button.
     * Randomly determines whether the game starts with a user turn or a computer turn.
     * @param view
     * @return true
     */



    public boolean onStart(View view) {
        userTurn = random.nextBoolean();
        tv_word = (TextView) findViewById(R.id.tv_word);
        tv_word.setText("");
        tv_status = (TextView) findViewById(R.id.tv_gamestatus);
        if (userTurn) {
            tv_status.setText(USER_TURN);
        } else {
            tv_status.setText(COMPUTER_TURN);
            firstcomputer = true;
            computerTurn();
        }
        return true;
    }

    private void computerTurn() {
        Log.d("turn","computer");
        // Do computer turn stuff then make it the user's turn again
        String returnvalue = simpledict.getAnyWordStartingWith(tv_word.getText().toString());
        Log.d("isvalidcomrun",simpledict.isWord(tv_status.getText().toString())+"");
        Log.d("withouttrim",tv_word.getText().toString());
        if(tv_word.getText().length()>=4 && simpledict.isWord(String.valueOf(tv_word.getText().toString()))) //challenge - 1
        {
            tv_status.setText(Html.fromHtml(String.format("<font color=#cc0031>%s</font>","COM WON")));
            Toast.makeText(getApplicationContext(),"This is a word : "+tv_word.getText(),Toast.LENGTH_SHORT).show();
            h.postDelayed(new Runnable() {
                @Override
                public void run() {
                    btn_restart.performClick();
                }
            },2000);
        }
        else if(returnvalue==null) { //challenge - 2
            tv_status.setText(Html.fromHtml(String.format("<font color=#cc0031>%s</font>","COM WON")));
            Toast.makeText(getApplicationContext(), "No word with this prefix exist.", Toast.LENGTH_SHORT).show();
            h.postDelayed(new Runnable() {
                @Override
                public void run() {
                    btn_restart.performClick();
                }
            }, 2000);

        }
        else {
            //find good word for append
            Log.d("tv_word",tv_word.getText().toString());
                if(tv_word.getText().length()>2)
                {
                   String ans = simpledict.getGoodWordStartingWith(tv_word.getText().toString());
                   if(ans!=null)
                       tv_word.append(String.valueOf(ans.charAt(tv_word.length())));
                    else
                       tv_word.append(String.valueOf(returnvalue.charAt(tv_word.length())));
                }
                else
                {
                    tv_word.append(String.valueOf(returnvalue.charAt(tv_word.length())));
                    tv_status.setText(USER_TURN);
                }

        }
    }

    //save state
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        Log.d("suspended","saving");
        savedInstanceState.putString("status", tv_status.getText().toString());
        savedInstanceState.putString("word", tv_word.getText().toString());

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);
        Log.d("foundsavedinstance","yes");
        tv_status.setText(state.getString("status"));
        tv_word.setText(state.getString("word"));
        b=true; //done
        // Restore View's state here
    }


}
