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
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;

import android.view.LayoutInflater;
import android.widget.ImageView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;

/**
 * Created by Ul on 04/10/2014.
 */
public class PinActivity  extends Activity
{
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.pin_layout);

        ImageView img = (ImageView)findViewById(R.id.selection_row_1);
        img.setImageResource(R.drawable.case_pin_selected);
        img = (ImageView)findViewById(R.id.selection_row_2);
        img.setImageResource(R.drawable.case_pin);
        img = (ImageView)findViewById(R.id.selection_row_3);
        img.setImageResource(R.drawable.case_pin);
        img = (ImageView)findViewById(R.id.selection_row_4);
        img.setImageResource(R.drawable.case_pin);
    }

    @Override
    public boolean onKeyDown(int keycode, KeyEvent event) {
        if (keycode == KeyEvent.KEYCODE_DPAD_CENTER) {
            //Intent intent = new Intent(PinActivity.this, AccountActivity.class);
            //startActivity(intent);
            return true;
        }
        return super.onKeyDown(keycode, event);
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
