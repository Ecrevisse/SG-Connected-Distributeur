package com.societegenerale.banking;

import com.google.android.glass.media.Sounds;
import com.google.android.glass.widget.CardBuilder;
import com.google.android.glass.widget.CardScrollAdapter;
import com.google.android.glass.widget.CardScrollView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;

import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;


public class PinActivity  extends Activity
{
    class NumberInterface {
        public Point    coord;
        public int      alpha;
        public int      size;

        NumberInterface(Point c, int a, int s)
        {
            coord = c;
            alpha = a;
            size = s;
        }
    }

    private GestureDetector _gestureDetector;

    private ArrayList<NumberInterface> _model;
    private ArrayList<TextView>        _row_one;
    private int                        _currentRow = 0;
    @Override

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        _gestureDetector = createGestureDetector(this);

        _model = new ArrayList<NumberInterface>();
        _model.add(0 , new NumberInterface(new Point(40, -21), 0  , 32));
        _model.add(1 , new NumberInterface(new Point(40, -21), 51 , 32));
        _model.add(2 , new NumberInterface(new Point(40, -21), 102, 32));
        _model.add(3 , new NumberInterface(new Point(40, -21), 153, 32));
        _model.add(4 , new NumberInterface(new Point(40, -21), 204, 40));
        _model.add(5 , new NumberInterface(new Point(40, -21), 255, 48));
        _model.add(6 , new NumberInterface(new Point(40, -21), 204, 40));
        _model.add(7 , new NumberInterface(new Point(40, -21), 153, 32));
        _model.add(8 , new NumberInterface(new Point(40, -21), 102, 32));
        _model.add(9 , new NumberInterface(new Point(40, -21), 51 , 32));
        _model.add(10, new NumberInterface(new Point(40, -21), 0  , 32));

        _row_one.add(0 , (TextView)findViewById(R.id.zerozero));
        _row_one.add(1 , (TextView)findViewById(R.id.zeroone));
        _row_one.add(2 , (TextView)findViewById(R.id.zerotwo));
        _row_one.add(3 , (TextView)findViewById(R.id.zerothree));
        _row_one.add(4 , (TextView)findViewById(R.id.zerofour));
        _row_one.add(5 , (TextView)findViewById(R.id.zerofive));
        _row_one.add(6 , (TextView)findViewById(R.id.zerosix));
        _row_one.add(7 , (TextView)findViewById(R.id.zeroseven));
        _row_one.add(8 , (TextView)findViewById(R.id.zeroeight));
        _row_one.add(9 , (TextView)findViewById(R.id.zeronine));
        _row_one.add(10, (TextView)findViewById(R.id.zeroten));

        setContentView(R.layout.pin_layout);
    }

    private GestureDetector createGestureDetector(Context context)
    {
        GestureDetector detector = new GestureDetector(context);
        detector.setBaseListener(new GestureDetector.BaseListener() {
                                     @Override
                                     public boolean onGesture(Gesture gesture) {
                                         if (gesture == Gesture.TAP)
                                         {
                                            ImageView img;
                                            if (_currentRow == 0)
                                            {
                                                img = (ImageView)findViewById(R.id.selection_row_1);
                                                img.setImageResource(R.drawable.case_pin);
                                                img = (ImageView)findViewById(R.id.selection_row_2);
                                                img.setImageResource(R.drawable.case_pin_selected);
                                            }
                                            else if (_currentRow == 1)
                                            {
                                                img = (ImageView)findViewById(R.id.selection_row_2);
                                                img.setImageResource(R.drawable.case_pin);
                                                img = (ImageView)findViewById(R.id.selection_row_3);
                                                img.setImageResource(R.drawable.case_pin_selected);
                                            }
                                            else if (_currentRow == 2)
                                            {
                                                img = (ImageView)findViewById(R.id.selection_row_3);
                                                img.setImageResource(R.drawable.case_pin);
                                                img = (ImageView)findViewById(R.id.selection_row_4);
                                                img.setImageResource(R.drawable.case_pin_selected);
                                            }
                                            else {
                                                Intent intent = new Intent(PinActivity.this,
                                                                           ConnectionScreen.class);
                                                startActivity(intent);
                                            }
                                            ++_currentRow;

                                         } else if (gesture == Gesture.SWIPE_RIGHT) {
                                             swipeForward();
                                             return true;
                                         } else if (gesture == Gesture.SWIPE_LEFT) {
                                             swipeBackward();
                                             return true;
                                         }
                                         return false;
                                     }
                                 }
        );
        return detector;
    }

    private void swipeBackward()
    {
        if (_currentRow == 0)
        {
            TextView erase = _row_one.remove(0);
            TextView newBlank = _row_one.get(0);
            _row_one.add(10, erase);
            erase.setText(newBlank.getText());
            for (int i = 0; i < 11; ++i)
            {
                TextView            tmp = _row_one.get(i);
                NumberInterface     model = _model.get(i);
                tmp.setAlpha(model.alpha);
                tmp.setTextSize(model.size);
                tmp.setX(model.coord.x);
                tmp.setY(model.coord.y);
            }
        }
    }

    private void swipeForward()
    {
        if (_currentRow == 0)
        {
            TextView erase = _row_one.remove(10);
            TextView newBlank = _row_one.get(9);
            _row_one.add(0, erase);
            erase.setText(newBlank.getText());
            for (int i = 0; i < 11; ++i)
            {
                TextView            tmp = _row_one.get(i);
                NumberInterface     model = _model.get(i);
                tmp.setAlpha(model.alpha);
                tmp.setTextSize(model.size);
                tmp.setX(model.coord.x);
                tmp.setY(model.coord.y);
            }
        }
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
