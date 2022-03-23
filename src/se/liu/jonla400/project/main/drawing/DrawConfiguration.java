package se.liu.jonla400.project.main.drawing;

public class DrawConfiguration
{
    private BallDrawer ballDrawer;
    private LineSegmentDrawer lineSegmentDrawer;
    private Drawer centerOfMassDrawer;

    public DrawConfiguration(final BallDrawer ballDrawer, final LineSegmentDrawer lineSegmentDrawer, final Drawer centerOfMassDrawer) {
	this.ballDrawer = ballDrawer;
	this.lineSegmentDrawer = lineSegmentDrawer;
	this.centerOfMassDrawer = centerOfMassDrawer;
    }

    public Drawer getBallDrawer(final double radius) {
	return ballDrawer.setRadius(radius);
    }

    public LineSegmentDrawer getLineSegmentDrawer() {
	return lineSegmentDrawer;
    }

    public Drawer getCenterOfMassDrawer() {
	return centerOfMassDrawer;
    }
}
