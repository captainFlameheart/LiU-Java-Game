package se.liu.jonla400.project.main.drawing;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class DrawerList implements Drawer
{
    private List<Drawer> drawers;

    private DrawerList(final List<Drawer> drawers) {
	this.drawers = drawers;
    }

    public static DrawerList create(final Drawer... drawers) {
	return new DrawerList(Arrays.asList(drawers));
    }

    @Override public void draw(final Graphics2D g) {
	drawers.forEach(d -> d.draw(g));
    }
}
