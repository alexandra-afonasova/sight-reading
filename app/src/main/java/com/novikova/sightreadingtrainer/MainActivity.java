package com.novikova.sightreadingtrainer;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends Activity {

    private List<String> notes;

    private static final String SAVED_NOTE_TAG = "note_tag";
    private static final String STREAK_TAG = "streak_tag";
    private static final String RESULT_TAG = "result_tag";
    private static final String BEST_RESULT_TAG = "best_result_tag";

    private static final int NUMBER_OF_NOTES = 26;
    private static final String DO = "do";
    private static final String RE = "re";
    private static final String MI = "mi";
    private static final String FA = "fa";
    private static final String SOL = "sol";
    private static final String LA = "la";
    private static final String SI = "si";

    private ImageView noteImage;
    private TypedArray images = null;
    private int index = -1;
    private TextView result;
    private TextView streak;
    private TextView bestResult;
    private int streakNumber;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        notes = new ArrayList<>(NUMBER_OF_NOTES);
        initializeNotesArray();
        initializeButtons(new OnAnswerClickListener());
        noteImage = (ImageView) findViewById(R.id.note_image);
        result = (TextView) findViewById(R.id.result);
        streak = (TextView) findViewById(R.id.streak);
        bestResult = (TextView) findViewById(R.id.best_result);
        sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        bestResult.setText(String.valueOf(sharedPreferences.getInt(BEST_RESULT_TAG, 0)));
        initializeNotesArray();
        if (savedInstanceState != null) {
            index = savedInstanceState.getInt(SAVED_NOTE_TAG);
            streakNumber = savedInstanceState.getInt(STREAK_TAG);
            streak.setText(String.valueOf(streakNumber));
            result.setText(savedInstanceState.getString(RESULT_TAG));
            if (images == null) {
                images = getResources().obtainTypedArray(R.array.notes);
            }
            noteImage.setImageResource(images.getResourceId(index, R.drawable.note0));
        } else {
            setRandomNoteImage();
        }
        initializeButtons(new OnAnswerClickListener());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVED_NOTE_TAG, index);
        outState.putInt(STREAK_TAG, streakNumber);
        outState.putString(RESULT_TAG, result.getText().toString());
    }

    private void initializeButtons(OnAnswerClickListener clickListener) {
        findViewById(R.id.c).setOnClickListener(clickListener);
        findViewById(R.id.d).setOnClickListener(clickListener);
        findViewById(R.id.e).setOnClickListener(clickListener);
        findViewById(R.id.f).setOnClickListener(clickListener);
        findViewById(R.id.g).setOnClickListener(clickListener);
        findViewById(R.id.a).setOnClickListener(clickListener);
        findViewById(R.id.b).setOnClickListener(clickListener);
    }

    private void initializeNotesArray() {
        notes = Arrays.asList(MI, DO, DO, DO, FA, FA, FA, FA, LA, LA, LA, LA, MI, MI, RE, RE, RE, SI, SI, SI, SI, SOL, SOL, SOL, SOL, SOL);
    }

    private void setRandomNoteImage() {
        if (images == null) {
            images = getResources().obtainTypedArray(R.array.notes);
        }
        index =  (int) (Math.random() * images.length());
        noteImage.setImageResource(images.getResourceId(index, R.drawable.note0));
    }

    private void incrementStreak() {
        streakNumber ++;
        streak.setText(String.valueOf(streakNumber));
    }

    private void cancelStreak() {
        if (sharedPreferences.getInt(BEST_RESULT_TAG, 0) < streakNumber) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(BEST_RESULT_TAG, streakNumber);
            editor.apply();
        }
        bestResult.setText(String.valueOf(streakNumber));
        streakNumber = 0;
        streak.setText(String.valueOf(streakNumber));
    }

    private class OnAnswerClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (index < 0) return;
            Button button = (Button) v;

            if (notes.get(index).compareToIgnoreCase(button.getText().toString()) == 0) {
                result.setText("Correct");
                incrementStreak();
            } else {
                result.setText("Wrong");
                cancelStreak();
            }
            setRandomNoteImage();
        }

    }

}
