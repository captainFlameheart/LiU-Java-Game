package se.liu.jonla400.project.main;

import se.liu.jonla400.project.physics.abstraction.main.Body;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class BodyDrawerSet implements Drawer
{
    private Collection<BodyDrawer> drawers;

    private BodyDrawerSet(final Collection<BodyDrawer> drawers) {
        this.drawers = drawers;
    }

    public static BodyDrawerSet create(final BodyDrawer... bodyDrawers) {
        return new BodyDrawerSet(new ArrayList<>(Arrays.asList(bodyDrawers)));
    }

    public void add(final Body body, final Drawer... localSpaceDrawers) {
        final DrawerSet drawerSet = DrawerSet.create(localSpaceDrawers);
        drawers.add(new BodyDrawer(body, drawerSet));
    }

    @Override public void draw(final Graphics2D g) {
        for (BodyDrawer drawer : drawers) {
            drawer.draw(g);
        }
    }
}
