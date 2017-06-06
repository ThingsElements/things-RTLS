//package com.example.hatio.things_rtls.odometer;
//
//import android.os.Bundle;
//import android.widget.Toast;
//
//import com.example.hatio.things_rtls.R;
//import com.example.hatio.things_rtls.app.ActionBarActivity;
//
//import org.opencv.core.Mat;
//
//import java.util.concurrent.TimeUnit;
//
//public class MonoVo extends ActionBarActivity {
//
//    Camera camera = new Camera();
////        Playback playback = new Playback("Road facing camera");
//private Camera mCamera = new Camera();
//    private Odometer mOdometer = new Odometer(mCamera.getFocal(), mCamera.getPrinciplePoint());
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.view_camera_mode);
//
//        Odometer odometer = new Odometer(camera.getFocal(), camera.getPrinciplePoint());
//        start();
//    }
//
//
//    //TODO: add a fucntion to load these values directly from KITTI's calib files
//    // WARNING: different sequences in the KITTI VO dataset have different intrinsic/extrinsic parameters
//
//    void start() {
//
//        double begin = System.currentTimeMillis();
//
//        while (true) {
//            Mat image = camera.capture();
//            if (image == null)
//                break;
//
//            odometer.estimate(image, camera.getScale());
//
////                    cvtColor(image, colorImage, COLOR_GRAY2RGB);
////                        playback.redraw(colorImage,odometer.getLastFeatures(),odometer.getCurrFeatures());
//
//
//            try {
//                TimeUnit.MILLISECONDS.sleep(1);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//
//        double end = System.currentTimeMillis();
//
//        double elapsed_secs = (double) ((end - begin) / 1000);
//        System.out.printf("Total time taken: " + elapsed_secs + "s");
//
//        Toast.makeText(getApplicationContext(), "OpenCV end", Toast.LENGTH_LONG).show();
//
//    }
//
//
//}
//
