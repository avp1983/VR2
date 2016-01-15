package ru.jtconsulting.voicerecognition2;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ViewFlipper;
import  java.lang.Math;

/**
 * Created by www on 12.01.2016.
 */
public class Slider implements  View.OnTouchListener{
    private ViewFlipper flipper = null;
    private float fromPosition;
    private MainActivity context;
    /**
     * Чувствительность
     */
    private float delta = 50;
    private boolean skipTouch(float fromPosition, float toPosition){
        if (Math.abs(fromPosition-toPosition)<delta) {
            return true;
        } else {
            return false;
        }
    }
    public Slider(MainActivity Context) {
        context = Context;
        flipper = (ViewFlipper)   context.findViewById(R.id.flipper);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        int layouts[] = new int[]{ R.layout.activity_1, R.layout.activity_2, R.layout.activity_3};
        for (int layout : layouts)
            flipper.addView(inflater.inflate(layout, null));


    }

    public boolean onTouch(View view, MotionEvent event)
    {

        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                fromPosition = event.getX();

                break;
            case MotionEvent.ACTION_UP:
                float toPosition = event.getX();
                if (skipTouch(fromPosition,toPosition)) return true;
                context.onSliding();


                if (fromPosition > toPosition)
                {
                    flipper.setInAnimation(AnimationUtils.loadAnimation(context, R.anim.go_next_in));
                    flipper.setOutAnimation(AnimationUtils.loadAnimation(context,R.anim.go_next_out));
                    flipper.showNext();
                }
                else if (fromPosition < toPosition)
                {
                    flipper.setInAnimation(AnimationUtils.loadAnimation(context,R.anim.go_prev_in));
                    flipper.setOutAnimation(AnimationUtils.loadAnimation(context,R.anim.go_prev_out));
                    flipper.showPrevious();
                }
            default:
                break;
        }
        return true;
    }

}
