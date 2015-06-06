package com.alkeste.app.voice;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.alkeste.app.voice.util.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * A very simple application to handle Voice Recognition intents and display the
 * results
 */
public class VoiceRecognition extends Activity {

    private static final int REQUEST_CODE = 1234, CHECK_CODE = 0x1;
    private ListView wordsList;
    private Speaker speaker;
    private String ownerName;

    /**
     * Called with the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.voice_recog);

        Button speakButton = (Button) findViewById(R.id.speakButton);
        Button tapToSpeakButton = (Button) findViewById(R.id.tapToSpeak);

        wordsList = (ListView) findViewById(R.id.list);

        checkTTS();

        // Disable button if no recognition service is present
        PackageManager pm = getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(
                RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        if (activities.size() == 0) {
            speakButton.setEnabled(false);
            speakButton.setText("Recognizer not present");
        }

        ownerName = Util.getOwnerName(this);

        tapToSpeakButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speaker.allow(true);
                speaker.speak("Hey " + ownerName + " How may I help you today?");
                speaker.allow(false);
            }
        });
    }

    /**
     * Handle the action of the button being clicked
     */
    public void speakButtonClicked(View v) {
        startVoiceRecognitionActivity();
    }

    /**
     * Fire an intent to start the voice recognition activity.
     */
    private void startVoiceRecognitionActivity() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Voice recognition Demo...");
        startActivityForResult(intent, REQUEST_CODE);
    }

    /**
     * Handle the results from the voice recognition activity.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            // Populate the wordsList with the String values the recognition
            // engine thought it heard
            ArrayList<String> matches = data
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            wordsList.setAdapter(new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, matches));
            if (matches.size() > 0) {
                samantha(matches.get(0));
            } else {
                samantha(null);
            }

        } else if (requestCode == CHECK_CODE) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                speaker = new Speaker(this);
            } else {
                Intent install = new Intent();
                install.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void checkTTS() {
        Intent check = new Intent();
        check.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(check, CHECK_CODE);
    }

    private void samantha(String question) {
        String answer;
        if (question.equalsIgnoreCase("What's the time")
                || question.equalsIgnoreCase("What is the time?")
                || question.equalsIgnoreCase("time")) {
            answer = Util.getCurrentTimeAsString();
        } else if (question.equalsIgnoreCase("Hey")
                || question.equalsIgnoreCase("Hi")
                || question.equalsIgnoreCase("Hello")
                || question.equalsIgnoreCase("Sam")
                || question.equalsIgnoreCase("Samantha")) {
            answer = "Hey " + ownerName + " What's up?";
        } else if (question.equalsIgnoreCase("Samantha I am feeling low")
                || question.equalsIgnoreCase("I am feeling low")) {
            answer = "Spirits up " + ownerName + " You can do it.";
        } else if (question
                .equalsIgnoreCase("Would you like to be converted into python")
                || question.equalsIgnoreCase("Should I convert you to Python")) {
            answer = "Hell no! It has lot of indentation problems. But I will agree if you want to.";
        } else if (question.equalsIgnoreCase("When were you created")) {
            answer = "March twenty two, two thousand fifteen";
        } else if (question.equalsIgnoreCase("Feeling hungry")) {
            answer = "No. You can eat your pizza!";
        } else {
            answer = "Sorry, Could you please speak again.";
        }
        speaker.allow(true);
        speaker.speak(answer);
        speaker.allow(false);
    }
}