package com.dict.startuplab;

import androidx.appcompat.app.AppCompatActivity;

import android.icu.util.TimeZone;
import android.os.Bundle;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.os.Handler;
import android.os.SystemClock;
import android.content.pm.ActivityInfo;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements Runnable{

    private Thread              mWorker = null;
    private LinearLayout        mViewLayout = null;
    private TextView            mTimeView = null;
    private TextView            mDayView = null;
    private boolean            mRun = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mViewLayout = (LinearLayout) findViewById(R.id.view_layout);

        mDayView = (TextView)findViewById(R.id.day_view);
        mDayView.setText(String.format(""));

        mTimeView = (TextView)findViewById(R.id.time_view);
        mTimeView.setText(String.format(""));

        start();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stop();
    }

    void start()
    {
        if(mWorker!= null)
        {
            stop();
        }

        mRun = true;
        mWorker =  new Thread(this);
        mWorker.start();
    }

    void stop()
    {
        if(mWorker != null && mWorker.isAlive())
        {
            mRun = false;
            mWorker.interrupt();
            wait(1000);
            mWorker = null;
        }
    }

    Handler mHandler = new Handler();

    @Override
    public void run() {
        while (mRun) {
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"));

            //int yy = calendar.get(Calendar.YEAR);
            int mm = calendar.get(Calendar.MONTH) + 1;
            int dd = calendar.get(Calendar.DAY_OF_MONTH);
            int h = calendar.get(Calendar.HOUR_OF_DAY);
            int m = calendar.get(Calendar.MINUTE);
            //int s = calendar.get(Calendar.SECOND);

            // UI 업데이트를 메인 스레드로 보내기 위해 Handler를 사용합니다.
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mDayView.setText(String.format("%02d월 %02d일", mm, dd));
                    mTimeView.setText(String.format("%02d:%02d", h, m));
                }
            });

            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void wait(int ms){
        SystemClock.sleep(ms);
    }
}