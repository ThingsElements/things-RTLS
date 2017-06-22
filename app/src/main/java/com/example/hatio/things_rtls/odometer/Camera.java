package com.example.hatio.things_rtls.odometer;

import org.opencv.core.Point;

public class Camera {
    public double getFocal() {
//        return 718.8560;
        return 13841.24403;
    }

    public double getScale() {
        return 1;
    }

    public Point getPrinciplePoint() {
//        return new Point(607.1928, 185.2157);
        return new Point(639.5, 479.5);
    }
}