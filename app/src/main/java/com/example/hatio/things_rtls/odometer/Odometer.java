package com.example.hatio.things_rtls.odometer;

import android.util.Log;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.KeyPoint;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.core.TermCriteria;
import org.opencv.features2d.FeatureDetector;
import org.opencv.imgproc.Imgproc;
import org.opencv.utils.Converters;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.opencv.video.Video.calcOpticalFlowPyrLK;
import static org.opencv.calib3d.Calib3d.RANSAC;
import static org.opencv.calib3d.Calib3d.findEssentialMat;
import static org.opencv.calib3d.Calib3d.recoverPose;

public class Odometer {

    double focal;
    Point pp = new Point();

    MatOfByte status = new MatOfByte();

    Mat R_f = new Mat();
    Mat t_f = new Mat();

    Mat E = new Mat();
    Mat R = new Mat();
    Mat t = new Mat();

    double x, y, z;

    Mat prevImage = new Mat();
    MatOfPoint2f lastFeatures = new MatOfPoint2f();
    MatOfPoint2f prevFeatures = new MatOfPoint2f();
    MatOfPoint2f currFeatures = new MatOfPoint2f();

    List<Point> currFeaturesList, prevFeaturesList;

    public Odometer(double focal, Point pp) {
        this.focal = focal;
        this.pp = pp;
    }

    public List<Point> getPrevFeaturesList(){
        return prevFeaturesList;
    }

    public List<Point> getCurrFeaturesList(){
        return currFeaturesList;
    }

    public MatOfPoint2f getPrevFeatures(){
        return prevFeatures;
    }

    public MatOfPoint2f getCurrFeatures(){
        return currFeatures;
    }

    public MatOfPoint2f getLastFeatures() { return lastFeatures; }

    public Mat getR_f(){
        return R_f;
    }

    double featureTracking(Mat prevImage, Mat currImage, MatOfPoint2f prevFeatures, MatOfPoint2f currFeatures, MatOfByte status){
        // 트래킹에 실패한 포인트들은 버린다.
        MatOfFloat err = new MatOfFloat();
        Size winSize = new Size(21, 21);
        TermCriteria termcrit = new TermCriteria(TermCriteria.COUNT+TermCriteria.EPS, 30, 0.01);

        calcOpticalFlowPyrLK(prevImage, currImage, prevFeatures, currFeatures, status, err, winSize, 3, termcrit, 0, 0.001);

        double weight = 0;
        // KLT 트래킹에 실패하거나 프레임 바깥으로 벗어난 포인트들은 버린다.
        int indexCorrection = 0;
        byte[] statusArray = status.toArray();
        Point[] currFeaturesArray = currFeatures.toArray();
        Point[] prevFeaturesArray = prevFeatures.toArray();

        currFeaturesList = new LinkedList<Point>(currFeatures.toList());
        prevFeaturesList = new LinkedList<>(prevFeatures.toList());

        for(int i = 0; i < statusArray.length; i++){
            Point pt = currFeaturesArray[i];

            if((statusArray[i] == 0) || (pt.x < 0) || (pt.y < 0)){
                prevFeaturesList.remove(i - indexCorrection);
                currFeaturesList.remove(i - indexCorrection);

                indexCorrection++;
            } else {
                Point before = prevFeaturesList.get(i - indexCorrection);
                Point after = currFeaturesList.get(i - indexCorrection);
                weight += (after.x - before.x) * (after.x - before.x) + (after.y - before.y)*(after.y - before.y);
//                Point before = prevFeaturesArray[i - indexCorrection];
//                weight += ((int)pt.x - before.x)*((int)pt.x - before.x) + ((int)pt.y - before.y)*((int)pt.y - before.y);
            }
        }

        // currFeatures, prevFeatures를 필터한 특징점으로 교체함
        currFeatures.fromList(currFeaturesList);
        prevFeatures.fromList(prevFeaturesList);

        if(prevFeaturesArray.length == 0)
            return 0;
        return weight / prevFeaturesArray.length;
    }

    MatOfPoint2f featureDetection(Mat image) {
        MatOfKeyPoint keypoints = new MatOfKeyPoint();

        FeatureDetector fast = FeatureDetector.create(FeatureDetector.FAST);
        fast.detect(image, keypoints);

        KeyPoint[] kps = keypoints.toArray();
        ArrayList<Point> arrayOfPoints = new ArrayList<Point>();

        for(int i = 0; i < kps.length; i++){
            arrayOfPoints.add(kps[i].pt);
        }

        MatOfPoint2f matOfPoint = new MatOfPoint2f();
        matOfPoint.fromList(arrayOfPoints);

        return matOfPoint;
    }

    public int estimate(Mat currImage, double scale) {
        Point[] prevFeaturesArray = prevFeatures.toArray();

        if(prevImage.empty()){
            prevImage = currImage.clone();
            prevFeatures = featureDetection(prevImage);
            return 0;
        } else if (prevFeaturesArray.length < 2000) {
            prevFeatures = featureDetection(prevImage);
            if(prevFeaturesArray.length <= 0){
                Log.e("", "Can't detect features.");
            }
        }

        double weight = featureTracking(prevImage, currImage, prevFeatures, currFeatures, status);
//        featureTracking(prevImage, currImage, prevFeatures, currFeatures, status);
        Log.d("============== ", ""+weight);

        if(prevFeaturesArray.length <= 0){
            prevFeatures = featureDetection(currImage);
            prevImage = currImage.clone();
            return 0;
        }

        if(weight < 500) {
            return 0;
        }

        E = findEssentialMat(currFeatures, prevFeatures, focal, pp, RANSAC, 0.999, 1.0, status);
        recoverPose(E, currFeatures, prevFeatures, R, t, focal, pp, status);

//        Log.d("==========R(0,0)", ""+R.get(0,0)[0]);
//        Log.d("==========R(0,1)", ""+R.get(0,1)[0]);
//        Log.d("==========R(0,2)", ""+R.get(0,2)[0]);
//        Log.d("==========R(1,0)", ""+R.get(1,0)[0]);
//        Log.d("==========R(1,1)", ""+R.get(1,1)[0]);
//        Log.d("==========R(1,2)", ""+R.get(1,2)[0]);
//        Log.d("==========R(2,0)", ""+R.get(2,0)[0]);
//        Log.d("==========R(2,1)", ""+R.get(2,1)[0]);
//        Log.d("==========R(2,2)", ""+R.get(2,2)[0]);

        if(R_f.empty()) {
            R_f = R.clone();
            t_f = t.clone();
        } else {
//            Mat tmp = new Mat();
//            Core.gemm(R_f, t, 1, new Mat(), 0, tmp);
//            Core.add(t_f, tmp, t_f);
//            Core.multiply(R, R_f, R_f);
            Core.gemm(R, R_f, 1, new Mat(), 0, R_f, 0);
        }

        x = t_f.get(0, 0)[0];
        y = t_f.get(1, 0)[0];
        z = t_f.get(2, 0)[0];

        prevImage = currImage.clone();
        prevFeatures.copyTo(lastFeatures);
        currFeatures.copyTo(prevFeatures);

        return 0;
    }
}