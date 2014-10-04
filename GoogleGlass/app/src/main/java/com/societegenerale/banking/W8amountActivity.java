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

public class W8amountActivity  extends Activity
{
    private View _View;
    private GestureDetector _gestureDetector;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        _gestureDetector = createGestureDetector(this);
        CardBuilder card = new CardBuilder(this, CardBuilder.Layout.AUTHOR);
        card.setText("Enter Amount.");
        _View = card.getView();
        Client myCientTask = Client.GetInstance();
        myCientTask.CallbackReceiveAmount = new Client.ClientCallbackReceiveAmount() {
            @Override
            public void callbackReceiveAmountOk() {
                Intent intent = new Intent(W8amountActivity.this, PinActivity.class);
                startActivity(intent);
            }
        };
        this.setContentView(_View);
    }

    @Override
    public boolean onKeyDown(int keycode, KeyEvent event)
    {
        if (keycode == KeyEvent.KEYCODE_DPAD_CENTER) {
            //Intent intent = new Intent(W8amountActivity.this, PinActivity.class);
            //startActivity(intent);
            return true;
        }
        return super.onKeyDown(keycode, event);
    }

    private GestureDetector createGestureDetector(Context context)
    {
        GestureDetector detector = new GestureDetector(context);
        detector.setBaseListener(new GestureDetector.BaseListener() {
                                     @Override
                                     public boolean onGesture(Gesture gesture) {
                                         if (gesture == Gesture.SWIPE_DOWN)
                                         {
                                             Intent intent = new Intent(W8amountActivity.this,
                                                     AlertQuit.class);
                                             startActivity(intent);
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