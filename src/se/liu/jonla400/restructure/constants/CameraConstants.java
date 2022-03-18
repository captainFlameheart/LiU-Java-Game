package se.liu.jonla400.restructure.constants;

import se.liu.jonla400.restructure.main.RectangularRegion;
import se.liu.jonla400.restructure.math.Interval;

public class CameraConstants
{
    private final static double SCALE_FACTOR = 1.1;
    private final static RectangularRegion DEFAULT_CAMERA = RectangularRegion.createFromIntervals(
            new Interval(-10, 10),
            new Interval(-10, 10)
    );

    private CameraConstants() {}

    public static double getScaleFactor() {
        return SCALE_FACTOR;
    }

    public static RectangularRegion getDefaultCamera() {
        return DEFAULT_CAMERA.copy();
    }
}
