package se.liu.jonla400.project.main.drawing;

import java.awt.*;

public class BallDrawer
{
    private CircleDrawer circleDrawer;
    private CrossDrawer crossDrawer;

    private BallDrawer(final CircleDrawer circleDrawer, final CrossDrawer crossDrawer) {
        this.circleDrawer = circleDrawer;
        this.crossDrawer = crossDrawer;
    }

    public static BallDrawer createDefault() {
        final Color fillColor = Color.RED;
        final Color strokeColor = Color.BLACK;
        final float strokeWidth = 0.1f;
        final CircleDrawer circleDrawer = CircleDrawer.create(fillColor, strokeColor, strokeWidth);

        final float crossStrokeWidth = 0.03f;
        final CrossDrawer crossDrawer = CrossDrawer.create(Color.BLACK, crossStrokeWidth);

        return new BallDrawer(circleDrawer, crossDrawer);
    }

    public Drawer setRadius(final double radius) {
        return g -> draw(g, radius);
    }

    public void draw(final Graphics2D g, final double radius) {
        circleDrawer.draw(g, radius);
        crossDrawer.draw(g, 0.3 * radius);
    }
}
