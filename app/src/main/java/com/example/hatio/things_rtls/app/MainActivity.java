package com.example.hatio.things_rtls.app;

import android.content.Intent;
import android.os.Bundle;

import com.example.hatio.things_rtls.R;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent i = new Intent(this, MainTap.class);
        startActivity(i);

    }
}
