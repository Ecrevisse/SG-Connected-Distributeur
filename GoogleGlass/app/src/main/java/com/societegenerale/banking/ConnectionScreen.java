package com.societegenerale.banking;

import com.google.android.glass.media.Sounds;
import com.google.android.glass.widget.CardBuilder;
import com.google.android.glass.widget.CardScrollAdapter;
import com.google.android.glass.widget.CardScrollView;

import android.app.Activity;
import android.app.Application;
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
import android.view.WindowManager;
import android.widget.AdapterView;

import java.lang.reflect.Array;
import java.sql.Connection;
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
public class ConnectionScreen  extends Activity
{
    private View _View;
    private GestureDetector _gestureDetector;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Client.GetInstance().Stop();
        if (getIntent().getBooleanExtra("EXIT", false)) {
            Log.d("NORMAL QUIT\n\n","");
            finish();
            return;
        }
        CardBuilder card = new CardBuilder(this, CardBuilder.Layout.COLUMNS );
        card.setText("Entamez le retrait au distributeur.");
        card.addImage(R.drawable.titlescreencolumn_2);
        _View = card.getView();
        _gestureDetector = createGestureDetector(this);
        this.setContentView(_View);
        Client.GetInstance().setContext(getApplicationContext());
        Client.GetInstance().CallbackUniqueId = new Client.ClientCallbackUniqueId() {
            @Override
            public void callbackReceiveUniqueId(int uniqueId) {
                myUniqueId.GetInstance().UniqueId = uniqueId;
                Intent intent = new Intent(ConnectionScreen.this, IDConnectActivity.class);
                startActivity(intent);
            }
        };
        Client.GetInstance().CallbackReceiveTransactionStatus = new Client.ClientCallbackTransactionStatus() {
            @Override
            public void callbackReceiveTransactionStatus(boolean status) {
                if (status == false)
                {
                    Intent intent = new Intent(ConnectionScreen.this, ConnectionScreen.class);
                    startActivity(intent);
                }
            }
        };
        Client.GetInstance().execute();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //myClientTask.SendCode(9513);
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        //if (myClientTask != null)
        //    myClientTask.Stop();
    }

    @Override
    public boolean onKeyDown(int keycode, KeyEvent event) {
        if (keycode == KeyEvent.KEYCODE_DPAD_CENTER) {
            //Intent intent = new Intent(ConnectionScreen.this, IDConnectActivity.class);
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
                                             Intent intent = new Intent(ConnectionScreen.this,
                                                     AlertQuit.class);
                                             startActivity(intent);
                                             return true;
                                         }
                                         else if (gesture == Gesture.TAP)
                                         {
                                             Client.GetInstance().SendRequestUniqueId();
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
