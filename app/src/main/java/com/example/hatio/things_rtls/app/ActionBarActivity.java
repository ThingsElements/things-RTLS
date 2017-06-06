package com.example.hatio.things_rtls.app;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.hatio.things_rtls.R;

/**
 * Created by hatio on 2017. 6. 5..
 */

public class ActionBarActivity extends AppCompatActivity {

    TextView tvTitle;

    ///// 상단 서브 메뉴 바 /////
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        ActionBar actionBar = getSupportActionBar();

        // Custom Actionbar를 사용하기 위해 CustomEnabled을 true 시키고 필요 없는 것은 false 시킨다
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);         //액션바 아이콘을 업 네비게이션 형태로 표시합니다.
        actionBar.setDisplayShowTitleEnabled(false);        //액션바에 표시되는 제목의 표시유무를 설정합니다.
        actionBar.setDisplayShowHomeEnabled(true);         //홈 아이콘을 숨김처리합니다.

        //layout을 가지고 와서 actionbar에 포팅을 시킵니다.
        LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        View actionbar = inflater.inflate(R.layout.action_bar, null);

        actionBar.setCustomView(actionbar);

        tvTitle = (TextView)actionbar.findViewById(R.id.title);

        //액션바 양쪽 공백 없애기
        Toolbar parent = (Toolbar)actionbar.getParent();
        parent.setContentInsetsAbsolute(0,0);

        menu.add(0, 0, Menu.NONE, "Edit Profile");
        menu.add(0, 1, Menu.NONE, "Alarm List");
        menu.add(0, 2, Menu.NONE, "Logout");


        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent i;
        switch (item.getItemId()) {
            case 0:
                i = new Intent(this, CVCameraMode.class);
                startActivity(i);
                break;
            case 1:
                break;
            case 2:
                i = new Intent(this, Login.class);
                startActivity(i);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setActionBarTitle(String title) {
        tvTitle.setText(title);
    }


}
