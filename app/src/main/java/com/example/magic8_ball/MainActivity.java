package com.example.magic8_ball;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    ImageView iv;
    TextView tv;
    long start=0L, end=0L, temp= 1L;
    String tempChangeTv;
    String []tabMagicBall;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iv = findViewById(R.id.ball);
        tv = findViewById(R.id.tv);
        tabMagicBall = getResources().getStringArray(R.array.magic_ball);

        SensorManager mySensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);

        Sensor lightSensor = mySensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if(lightSensor != null){
            tv.setText("");
            iv.setImageResource(R.drawable.hw3ball_front);
            mySensorManager.registerListener(
                    lightSensorListener,
                    lightSensor,
                    SensorManager.SENSOR_DELAY_NORMAL);

        } else {
            tv.setText("not available");
        }
    }

    private final SensorEventListener lightSensorListener
            = new SensorEventListener(){

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            if(event.sensor.getType() == Sensor.TYPE_LIGHT){
                if(event.values[0] == 0.0){
                    tempChangeTv = "temp";
                    Calendar calendar = Calendar.getInstance();
                    start = calendar.getTimeInMillis();
                    temp = start;
                    animation();
                }else if(event.values[0] != 0.0 && temp == start && !tv.getText().toString().equals(tempChangeTv)){
                    temp = end;
                    Calendar calendar = Calendar.getInstance();

                    end = calendar.getTimeInMillis();

                    int diff = (int)end - (int)start;

                    tempChangeTv = tv.getText().toString();
                    iv.setImageResource(R.drawable.hw3ball_empty);
                    randomSentence(diff);
                }
            }
        }
    };

    private void animation() {
        Animation img = new TranslateAnimation(Animation.ABSOLUTE, Animation.ABSOLUTE, 150, Animation.ABSOLUTE);
        img.setDuration(3000);
        img.setFillAfter(true);

        Animation temp = new CircularRotateAnimation(iv, 180);
        temp.setDuration(500);
        temp.setInterpolator(new LinearInterpolator());
        temp.setRepeatMode(Animation.RESTART);
        temp.setRepeatCount(Animation.INFINITE);

        tv.startAnimation(temp);
        iv.startAnimation(temp);
    }

    private void animationBack() {
        Animation img = new TranslateAnimation(Animation.ABSOLUTE, Animation.ABSOLUTE, Animation.ABSOLUTE, Animation.ABSOLUTE);
        img.setDuration(1000);
        img.setFillAfter(true);

        tv.startAnimation(img);
        iv.startAnimation(img);
    }

    public void randomSentence(int r){
        animationBack();
        int sentenceId = r%20;
        tv.setText(tabMagicBall[sentenceId]);
    }
}
