package com.example.hatio.things_rtls.odometer;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.LinkedList;
import java.util.List;

public class Preview {
    Mat AXIS = new Mat(3, 3, CvType.CV_8UC1);
    List<Point> currFeaturesList, prevFeaturesList;

    public Preview() {
        AXIS.put(0, 0, 1);
        AXIS.put(1, 1, 1);
        AXIS.put(2, 2, 1);
    }

    public void redraw(Mat image, MatOfPoint2f prevFeatures, MatOfPoint2f currFeatures, Mat R) {
        prevFeaturesList = new LinkedList<>(prevFeatures.toList());
        currFeaturesList = new LinkedList<>(currFeatures.toList());
        if(prevFeaturesList.size() == currFeaturesList.size()){
            for(int i = 0; i < prevFeaturesList.size(); i++){
                Imgproc.line(image, prevFeaturesList.get(i), currFeaturesList.get(i), new Scalar(0, 255, 0));
            }
        }

        if(R.rows() >0 && R.cols() >0){
            Point origin = new Point(50, 20);
//            Log.d("==========R(0,0)", ""+R.get(0,0)[0]);
//            Log.d("==========R(0,1)", ""+R.get(0,1)[0]);
//            Log.d("==========R(0,2)", ""+R.get(0,2)[0]);
//            Log.d("==========R(1,0)", ""+R.get(1,0)[0]);
//            Log.d("==========R(1,1)", ""+R.get(1,1)[0]);
//            Log.d("==========R(1,2)", ""+R.get(1,2)[0]);
//            Log.d("==========R(2,0)", ""+R.get(2,0)[0]);
//            Log.d("==========R(2,1)", ""+R.get(2,1)[0]);
//            Log.d("==========R(2,2)", ""+R.get(2,2)[0]);

            Point xAxis = new Point(R.get(0, 0)[0] * 100 + origin.x, R.get(1, 0)[0] * 100 + origin.y);
            Point yAxis = new Point(R.get(0, 1)[0] * 100 + origin.x, R.get(1, 1)[0] * 100 + origin.y);
            Point zAxis = new Point(R.get(0, 2)[0] * 100 + origin.x, R.get(1, 2)[0] * 100 + origin.y);

            Imgproc.line(image, origin, xAxis, new Scalar(255, 0, 0), 2);
            Imgproc.line(image, origin, yAxis, new Scalar(0, 255, 0), 2);
            Imgproc.line(image, origin, zAxis, new Scalar(0, 0, 255), 2);

        }
    }
}
