/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.ghost;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import java.util.StringTokenizer;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;


public class GhostActivity extends AppCompatActivity {
    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN = "Your turn";
    private GhostDictionary dictionary;
    private boolean userTurn = false;
    private Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghost);
        AssetManager assetManager = getAssets();

        try {
            dictionary = new FastDictionary(assetManager.open("words.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        onStart(null);
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
     *
     * @param view
     * @return true
     */
    public boolean onStart(View view) {
        userTurn = random.nextBoolean();

        TextView text = (TextView) findViewById(R.id.ghostText);
        text.setText("");
        TextView label = (TextView) findViewById(R.id.gameStatus);

        if (userTurn) {
            label.setText(USER_TURN);
        } else {
            label.setText(COMPUTER_TURN);

            computerTurn();
        }
        return true;
    }

    private void computerTurn() {
        TextView text = (TextView) findViewById(R.id.ghostText);
        TextView label = (TextView) findViewById(R.id.gameStatus);
        // Do computer turn stuff then make it the user's turn again

        String currentWord = text.getText().toString();
        String nextWord = dictionary.getAnyWordStartingWith(currentWord);

        if (currentWord.length() >= dictionary.MIN_WORD_LENGTH && dictionary.isWord(currentWord))
            Toast.makeText(this, "Computer Won!!!", Toast.LENGTH_SHORT).show();

        else if (nextWord == null || nextWord.equals(currentWord)){
            // challange user
            challenge(null);
        } else {
            Toast.makeText(this, nextWord, Toast.LENGTH_SHORT).show();
            nextWord = nextWord.substring(0, currentWord.length() + 1);
            text.setText(nextWord);
        }

        userTurn = true;
        label.setText(USER_TURN);
    }

    /**
     * Handler for user key presses.
     *
     * @param keyCode
     * @param event
     * @return whether the key stroke was handled.
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        // A = 29 , Z = 54
        if (keyCode >= 29 && keyCode <= 54) {

            TextView text = (TextView) findViewById(R.id.ghostText);
            TextView label = (TextView) findViewById(R.id.gameStatus);

            String currentWord = text.getText().toString();
            String pressedChar = String.valueOf((char) event.getUnicodeChar());
            currentWord += pressedChar;

            text.setText(currentWord);
            userTurn = false;
            computerTurn();
        }
        return super.onKeyUp(keyCode, event);
    }

    public void challenge(View view){
        TextView text = (TextView) findViewById(R.id.ghostText);
        TextView label = (TextView) findViewById(R.id.gameStatus);

        String currentWord = text.getText().toString();

        if (currentWord.length() >= dictionary.MIN_WORD_LENGTH && dictionary.isWord(currentWord)) {
            if (userTurn)
                Toast.makeText(this, "You Won!!! - As Computer Completed the word", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "Computer Won!!! - As you completed the word", Toast.LENGTH_SHORT).show();
        } else {
            if (dictionary.getAnyWordStartingWith(currentWord) != null){
                if (userTurn)
                    Toast.makeText(this, "Computer Won!!! - As a word can be formed from string", Toast.LENGTH_SHORT).show();

                // this condition never comes
                else
                    Toast.makeText(this, "You Won!!! - As a word can be formed from string", Toast.LENGTH_SHORT).show();
            } else {
                // this condition never comes
                if (userTurn)
                    Toast.makeText(this, "You Won!!! - As a word can not be formed from string", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, "Computer Won!!! - As a word can not be formed from string", Toast.LENGTH_SHORT).show();
            }
        }

    }
}


