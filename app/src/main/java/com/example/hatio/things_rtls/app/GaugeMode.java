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
    private TextView tvValocity;
    private final Random RAND = new Random();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.view_gauge_mode, container, false);

        GaugeView gv = new GaugeView(view.getContext());
        mGaugeView = (GaugeView) view.findViewById(R.id.gauge_view1);
        tvValocity = (TextView)view.findViewById(R.id.tvValocityValue);

        mTimer.start();

        return view;
    }


    private final CountDownTimer mTimer = new CountDownTimer(300000, 2000) {

        @Override
        public void onTick(final long millisUntilFinished) {
            float rndSpeed = RAND.nextFloat() * 15f;

            mGaugeView.setTargetValue(rndSpeed);

            String str = String.format("%.1f",rndSpeed);

            tvValocity.setText(rndSpeed + "");
        }

        @Override
        public void onFinish() {}
    };
}
