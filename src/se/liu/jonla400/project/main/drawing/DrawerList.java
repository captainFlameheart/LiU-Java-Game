package se.liu.jonla400.project.main.drawing;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a list of {@link Drawer} that can draw those drawers from first to last
 */
public class DrawerList implements Drawer
{
    private List<Drawer> drawers;

    private DrawerList(final List<Drawer> drawers) {
	this.drawers = drawers;
    }

    /**
     * Creates a new DrawerList of the given drawers and with the same order
     *
     * @param drawers The drawers of the list
     * @return The created DrawerList
     */
    public static DrawerList create(final Drawer... drawers) {
	return new DrawerList(Arrays.asList(drawers));
    }

    /**
     * Draws each drawer onto the {@link Graphics2D} object from first to last
     *
     * @param g The graphics object to draw to
     */
    @Override public void draw(final Graphics2D g) {
	drawers.forEach(d -> d.draw(g));
    }
}
