package com.marco.marplex.schoolbook.fragments.tabs;


import android.animation.Animator;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import com.marco.marplex.schoolbook.R;
import com.marco.marplex.schoolbook.fragments.custom.PagerFragment;
import com.marco.marplex.schoolbook.reminds.SpeechParser;
import com.marco.marplex.schoolbook.utilities.SharedPreferences;
import com.marco.marplex.schoolbook.views.BreathingActionButton;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class Reminds extends PagerFragment implements RecognitionListener{

    private View rootView;

    @Bind(R.id.fab_record) BreathingActionButton mRecordButton;
    @Bind(R.id.rv_whatCanDo) RecyclerView mSuggests;
    @Bind(R.id.txt_hearing) TextView mHearingText;

    float alpha = 1f;

    private SpeechRecognizer recognizer;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            if(!SharedPreferences.loadBoolean(getContext(), "pref", "reminds")){
                AlertDialog dialog = new AlertDialog.Builder(getContext())
                        .setTitle("Benvenuto")
                        .setMessage("Reminds ti aiuterà nella tua gestione scolastica. " +
                                "Il nuovo assistente vocale ti renderà tutto più facile")
                        .setPositiveButton("Scopri", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        })
                        .setCancelable(false)
                        .create();

                dialog.show();
            }
            SharedPreferences.saveBoolean(getContext(),"pref", "reminds", true);
        }
    }

    @OnClick(R.id.fab_record)
    public void recordVoice(View view){

        //Set up
        mHearingText.setVisibility(View.VISIBLE);
        mHearingText.setAlpha(0f);

        //Animations
        mRecordButton.startBreathing();
        startAlphaAnimation();

        //Recording
        recognizer = SpeechRecognizer.createSpeechRecognizer(getContext());
        recognizer.setRecognitionListener(this);

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "it-IT");
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "it-IT");
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,5);
        recognizer.startListening(intent);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(mRecordButton.isBreathing())
                recognizer.stopListening();
                reset(false);
            }
        }, 5500);

    }

    private void startAlphaAnimation(){
        mHearingText.animate()
                .alpha(alpha)
                .setDuration(900)
                .setListener(new Animator.AnimatorListener() {
                    @Override public void onAnimationStart(Animator animator) { }
                    @Override public void onAnimationRepeat(Animator animator) { }
                    @Override  public void onAnimationCancel(Animator animator) { }
                    @Override public void onAnimationEnd(Animator animator) {
                        alpha = alpha == 1 ? 0f : 1f;
                        startAlphaAnimation();
                    }
                })
                .start();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_reminder, container, false);
        ButterKnife.bind(this, rootView);

        mHearingText.setVisibility(View.GONE);

        return rootView;

    }

    @Override public String getPageTitle() {
        return "Reminds";
    }

    @Override public void onReadyForSpeech(Bundle bundle) { }
    @Override public void onBeginningOfSpeech() { }
    @Override public void onRmsChanged(float v) { }
    @Override public void onBufferReceived(byte[] bytes) { }
    @Override public void onPartialResults(Bundle bundle) { }
    @Override public void onEvent(int i, Bundle bundle) { }
    @Override public void onError(int i) {
        switch (i) {
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                reset(false);
                recognizer.stopListening();
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                reset(false);
                recognizer.stopListening();
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                reset(false);
                recognizer.stopListening();
                break;
        }
    }

    @Override public void onEndOfSpeech() { }

    @Override public void onResults(Bundle bundle) {

        mRecordButton.animate()
                .alpha(0f)
                .setDuration(150)
                .start();

        //Stop the animations
        mRecordButton.stopBreathing();
        mHearingText.clearAnimation();
        //Set text visible
        mHearingText.setAlpha(1f);
        mHearingText.setVisibility(View.VISIBLE);
        //Slide up text
        mHearingText.animate()
                .yBy(-(mHearingText.getHeight() * 2))
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .setDuration(300)
                .start();
        //Reset starting values
        alpha = 1f;

        ArrayList datas = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        String txt =  datas.get(0).toString().substring(0, 1).toUpperCase() + datas.get(0).toString().substring(1);
        mHearingText.setText(txt);

        final SpeechParser parser = new SpeechParser(txt, datas, this);

        android.os.Handler handler = new android.os.Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!parser.parseAndGo()){
                    reset(true);
                }else{
                    reset(false);
                }
                recognizer.stopListening();
                recognizer.cancel();
                recognizer.destroy();
            }
        }, 800);
    }

    private void reset(boolean slideDownText){

        //Stop the animations
        mRecordButton.stopBreathing();
        mRecordButton.animate()
                .alpha(1f)
                .setDuration(150)
                .start();
        mHearingText.clearAnimation();

        //Reset starting values
        alpha = 1f;

        mHearingText.setVisibility(View.INVISIBLE);
        mHearingText.setAlpha(1f);

        float y = rootView.findViewById(R.id.txt_hearing).getTranslationY();
        mHearingText.animate()
                .yBy(slideDownText ? mHearingText.getHeight() * 2 : y)
                .setDuration(300)
                .start();

        mHearingText.setText("Sto ascoltando");

    }
}
