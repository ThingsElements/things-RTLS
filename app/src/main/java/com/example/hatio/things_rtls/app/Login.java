package com.example.hatio.things_rtls.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.view.*;

import com.example.hatio.things_rtls.R;

/**
 * Created by hatio on 2017. 6. 2..
 */

public class Login extends Activity implements View.OnClickListener {

    EditText etEmail, etPassword;
    Button btnLogin, btnRegister, btnResetPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_login);

        etEmail = (EditText)findViewById(R.id.etEmail);
        etPassword = (EditText)findViewById(R.id.etPassword);
        btnLogin = (Button)findViewById(R.id.btnLogin);
        btnRegister = (Button)findViewById(R.id.btnRegister);
        btnResetPwd = (Button)findViewById(R.id.btnResetPwd);

        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:
                Intent i = new Intent(this, MainTap.class);
                startActivity(i);
                break;
        }
    }


}
