package com.example.hatio.things_rtls.app;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.hatio.things_rtls.R;
import com.example.hatio.things_rtls.assist.CameraPreview;

public class CameraMode extends Fragment {
    String TAG = "CAMERA";
    private Context mContext = this.getContext();
    private static Camera mCamera;
    private CameraPreview mPreview;
    private boolean runThread = false;
    public static Toast mToast;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getCameraInstance();


//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.view_camera_mode, container, false);

        mContext = this.getContext();
        // 카메라 인스턴스 생성
        if (checkCameraHardware(mContext)) {
            mContext = view.getContext();
//            mCamera = getCameraInstance();

            // 프리뷰창을 생성하고 액티비티의 레이아웃으로 지정합니다

            if(mCamera != null) {
                mPreview = new CameraPreview(view.getContext(), mCamera);
                FrameLayout preview = (FrameLayout) view.findViewById(R.id.camera_preview);
                preview.addView(mPreview);

            }

        }

        return view;
    }

    /**
     * 카메라 하드웨어 지원 여부 확인
     */
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            // 카메라가 최소한 한개 있는 경우 처리
            Log.i(TAG, "Number of available camera : " + Camera.getNumberOfCameras());
            return true;
        } else {
            // 카메라가 전혀 없는 경우
            Toast.makeText(mContext, "No camera found!", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    /**
     * 카메라 인스턴스를 안전하게 획득합니다
     */
    public static void getCameraInstance() {
        releaseCameraAndPreview();

        int numCams = Camera.getNumberOfCameras();
        if(numCams == 0)
            return;

        try {
            mCamera = Camera.open(0);
        } catch (Exception e) {
            // 사용중이거나 사용 불가능 한 경우
        }
//        return mCamera;
    }

    private static void releaseCameraAndPreview() {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // 보통 안쓰는 객체는 onDestroy에서 해제 되지만 카메라는 확실히 제거해주는게 안전하다.
    }

}
