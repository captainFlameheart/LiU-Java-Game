package se.liu.jonla400.project.main.drawing;

import se.liu.jonla400.project.physics.abstraction.main.Body;

import java.awt.*;

public class BodyDrawer implements Drawer
{
    private Body body;
    private Drawer localSpaceDrawer;

    public BodyDrawer(final Body body, final Drawer localSpaceDrawer) {
        this.body = body;
        this.localSpaceDrawer = localSpaceDrawer;
    }

    @Override public void draw(final Graphics2D g) {
        TransformedDrawer.draw(
                g, Transform.createWithTranslationAndRotation(body.getPos(), body.getAngle()),
                localSpaceDrawer
        );
    }
}
