package com.marco.marplex.schoolbook.utilities;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;

import java.util.HashMap;
import java.util.Locale;

import static android.speech.tts.Voice.LATENCY_NORMAL;
import static android.speech.tts.Voice.QUALITY_VERY_HIGH;

/**
 * Created by marco on 5/22/16.
 */

public class TTS {
    private TextToSpeech tts;
    public TTS(Context c){
        tts = new TextToSpeech(c, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i==TextToSpeech.SUCCESS) {
                    tts.setLanguage(Locale.ITALY);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                        tts.setVoice(new Voice("My Voice", Locale.ITALY, QUALITY_VERY_HIGH, LATENCY_NORMAL, true, tts.getFeatures(Locale.ITALY)));
                }
            }
        });
    }

    public TTS talk(final String text){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) ttsGreater21(text);
                else ttsUnder20(text);
            }
        }, 800);
        return this;
    }

    /**
     * From http://stackoverflow.com/a/29777304
     */
    @SuppressWarnings("deprecation")
    private void ttsUnder20(String text) {
        HashMap<String, String> map = new HashMap<>();
        map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "MessageId");
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, map);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void ttsGreater21(String text) {
        String utteranceId=this.hashCode() + "";
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
    }

}
