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
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.view.KeyEvent;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;

public class IDConnectActivity extends Activity
{
    private View    _View;
    private GestureDetector _gestureDetector;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        _gestureDetector = createGestureDetector(this);

        CardBuilder card = new CardBuilder(this, CardBuilder.Layout.TEXT);
        card.setText("Rentrez votre code de connection unique :\n" + myUniqueId.GetInstance().UniqueId);
        _View = card.getView();
        _View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        Client myClientTask = Client.GetInstance();
        myClientTask.CallbackReceiveIdOk = new Client.ClientCallbackReceiveIdOk() {
            @Override
            public void callbackReceiveIdOk() {
                Intent intent = new Intent(IDConnectActivity.this, W8amountActivity.class);
                startActivity(intent);
            }
        };
        myClientTask.CallbackReceiveTransactionStatus = new Client.ClientCallbackTransactionStatus() {
            @Override
            public void callbackReceiveTransactionStatus(boolean status) {
                if (status == false)
                {
                    Intent intent = new Intent(IDConnectActivity.this, ConnectionScreen.class);
                    startActivity(intent);
                }
            }
        };
        this.setContentView(_View);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

    }

    @Override
    public boolean onKeyDown(int keycode, KeyEvent event) {
        if (keycode == KeyEvent.KEYCODE_DPAD_CENTER) {
            //Intent intent = new Intent(IDConnectActivity.this, W8amountActivity.class);
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
                                             Intent intent = new Intent(IDConnectActivity.this,
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
