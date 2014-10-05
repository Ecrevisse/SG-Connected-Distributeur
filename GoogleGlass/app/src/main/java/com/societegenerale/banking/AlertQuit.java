package com.societegenerale.banking;

import com.google.android.glass.media.Sounds;
import com.google.android.glass.widget.CardBuilder;
import com.google.android.glass.widget.CardScrollAdapter;
import com.google.android.glass.widget.CardScrollView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;
/**
 * Created by Ul on 04/10/2014.
 */
public class AlertQuit  extends Activity
{
    private View _View;
    private GestureDetector _gestureDetector;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        CardBuilder card = new CardBuilder(this, CardBuilder.Layout.ALERT);
        card.setText("Swipe down pour quitter.");
        _View = card.getView();
        _gestureDetector = createGestureDetector(this);
        this.setContentView(_View);
    }

    private GestureDetector createGestureDetector(Context context)
    {
        GestureDetector detector = new GestureDetector(context);
        detector.setBaseListener(new GestureDetector.BaseListener() {
                                     @Override
                                     public boolean onGesture(Gesture gesture)
                                     {
                                         if (gesture == Gesture.SWIPE_DOWN) {
                                             Intent intent = new Intent(AlertQuit.this, ConnectionScreen.class);
                                             intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                             intent.putExtra("EXIT", true);
                                             startActivity(intent);
                                             return true;
                                         } else if (gesture == Gesture.TAP) {
                                             finish();
                                             return true;
                                         }
                                         return false;
                                     }
            }


        );
        return detector;
    }
    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {
        if (_gestureDetector != null) {
            return _gestureDetector.onMotionEvent(event);
        }
        return false;
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
