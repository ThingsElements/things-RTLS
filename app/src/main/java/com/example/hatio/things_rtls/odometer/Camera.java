package com.example.hatio.things_rtls.odometer;

import org.opencv.core.Point;

public class Camera {

    public static double focal = 13841.24403;
    public static Point pp = new Point(639.5, 479.5);
    public static double scale = 1;

    public static double getFocal() {
        return focal;
    }

    public static void setFocal(double focal) {
        Camera.focal = focal;
    }

    public static void setScale(double scale){
        Camera.scale = scale;
    }

    public static double getScale() {
        return 1;
    }

    public static Point getPrinciplePoint() {
//        return new Point(607.1928, 185.2157);
        return pp;
    }

    public static void setPrinciplePoint(Point pp) {
        Camera.pp = pp;
    }
}