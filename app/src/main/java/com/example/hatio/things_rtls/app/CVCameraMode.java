package com.example.hatio.things_rtls.app;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import com.example.hatio.things_rtls.R;
import com.example.hatio.things_rtls.odometer.Camera;
import com.example.hatio.things_rtls.odometer.Odometer;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;


public class CVCameraMode extends Fragment implements CvCameraViewListener2 {

    private static final String TAG = "OCVSample::Activity";

    private CameraBridgeViewBase mOpenCvCameraView;
    private boolean              mIsJavaCamera = true;
    private MenuItem mItemSwitchCamera = null;
    private Context mContext;
    private Camera mCamera = new Camera();
    private Odometer mOdometer = new Odometer(mCamera.getFocal(), mCamera.getPrinciplePoint());

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this.getActivity()) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i(TAG, "OpenCV loaded successfully");
                    mOpenCvCameraView.enableView();
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {


        Log.i(TAG, "called onCreate");
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.view_camera_mode, container, false);

        mOpenCvCameraView = (CameraBridgeViewBase) view.findViewById(R.id.camera_preview);

        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);

        mOpenCvCameraView.setCvCameraViewListener(this);

//        camera.set(CV_CAP_PROP_FRAME_WIDTH, 640);
//        camera.set(CV_CAP_PROP_FRAME_HEIGHT, 480);
//        camera.set(CV_CAP_PROP_AUTOFOCUS, 0);

        return view;
    }




    @Override
    public void onPause()
    {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this.getActivity(), mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
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
        try{
            mOdometer.estimate(gray, mCamera.getScale());
        } catch(Throwable t) {
            Log.e("ESTIMATE", t.getMessage());
        }

        return gray;
    }
}
