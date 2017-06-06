package com.example.hatio.things_rtls.odometer;

import org.opencv.core.Point;

public class Camera {

//    int seq;

//    public void init() {
//        seq = 0;
//
        // Get access to the default camera.
//        try {
//            camera.open(0);
//            Thread.sleep(1);
//        } catch (Exception e) {}
//        if ( !camera.isOpened() ) {
//            System.out.println("ERROR: Could not access the camera!");
//            throw new Error("Could not access the camera");
//        }
//
//        camera.set(CV_CAP_PROP_FRAME_WIDTH, 640);
//        camera.set(CV_CAP_PROP_FRAME_HEIGHT, 480);
//        camera.set(CV_CAP_PROP_AUTOFOCUS, 0);
//    }

    public double getFocal() {
        return 718.8560;
    }

    public double getScale() {
        return 1;
    }

    public Point getPrinciplePoint() {
        return new Point(607.1928, 185.2157);
    }

//    Mat capture() {
//
//        camera.read(cameraFrame);
//
//        Mat grayImage = new Mat();
//
//        cvtColor(cameraFrame, grayImage, COLOR_BGR2GRAY);
//
//        return grayImage;
//    }

}
