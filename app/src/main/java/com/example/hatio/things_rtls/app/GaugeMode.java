package com.example.hatio.things_rtls.app;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hatio.things_rtls.R;
import com.example.hatio.things_rtls.assist.GaugeView;

import java.util.Random;


public class GaugeMode extends Fragment {

    private GaugeView mGaugeView;
    private TextView tvSpeed;
    private TextView tvAverageSpeed;
    private final Random RAND = new Random();
    private int averageSpeed = 0;
    private int count = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.view_gauge_mode, container, false);
    }



//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.view_gauge_mode);
//
//        GaugeView gv = new GaugeView(GaugeMode.this);
//
////        setContentView(gv);
//
//        mGaugeView = (GaugeView) findViewById(R.id.gauge_view1);
//        tvSpeed = (TextView) findViewById(R.id.tv_speed);
//        tvAverageSpeed = (TextView) findViewById(R.id.tv_average_speed);
//        mTimer.start();
//
//    }


    private final CountDownTimer mTimer = new CountDownTimer(300000, 1000) {

        @Override
        public void onTick(final long millisUntilFinished) {
            int rndSpeed = RAND.nextInt(61);
            averageSpeed += rndSpeed;
            count += 1;

            mGaugeView.setTargetValue(rndSpeed);
            tvSpeed.setText(rndSpeed + "km");
            tvAverageSpeed.setText((averageSpeed / count) + "km");
        }

        @Override
        public void onFinish() {}
    };
}
