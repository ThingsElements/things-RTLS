package com.example.hatio.things_rtls.app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.hatio.things_rtls.R;
import com.example.hatio.things_rtls.assist.GetCameraPermission;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;


public class MainActivity extends ActionBarActivity {
    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GetCameraPermission getCameraPermission = new GetCameraPermission(this);
        getCameraPermission.getCameraPermission();
    }

    // OpenCV 로드
    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.d("OpenCV", "OpenCV loaded successfully");
                } break;
                default:
                {
                    Log.d("OpenCV", "OpenCV loaded Fail");
                    super.onManagerConnected(status);
                } break;
            }
        }
    };

    @Override
    public void onResume()
    {
        super.onResume();

        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }

        Intent i = new Intent(this, MainTap.class);
        startActivity(i);
    }

}
