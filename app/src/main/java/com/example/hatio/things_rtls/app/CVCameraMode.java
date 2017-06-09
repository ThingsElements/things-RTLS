package com.example.hatio.things_rtls.app;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hatio.things_rtls.R;
import com.example.hatio.things_rtls.assist.FirebaseSetValue;
import com.example.hatio.things_rtls.odometer.Camera;
import com.example.hatio.things_rtls.odometer.Odometer;
import com.firebase.client.Firebase;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.core.Mat;


public class CVCameraMode extends Fragment implements CvCameraViewListener2, SensorEventListener {

    private static final String TAG = "OCVSample::Activity";

    private CameraBridgeViewBase mOpenCvCameraView;
    private boolean  mIsJavaCamera = true;
    private MenuItem mItemSwitchCamera = null;
    private Camera mCamera = new Camera();
    private Odometer mOdometer = new Odometer(mCamera.getFocal(), mCamera.getPrinciplePoint());
    private TextView tvSensorYaw, tvSensorPitch, tvSensorRoll;
    private SensorManager mSensorManager;
    private Sensor mGyroscope;
    private double yaw = 0, pitch = 0, roll = 0;
    FirebaseSetValue firebaseSetValue;
    int count = 0;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);

        //센서 매니저 얻기
        mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        //자이로스코프 센서(회전)
        mGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mOpenCvCameraView.enableView();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.view_camera_mode, container, false);

        mOpenCvCameraView = (CameraBridgeViewBase) view.findViewById(R.id.camera_preview);
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);

        tvSensorYaw = (TextView)view.findViewById(R.id.tvSensorYaw);
        tvSensorPitch = (TextView)view.findViewById(R.id.tvSensorPitch);
        tvSensorRoll = (TextView)view.findViewById(R.id.tvSensorRoll);

        Firebase.setAndroidContext(getActivity());

        firebaseSetValue = new FirebaseSetValue("260");

        firebaseSetValue.getFirebase();

//        camera.set(CV_CAP_PROP_FRAME_WIDTH, 640);
//        camera.set(CV_CAP_PROP_FRAME_HEIGHT, 480);
//        camera.set(CV_CAP_PROP_AUTOFOCUS, 0);

        return view;
    }


    //센서값 얻어오기
    public void onSensorChanged(SensorEvent event) {



        count += 1;

        if(count >= 300) {
            Sensor sensor = event.sensor;
            if (sensor.getType() == Sensor.TYPE_GYROSCOPE) {
                yaw = yaw * 99 / 100 + (event.values[0] * 1000) * 1 / 100;
                pitch = pitch * 99 / 100 + (event.values[1] * 1000) * 1 / 100;
                roll = roll * 99 / 100 + (event.values[2] * 1000) * 1 / 100;
                tvSensorYaw.setText(String.format("%.1f", yaw));
                tvSensorPitch.setText(String.format("%.1f", pitch));
                tvSensorRoll.setText(String.format("%.1f", roll));

                firebaseSetValue.setPosition(yaw, pitch, roll, "sensor");
                count = 0;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onPause()
    {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();

        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onResume()
    {
        super.onResume();

        mSensorManager.registerListener(this, mGyroscope, SensorManager.SENSOR_DELAY_FASTEST);

    }

    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    public void onCameraViewStarted(int width, int height) {
    }

    public void onCameraViewStopped() {
    }

    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
        Mat gray = inputFrame.gray();
//        try{
//            mOdometer.estimate(gray, mCamera.getScale());
//        } catch(Throwable t) {
//            Log.e("ESTIMATE", t.getMessage());
//        }

        return gray;
    }
}
