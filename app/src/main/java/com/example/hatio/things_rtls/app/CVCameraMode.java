package com.example.hatio.things_rtls.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hatio.things_rtls.R;
import com.example.hatio.things_rtls.assist.FirebaseSetValue;
import com.example.hatio.things_rtls.assist.Position;
import com.example.hatio.things_rtls.madgwickAHRS.MadgwickAHRS;
import com.example.hatio.things_rtls.odometer.Camera;
import com.example.hatio.things_rtls.odometer.Odometer;
import com.example.hatio.things_rtls.odometer.Preview;
import com.firebase.client.Firebase;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;

import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.MODE_PRIVATE;


public class CVCameraMode extends Fragment implements CvCameraViewListener2, SensorEventListener {

    private CameraBridgeViewBase mOpenCvCameraView;
    private TextView tvSensorYaw, tvSensorPitch, tvSensorRoll, tvSensorX, tvSensorY;
    private SensorManager mSensorManager;
    private Sensor accSensor, gyroSensor, magSensor;
    FirebaseSetValue firebaseSetValue;

    private float[] gyro = new float[3];
    private float[] magnet = new float[3];
    private float[] accel = new float[3];

    MadgwickAHRS madgwickAHRS = new MadgwickAHRS(0.01f, 0.041f);
    private Timer madgwickTimer = new Timer();
    private Position avgPosition = new Position();
    private long lastUpdate;
    String phoneUUid = "sample";

    ///// Odometer /////
    private static Camera mCamera = new Camera();
    private Odometer mOdometer;
    private Preview mPreview;
    private boolean check = false;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);

        //센서 매니저 얻기
        mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        //자이로스코프 센서(회전)
        gyroSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        accSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        mSensorManager.registerListener(this, accSensor, 0);//every 5ms
        mSensorManager.registerListener(this, gyroSensor, 0);
        mSensorManager.registerListener(this, magSensor, 0);

        mOpenCvCameraView.enableView();
        madgwickTimer.scheduleAtFixedRate(new DoMadgwick(), 1000, 1000);

        // 카메라 조정 값 가져오기
        SharedPreferences prefs = this.getActivity().getSharedPreferences("camera_setting", MODE_PRIVATE);
        double focal = Double.parseDouble(prefs.getString("focal", "13841.24403"));
        double principlePoint1 = Double.parseDouble(prefs.getString("principlePoint1", "639.5"));
        double principlePoint2 = Double.parseDouble(prefs.getString("principlePoint2", "479.5"));

        Camera.setFocal(focal);
        Camera.setPrinciplePoint(new Point(principlePoint1, principlePoint2));
    }


    // onCreateView가 onActivityCreated 보다 먼저 생성됨
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_camera_mode, container, false);

        mOpenCvCameraView = (CameraBridgeViewBase) view.findViewById(R.id.camera_preview);
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setMaxFrameSize(1280, 960);
        mOpenCvCameraView.setCvCameraViewListener(this);

        tvSensorYaw = (TextView)view.findViewById(R.id.tvSensorYaw);
        tvSensorPitch = (TextView)view.findViewById(R.id.tvSensorPitch);
        tvSensorRoll = (TextView)view.findViewById(R.id.tvSensorRoll);
        tvSensorX = (TextView)view.findViewById(R.id.tvSensorX);
        tvSensorY = (TextView)view.findViewById(R.id.tvSensorY);

        Firebase.setAndroidContext(getActivity());
        firebaseSetValue = new FirebaseSetValue("260");
        firebaseSetValue.getFirebase();

        return view;
    }


    /////////  센서에 관한 로직  /////////
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(event.values, 0, accel, 0, 3);
        } else if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            System.arraycopy(event.values, 0, gyro, 0, 3);
        } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            System.arraycopy(event.values, 0, magnet, 0, 3);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();

        if (mCamera != null) {
            mCamera = null;
        }

        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, gyroSensor,SensorManager.SENSOR_DELAY_FASTEST);
        mSensorManager.registerListener(this, accSensor,SensorManager.SENSOR_DELAY_FASTEST);
        mSensorManager.registerListener(this, magSensor,SensorManager.SENSOR_DELAY_GAME);

    }

    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    class DoMadgwick extends TimerTask {

        public void run() {
            Long now = System.currentTimeMillis();
            madgwickAHRS.SamplePeriod = (now - lastUpdate) / 1000.0f;
            lastUpdate = now;
            madgwickAHRS.Update(gyro[0], gyro[1], gyro[2], accel[0], accel[1],
                    accel[2], magnet[0], magnet[1], magnet[2]);

            try {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        tvSensorX.setText(String.format("%.1f", avgPosition.getCenterX()));
                        tvSensorY.setText(String.format("%.1f", avgPosition.getCenterY()));
                        tvSensorYaw.setText(String.format("%.1f", Math.toRadians(madgwickAHRS.MadgYaw)));
                        tvSensorPitch.setText(String.format("%.1f", Math.toRadians(madgwickAHRS.MadgPitch)));
                        tvSensorRoll.setText(String.format("%.1f", Math.toRadians(madgwickAHRS.MadgRoll)));
                    }
                });

                firebaseSetValue.setPosition(avgPosition.getCenterX(), avgPosition.getCenterY(), Math.toRadians(madgwickAHRS.MadgPitch),
                                                Math.toRadians(madgwickAHRS.MadgRoll), Math.toRadians(madgwickAHRS.MadgYaw), phoneUUid);
            } catch (Exception e) {
            }
        }
    }


    /////////  카메라에 관한 로직  /////////
    public void onCameraViewStarted(int width, int height) {
    }

    public void onCameraViewStopped() {
    }

    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
        if(check == false){
            mOdometer = new Odometer(Camera.getFocal(), Camera.getPrinciplePoint());
            mPreview = new Preview();
            check = true;
        }
        Mat gray = inputFrame.gray();
        Mat grayT = gray.t();
        Core.flip(gray.t(), grayT, 1);
        Imgproc.resize(grayT, grayT, gray.size());
        Mat rgb = new Mat(grayT.rows(), grayT.cols(), CvType.CV_8UC3);
        try{
            mOdometer.estimate(grayT, Camera.getScale());

            Imgproc.cvtColor(grayT, rgb, Imgproc.COLOR_GRAY2RGB);
            mPreview.redraw(rgb, mOdometer.getLastFeatures(), mOdometer.getCurrFeatures(), mOdometer.getR_f());
        } catch (Throwable t) {
            Log.e("ESTIMATE", t.getMessage());
            mOdometer = new Odometer(Camera.getFocal(), Camera.getPrinciplePoint());
            mPreview = new Preview();
        }
        return rgb;
    }
}
