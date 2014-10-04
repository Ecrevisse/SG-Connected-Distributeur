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
import android.util.TypedValue;
import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;

import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.Vector;


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

    class Velocity {
        public long time;
        public float velocity;

        Velocity(long t, float v)
        {
            time = t;
            velocity = v;
        }
    }


    private GestureDetector _gestureDetector;


    private ArrayList<ArrayList<NumberInterface>> _model;
    private ArrayList<ArrayList<TextView>>        _row;
    private int                                   _currentRow = 0;
    private ArrayList<Integer>                    _code;
    private Vector<Velocity>                      _velocities;
    private float                                 _tmpVelocity;
    private float                                 _velocityStep = 1.0f;
    private float                                 _tmpNegativeVelocity = -1.0f;
    @Override

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Log.d("Pin", "Init");
        setContentView(R.layout.pin_layout);

        _code = new ArrayList<Integer>();
        _velocities = new Vector<Velocity>();
        _row = new ArrayList<ArrayList<TextView>>();
        _tmpVelocity = 0.0f;
        _velocityStep = 1.0f;
        _gestureDetector = createGestureDetector(this);

        _model = new ArrayList<ArrayList<NumberInterface>>();
        _model.add(0 , new ArrayList<NumberInterface>());
        _model.add(1 , new ArrayList<NumberInterface>());
        _model.add(2 , new ArrayList<NumberInterface>());
        _model.add(3 , new ArrayList<NumberInterface>());
        _model.get(0).add(0 , new NumberInterface(new Point(40, -21), 0  , 32));
        _model.get(0).add(1 , new NumberInterface(new Point(40, 41),  51 , 32));
        _model.get(0).add(2 , new NumberInterface(new Point(40, 103), 102, 32));
        _model.get(0).add(3 , new NumberInterface(new Point(40, 165), 153, 32));
        _model.get(0).add(4 , new NumberInterface(new Point(40, 227), 204, 40));
        _model.get(0).add(5 , new NumberInterface(new Point(40, 289), 255, 48));
        _model.get(0).add(6 , new NumberInterface(new Point(40, 351), 204, 40));
        _model.get(0).add(7 , new NumberInterface(new Point(40, 413), 153, 32));
        _model.get(0).add(8 , new NumberInterface(new Point(40, 475), 102, 32));
        _model.get(0).add(9 , new NumberInterface(new Point(40, 537), 51 , 32));
        _model.get(0).add(10, new NumberInterface(new Point(40, 599), 0  , 32));

        _model.get(1).add(0 , new NumberInterface(new Point(112, -21), 0  , 32));
        _model.get(1).add(1 , new NumberInterface(new Point(112, 41),  51 , 32));
        _model.get(1).add(2 , new NumberInterface(new Point(112, 103), 102, 32));
        _model.get(1).add(3 , new NumberInterface(new Point(112, 165), 153, 32));
        _model.get(1).add(4 , new NumberInterface(new Point(112, 227), 204, 40));
        _model.get(1).add(5 , new NumberInterface(new Point(112, 289), 255, 48));
        _model.get(1).add(6 , new NumberInterface(new Point(112, 351), 204, 40));
        _model.get(1).add(7 , new NumberInterface(new Point(112, 413), 153, 32));
        _model.get(1).add(8 , new NumberInterface(new Point(112, 475), 102, 32));
        _model.get(1).add(9 , new NumberInterface(new Point(112, 537), 51 , 32));
        _model.get(1).add(10, new NumberInterface(new Point(112, 599), 0  , 32));

        _model.get(2).add(0 , new NumberInterface(new Point(184, -21), 0  , 32));
        _model.get(2).add(1 , new NumberInterface(new Point(184, 41),  51 , 32));
        _model.get(2).add(2 , new NumberInterface(new Point(184, 103), 102, 32));
        _model.get(2).add(3 , new NumberInterface(new Point(184, 165), 153, 32));
        _model.get(2).add(4 , new NumberInterface(new Point(184, 227), 204, 40));
        _model.get(2).add(5 , new NumberInterface(new Point(184, 289), 255, 48));
        _model.get(2).add(6 , new NumberInterface(new Point(184, 351), 204, 40));
        _model.get(2).add(7 , new NumberInterface(new Point(184, 413), 153, 32));
        _model.get(2).add(8 , new NumberInterface(new Point(184, 475), 102, 32));
        _model.get(2).add(9 , new NumberInterface(new Point(184, 537), 51 , 32));
        _model.get(2).add(10, new NumberInterface(new Point(184, 599), 0  , 32));

        _model.get(3).add(0 , new NumberInterface(new Point(256, -21), 0  , 32));
        _model.get(3).add(1 , new NumberInterface(new Point(256, 41),  51 , 32));
        _model.get(3).add(2 , new NumberInterface(new Point(256, 103), 102, 32));
        _model.get(3).add(3 , new NumberInterface(new Point(256, 165), 153, 32));
        _model.get(3).add(4 , new NumberInterface(new Point(256, 227), 204, 40));
        _model.get(3).add(5 , new NumberInterface(new Point(256, 289), 255, 48));
        _model.get(3).add(6 , new NumberInterface(new Point(256, 351), 204, 40));
        _model.get(3).add(7 , new NumberInterface(new Point(256, 413), 153, 32));
        _model.get(3).add(8 , new NumberInterface(new Point(256, 475), 102, 32));
        _model.get(3).add(9 , new NumberInterface(new Point(256, 537), 51 , 32));
        _model.get(3).add(10, new NumberInterface(new Point(256, 599), 0  , 32));

        _row.add(0, new ArrayList<TextView>());
        _row.add(1, new ArrayList<TextView>());
        _row.add(2, new ArrayList<TextView>());
        _row.add(3, new ArrayList<TextView>());
        _row.get(0).add(0, (TextView) findViewById(R.id.zerozero));
        _row.get(0).add(1, (TextView) findViewById(R.id.zeroone));
        _row.get(0).add(2, (TextView) findViewById(R.id.zerotwo));
        _row.get(0).add(3, (TextView) findViewById(R.id.zerothree));
        _row.get(0).add(4, (TextView) findViewById(R.id.zerofour));
        _row.get(0).add(5, (TextView) findViewById(R.id.zerofive));
        _row.get(0).add(6, (TextView) findViewById(R.id.zerosix));
        _row.get(0).add(7, (TextView) findViewById(R.id.zeroseven));
        _row.get(0).add(8, (TextView) findViewById(R.id.zeroeight));
        _row.get(0).add(9, (TextView) findViewById(R.id.zeronine));
        _row.get(0).add(10, (TextView) findViewById(R.id.zeroten));

        _row.get(1).add(0, (TextView) findViewById(R.id.onezero));
        _row.get(1).add(1, (TextView) findViewById(R.id.oneone));
        _row.get(1).add(2, (TextView) findViewById(R.id.onetwo));
        _row.get(1).add(3, (TextView) findViewById(R.id.onethree));
        _row.get(1).add(4, (TextView) findViewById(R.id.onefour));
        _row.get(1).add(5, (TextView) findViewById(R.id.onefive));
        _row.get(1).add(6, (TextView) findViewById(R.id.onesix));
        _row.get(1).add(7, (TextView) findViewById(R.id.oneseven));
        _row.get(1).add(8, (TextView) findViewById(R.id.oneeight));
        _row.get(1).add(9, (TextView) findViewById(R.id.onenine));
        _row.get(1).add(10, (TextView) findViewById(R.id.oneten));

        _row.get(2).add(0, (TextView) findViewById(R.id.twozero));
        _row.get(2).add(1, (TextView) findViewById(R.id.twoone));
        _row.get(2).add(2, (TextView) findViewById(R.id.twotwo));
        _row.get(2).add(3, (TextView) findViewById(R.id.twothree));
        _row.get(2).add(4, (TextView) findViewById(R.id.twofour));
        _row.get(2).add(5, (TextView) findViewById(R.id.twofive));
        _row.get(2).add(6, (TextView) findViewById(R.id.twosix));
        _row.get(2).add(7, (TextView) findViewById(R.id.twoseven));
        _row.get(2).add(8, (TextView) findViewById(R.id.twoeight));
        _row.get(2).add(9, (TextView) findViewById(R.id.twonine));
        _row.get(2).add(10, (TextView) findViewById(R.id.twoten));

        _row.get(3).add(0, (TextView) findViewById(R.id.threezero));
        _row.get(3).add(1, (TextView) findViewById(R.id.threeone));
        _row.get(3).add(2, (TextView) findViewById(R.id.threetwo));
        _row.get(3).add(3, (TextView) findViewById(R.id.threethree));
        _row.get(3).add(4, (TextView) findViewById(R.id.threefour));
        _row.get(3).add(5, (TextView) findViewById(R.id.threefive));
        _row.get(3).add(6, (TextView) findViewById(R.id.threesix));
        _row.get(3).add(7, (TextView) findViewById(R.id.threeseven));
        _row.get(3).add(8, (TextView) findViewById(R.id.threeeight));
        _row.get(3).add(9, (TextView) findViewById(R.id.threenine));
        _row.get(3).add(10, (TextView) findViewById(R.id.threeten));

        for (int j = 0; j < 4; ++j) {
            for (int i = 0; i < 11; ++i) {
                TextView tmp = _row.get(j).get(i);
                NumberInterface model = _model.get(j).get(i);
                tmp.setAlpha(model.alpha);
                tmp.setTextSize(TypedValue.COMPLEX_UNIT_PX, model.size);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) tmp.getLayoutParams();
                params.setMargins(model.coord.y, model.coord.x, 0, 0);
                tmp.setLayoutParams(params);
            }
        }
    }

    private GestureDetector createGestureDetector(Context context)
    {
        GestureDetector detector = new GestureDetector(context);
        Log.d("gesture", "INIT");
        detector.setBaseListener(new GestureDetector.BaseListener() {
                                     @Override
                                     public boolean onGesture(Gesture gesture) {
                                         Log.d("oneTap", "");
                                         if (gesture == Gesture.TAP)
                                         {
                                            ImageView img;
                                            if (_currentRow == 0)
                                            {
                                                img = (ImageView)findViewById(R.id.selection_row_1);
                                                img.setImageResource(R.drawable.case_pin);
                                                img = (ImageView)findViewById(R.id.selection_row_2);
                                                img.setImageResource(R.drawable.case_pin_selected);
                                                _code.add(Integer.parseInt(_row.get(_currentRow).get(5).getText().toString()));
                                            }
                                            else if (_currentRow == 1)
                                            {
                                                img = (ImageView)findViewById(R.id.selection_row_2);
                                                img.setImageResource(R.drawable.case_pin);
                                                img = (ImageView)findViewById(R.id.selection_row_3);
                                                img.setImageResource(R.drawable.case_pin_selected);
                                                _code.add(Integer.parseInt(_row.get(_currentRow).get(5).getText().toString()));
                                            }
                                            else if (_currentRow == 2)
                                            {
                                                img = (ImageView)findViewById(R.id.selection_row_3);
                                                img.setImageResource(R.drawable.case_pin);
                                                img = (ImageView)findViewById(R.id.selection_row_4);
                                                img.setImageResource(R.drawable.case_pin_selected);
                                                _code.add(Integer.parseInt(_row.get(_currentRow).get(5).getText().toString()));
                                            }
                                            else {
                                                _code.add(Integer.parseInt(_row.get(_currentRow).get(5).getText().toString()));
                                                Intent intent = new Intent(PinActivity.this,
                                                                           QuitActivity.class);
                                                startActivity(intent);
                                            }
                                            ++_currentRow;
                                             return true;

                                         }
                                         else if (gesture == Gesture.TWO_TAP) {
                                             Log.d("TwoTap", "");
                                             ImageView img;
                                             if (_currentRow == 1)
                                             {
                                                 img = (ImageView)findViewById(R.id.selection_row_2);
                                                 img.setImageResource(R.drawable.case_pin);
                                                 img = (ImageView)findViewById(R.id.selection_row_1);
                                                 img.setImageResource(R.drawable.case_pin_selected);
                                                 _code.remove(_code.size() - 1);
                                                 --_currentRow;
                                             }
                                             else if (_currentRow == 2)
                                             {
                                                 img = (ImageView)findViewById(R.id.selection_row_3);
                                                 img.setImageResource(R.drawable.case_pin);
                                                 img = (ImageView)findViewById(R.id.selection_row_2);
                                                 img.setImageResource(R.drawable.case_pin_selected);
                                                 _code.remove(_code.size() - 1);
                                                 --_currentRow;
                                             }
                                             else if (_currentRow == 3) {
                                                 img = (ImageView)findViewById(R.id.selection_row_4);
                                                 img.setImageResource(R.drawable.case_pin);
                                                 img = (ImageView)findViewById(R.id.selection_row_3);
                                                 img.setImageResource(R.drawable.case_pin_selected);
                                                 _code.remove(_code.size() - 1);
                                                 --_currentRow;
                                             }
                                         } else if (gesture == Gesture.SWIPE_DOWN)
                                         {
                                             Intent intent = new Intent(PinActivity.this,
                                                     AlertQuit.class);
                                             startActivity(intent);
                                             return true;
                                         }
                                         return false;
                                     }

                                 }
        );
        detector.setFingerListener(new GestureDetector.FingerListener() {
            @Override
            public void onFingerCountChanged(int previousCount, int currentCount) {
                // do something on finger count changes
            }
        });
        detector.setScrollListener(new GestureDetector.ScrollListener() {
            @Override
            public boolean onScroll(float displacement, float delta, float velocity) {
                long lDateTime = new Date().getTime();
                int count = 0;
                float addedVelocity = 0;
                for (int i = _velocities.size() - 1; i > 0; --i)
                {
                    if (lDateTime - _velocities.get(i).time > 1000)
                        _velocities.remove(i);
                    else
                    {
                        count++;
                        addedVelocity += _velocities.get(i).velocity / 5.0f;
                    }
                }
                count++;
                addedVelocity += velocity;
                _velocities.add(new Velocity(lDateTime, velocity));
                float moyenne = addedVelocity / (float)count;
                if (moyenne > 0)
                    _tmpVelocity += moyenne;
                else
                    _tmpNegativeVelocity += moyenne;

                if (_tmpVelocity > _velocityStep)
                {
                    swipeForward();
                    _tmpVelocity -= _velocityStep;
                }
                else if (_tmpNegativeVelocity < -_velocityStep)
                {
                    swipeBackward();
                    _tmpNegativeVelocity += _velocityStep;
                }
                return false;
            }
        });
        return detector;
    }

    /*
    * Send generic motion events to the gesture detector
    */
    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {
        if (_gestureDetector != null) {
            return _gestureDetector.onMotionEvent(event);
        }
        return false;
    }

    private void swipeForward()
    {
            TextView erase = _row.get(_currentRow).remove(0);
            TextView newBlank = _row.get(_currentRow).get(0);
            _row.get(_currentRow).add(10, erase);
            erase.setText(newBlank.getText());
            for (int i = 0; i < 11; ++i)
            {
                TextView            tmp = _row.get(_currentRow).get(i);
                NumberInterface     model = _model.get(_currentRow).get(i);
                //Log.d(tmp.getText() + " " + model.size , "" + model.alpha);
                tmp.setAlpha(model.alpha);
                tmp.setTextSize(TypedValue.COMPLEX_UNIT_PX, model.size);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)tmp.getLayoutParams();
                params.setMargins(model.coord.y, model.coord.x, 0, 0);
                tmp.setLayoutParams(params);
            }
    }

    private void swipeBackward()
    {
            TextView erase = _row.get(_currentRow).remove(10);
            TextView newBlank = _row.get(_currentRow).get(9);
            _row.get(_currentRow).add(0, erase);
            erase.setText(newBlank.getText());
            for (int i = 0; i < 11; ++i)
            {
                TextView            tmp = _row.get(_currentRow).get(i);
                NumberInterface     model = _model.get(_currentRow).get(i);
                tmp.setAlpha(model.alpha);
                tmp.setTextSize(TypedValue.COMPLEX_UNIT_PX, model.size);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)tmp.getLayoutParams();
                params.setMargins(model.coord.y, model.coord.x, 0, 0);
                tmp.setLayoutParams(params);
            }
        //}
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
