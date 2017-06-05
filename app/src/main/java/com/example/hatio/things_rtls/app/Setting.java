package com.example.hatio.things_rtls.app;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.hatio.things_rtls.R;

import static com.example.hatio.things_rtls.R.id.btnLogin;

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
            case btnLogin:
                //TODO
//                Intent i = new Intent(this, MainTap.class);
//                startActivity(i);
                break;
        }
    }


}
