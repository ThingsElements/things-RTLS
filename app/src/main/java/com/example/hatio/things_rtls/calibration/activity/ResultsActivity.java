package com.example.hatio.things_rtls.calibration.activity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.hatio.things_rtls.R;
import com.example.hatio.things_rtls.app.Calibration;

import org.opencv.core.CvType;
import org.opencv.core.Mat;


public class ResultsActivity extends AppCompatActivity {

    private TextView mTextResults;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Pass to super
        super.onCreate(savedInstanceState);

        // Create our layout
        setContentView(R.layout.view_calibration_result);

        // Add our button listeners
        addButtonListeners();

        // Get the text view we will display our results on
        mTextResults = (TextView) findViewById(R.id.text_results);

        // Get shared pref config
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Run the async calibration
        run_calibration();

    }

    private void addButtonListeners() {

        // When the done button is pressed we should end the result activity
        Button button_done = (Button) findViewById(R.id.button_done);
        button_done.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResultsActivity.this.finish();
            }
        });

        // When this is clicked we should save the settings file
        Button button_save = (Button) findViewById(R.id.button_save);
        button_save.setEnabled(false);
        button_save.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    // Taken from the calibration example, will run in async tast
    // https://github.com/Itseez/opencv/blob/master/samples/android/camera-calibration/src/org/opencv/samples/cameracalibration/CameraCalibrationActivity.java#L154-L188
    private void run_calibration() {

        final Resources res = getResources();

        new AsyncTask<Void, Void, Void>() {
            private ProgressDialog calibrationProgress;

            @Override
            protected void onPreExecute() {
                calibrationProgress = new ProgressDialog(ResultsActivity.this);
                calibrationProgress.setTitle("Calibrating");
                calibrationProgress.setMessage("Please Wait");
                calibrationProgress.setCancelable(false);
                calibrationProgress.setIndeterminate(true);
                calibrationProgress.show();
            }

            @Override
            protected Void doInBackground(Void... arg0) {
                try {
                    Calibration.mCameraCalibrator.calibrate();
                } catch(Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {

                // Dismiss the processing popup
                calibrationProgress.dismiss();

                // Display our text with info
                if (Calibration.mCameraCalibrator.isCalibrated()) {

                    // Get image size and focus lengh
                    String imageSize = sharedPreferences.getString("prefSizeRaw", "640x480");
                    int widthRaw = Integer.parseInt(imageSize.substring(0,imageSize.lastIndexOf("x")));
                    int heightRaw = Integer.parseInt(imageSize.substring(imageSize.lastIndexOf("x")+1));
                    String focus = sharedPreferences.getString("prefFocusLength", "5.0");

                    // Get results from calibrator
                    double rms = Calibration.mCameraCalibrator.getAvgReprojectionError();
                    Mat cal_mat = Calibration.mCameraCalibrator.getCameraMatrix();
                    Mat cal_dist = Calibration.mCameraCalibrator.getDistortionCoefficients();

                    // Get actual camera intrinsic values (api 23 and greater)
                    // https://developer.android.com/reference/android/hardware/camera2/CaptureResult.html#LENS_INTRINSIC_CALIBRATION
                    float[] intrinsic = Calibration.mCameraManager.getIntrinsic();
                    Mat dev_mat = new Mat();
                    Mat.eye(3, 3, CvType.CV_64FC1).copyTo(dev_mat);
                    dev_mat.put(0,0, intrinsic[0]); // f_x
                    dev_mat.put(1,1, intrinsic[1]); // f_y
                    dev_mat.put(0,2, intrinsic[2]); // c_x
                    dev_mat.put(1,2, intrinsic[3]); // c_y
                    dev_mat.put(0,1, intrinsic[4]); // skew

                    // Get actual camera distortion values (radtan)
                    // https://developer.android.com/reference/android/hardware/camera2/CaptureResult.html#LENS_INTRINSIC_CALIBRATION
                    float[] distortion = Calibration.mCameraManager.getDistortion();
                    Mat dev_dist = new Mat();
                    Mat.zeros(4, 1, CvType.CV_64FC1).copyTo(dev_dist);
                    dev_dist.put(0,0, distortion[0]); // kappa_0
                    dev_dist.put(1,0, distortion[1]); // kappa_1
                    dev_dist.put(2,0, distortion[2]); // kappa_2
                    dev_dist.put(3,0, distortion[3]); // kappa_3

                    // Display the values
                    mTextResults.setText("Calibration was successful!!\n\n" +
                            "Image Size (px):\n" + widthRaw + " x " + heightRaw + "\n\n" +
                            "Set Focal Length (mm):\n" + Float.parseFloat(focus) + "\n\n" +
                            "Average Reprojection Error:\n" + rms + "\n\n" +
                            "Calibration Matrix:\n" + cal_mat.dump() + "\n\n" +
                            "Calibration Distortion:\n" + cal_dist.dump() + "\n\n" +
                            "Device Matrix (api 23):\n" + dev_mat.dump() + "\n\n" +
                            "Device Distortion (api 23):\n" + dev_dist.dump() + "\n\n"
                    );
                } else {

                    // Get actual camera intrinsic values (api 23 and greater)
                    // https://developer.android.com/reference/android/hardware/camera2/CaptureResult.html#LENS_INTRINSIC_CALIBRATION
                    float[] intrinsic = Calibration.mCameraManager.getIntrinsic();
                    Mat dev_mat = new Mat();
                    Mat.eye(3, 3, CvType.CV_64FC1).copyTo(dev_mat);
                    dev_mat.put(0,0, intrinsic[0]); // f_x
                    dev_mat.put(1,1, intrinsic[1]); // f_y
                    dev_mat.put(0,2, intrinsic[2]); // c_x
                    dev_mat.put(1,2, intrinsic[3]); // c_y
                    dev_mat.put(0,1, intrinsic[4]); // skew

                    // Get actual camera distortion values (radtan)
                    // https://developer.android.com/reference/android/hardware/camera2/CaptureResult.html#LENS_INTRINSIC_CALIBRATION
                    float[] distortion = Calibration.mCameraManager.getDistortion();
                    Mat dev_dist = new Mat();
                    Mat.zeros(4, 1, CvType.CV_64FC1).copyTo(dev_dist);
                    dev_dist.put(0,0, distortion[0]); // kappa_0
                    dev_dist.put(1,0, distortion[1]); // kappa_1
                    dev_dist.put(2,0, distortion[2]); // kappa_2
                    dev_dist.put(3,0, distortion[3]); // kappa_3

                    // Display the values
                    mTextResults.setText("Unable to preform OpenCV calibration.\n\n" +
                            "Device Matrix (api 23):\n" + dev_mat.dump() + "\n\n" +
                            "Device Distortion (api 23):\n" + dev_dist.dump() + "\n\n"
                    );
                }

                // Reset everything
                Calibration.mCameraCalibrator.clearCorners();

            }
        }.execute();
    }

}