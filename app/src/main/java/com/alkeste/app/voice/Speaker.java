package com.alkeste.app.voice;

import android.content.Context;
import android.media.AudioManager;
import android.speech.tts.TextToSpeech;

import java.util.HashMap;
import java.util.Locale;

/**
 * @author Kushagra on 24-03-2015.
 *
 * @class Speaker makes Samantha speak
 */
public class Speaker implements TextToSpeech.OnInitListener {
    private TextToSpeech tts;
    private boolean ready = false;
    private boolean allowed = false;

    public Speaker(Context context) {
        tts = new TextToSpeech(context, this);
    }

    public boolean isAllowed() {
        return allowed;
    }

    public void allow(boolean allowed) {
        this.allowed = allowed;
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            tts.setLanguage(Locale.US);
            ready = true;
        } else {
            ready = false;
        }
    }

    public void speak(String text) {
        if (ready && allowed) {
            HashMap<String, String> hash = new HashMap<String, String>();
            hash.put(TextToSpeech.Engine.KEY_PARAM_STREAM, String.valueOf(AudioManager.STREAM_NOTIFICATION));
            tts.setSpeechRate(1.0f);
            tts.speak(text, TextToSpeech.QUEUE_ADD, hash);
        }
    }

    public void pause(int duration) {
        tts.playSilence(duration, TextToSpeech.QUEUE_ADD, null);
        //    tts.playSilentUtterance(duration, TextToSpeech.QUEUE_ADD, null);
    }

    public void destroy() {
        tts.shutdown();
    }
}
