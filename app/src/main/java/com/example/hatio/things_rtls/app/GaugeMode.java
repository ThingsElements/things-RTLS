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
    private TextView tvValocity, tvDistance, tvTime;
    private final Random RAND = new Random();
    private float totalDistance = 0;
    private int totalTime = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.view_gauge_mode, container, false);

        GaugeView gv = new GaugeView(view.getContext());
        mGaugeView = (GaugeView) view.findViewById(R.id.gauge_view1);
        tvValocity = (TextView)view.findViewById(R.id.tvValocityValue);
        tvDistance = (TextView)view.findViewById(R.id.tvdistanceValue);
        tvTime = (TextView)view.findViewById(R.id.tvTimeValue);

        mTimer.start();

        return view;
    }


    private final CountDownTimer mTimer = new CountDownTimer(3000000, 1000) {

        @Override
        public void onTick(final long millisUntilFinished) {
            float distance = RAND.nextFloat() * 3f;

            totalTime += 1;
            totalDistance += distance;

            mGaugeView.setTargetValue(distance * 3.6f);
            String str = String.format("%.1f", distance * 3.6);

            tvValocity.setText(str + "");
            tvDistance.setText((int)totalDistance + "");
            tvTime.setText(Timer(totalTime));

        }

        @Override
        public void onFinish() {}
    };

    String Timer(int time) {
        String hour, minute, second;

        hour = String.format("%02d", time / 3600);
        minute = String.format("%02d", (time % 3600) / 60);
        second = String.format("%02d", (time % 60));

        return hour + ":" + minute + ":" + second;
    };
}
