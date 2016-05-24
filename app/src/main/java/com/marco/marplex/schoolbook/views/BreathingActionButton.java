package com.marco.marplex.schoolbook.views;

import android.animation.Animator;
import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;

/**
 * Created by marco on 5/23/16.
 */

public class BreathingActionButton extends FloatingActionButton {

    float scaleX = 1f;
    float scaleY = 1f;

    private boolean isBreathing = false;

    public BreathingActionButton(Context context) {
        super(context);
    }

    public BreathingActionButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BreathingActionButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void startBreathing(){
        scaleX = scaleY = 1.2f;
        setScaleX(1f);
        setScaleY(1f);
        isBreathing = true;
        startBreathAnimation();
        setClickable(false);
    }

    public void stopBreathing(){
        stopBreathAnimation();
        scaleX = scaleY = 1f;
        setScaleX(1f);
        setScaleY(1f);
        setAlpha(1f);
        setClickable(true);
    }

    public boolean isBreathing(){
        return isBreathing;
    }

    private void startBreathAnimation(){
        if(isBreathing) {
            this.animate()
                    .scaleX(scaleX)
                    .scaleY(scaleY)
                    .setDuration(900)
                    .setListener(new Animator.AnimatorListener() {
                        @Override public void onAnimationStart(Animator animator) { }
                        @Override public void onAnimationRepeat(Animator animator) { }
                        @Override  public void onAnimationCancel(Animator animator) { }
                        @Override public void onAnimationEnd(Animator animator) {
                            if(!isBreathing) stopBreathing();
                            if (scaleX == 1.2f) scaleX = scaleY = 1f;
                            else scaleX = scaleY = 1.2f;
                            startBreathAnimation();
                        }
                    })
                    .start();
        }else{
            stopBreathing();
        }
    }

    private void stopBreathAnimation(){
        if(isBreathing) {
            this.clearAnimation();
            isBreathing = false;
        }
    }
}
