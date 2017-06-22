package com.example.hatio.things_rtls.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.hatio.things_rtls.R;

/**
 * Created by hatio on 2017. 6. 2..
 */

public class Setting extends Fragment implements View.OnClickListener {

    EditText etURL;
    Button btnCalibration;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.view_setting, container, false);

        etURL = (EditText)view.findViewById(R.id.etURL);
        btnCalibration = (Button)view.findViewById(R.id.btnCalibration);


        btnCalibration.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCalibration:
                Intent i = new Intent(this.getContext(), Calibration.class);
                startActivity(i);
                break;
        }
    }


}
