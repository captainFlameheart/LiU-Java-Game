package se.liu.jonla400.project.main.drawing;

import se.liu.jonla400.project.physics.main.Body;

import java.awt.*;

/**
 * Draws a {@link Body} by performing a given drawing procedure in the local space of the body.
 * The drawing procedure in the body's local space is specified by a {@link Drawer}
 */
public class BodyDrawer implements Drawer
{
    private Body body;
    private Drawer localSpaceDrawer;

    /**
     * Returns a new BodyDrawer that draws the given {@link Body} by using the given {@link Drawer}
     * in the body's local space
     *
     * @param body The body to draw
     * @param localSpaceDrawer The drawing procedure in the body's local space
     */
    public BodyDrawer(final Body body, final Drawer localSpaceDrawer) {
        this.body = body;
        this.localSpaceDrawer = localSpaceDrawer;
    }

    /**
     * Draws the body onto the {@link Graphics2D} object according to the position and angle of the body,
     * as well as the drawing procedure given to this BodyDrawer
     *
     * @param g The graphics object to draw to
     */
    @Override public void draw(final Graphics2D g) {
        TransformedDrawer.draw(
                g, Transform.createWithTranslationAndRotation(body.getPos(), body.getAngle()),
                localSpaceDrawer
        );
    }
}
