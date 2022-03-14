package se.liu.jonla400.restructure.main.drawing;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class DrawerSet implements Drawer
{
    private List<Drawer> subDrawers;

    private DrawerSet(final List<Drawer> subDrawers) {
	this.subDrawers = subDrawers;
    }

    public static DrawerSet create(final Drawer... drawers) {
	return new DrawerSet(Arrays.asList(drawers));
    }

    public void add(final Drawer subDrawer) {
	subDrawers.add(subDrawer);
    }

    public void remove(final Drawer subDrawer) {
	subDrawers.remove(subDrawer);
    }

    public void remove(final int subDrawerIndex) {
	subDrawers.remove(subDrawerIndex);
    }

    @Override public void draw(final Graphics2D g) {
	subDrawers.forEach(d -> d.draw(g));
    }
}
