package se.liu.jonla400.project.main.temp;

import se.liu.jonla400.project.main.RectangularRegion;
import se.liu.jonla400.project.main.levelcreation.LevelBlueprint;
import se.liu.jonla400.project.main.levelcreation.LevelCreator;
import se.liu.jonla400.project.main.levelcreation.LevelCreatorConstructor;
import se.liu.jonla400.project.main.leveldefinition.LevelDefinition;
import se.liu.jonla400.project.math.Interval;
import se.liu.jonla400.project.math.Vector2D;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

public class TestRunner
{
    public static void main(String[] args) {
	final World world = new TestWorld();
	final RectangularRegion camera = RectangularRegion.createFromIntervals(new Interval(-10, 10), new Interval(-10, 10));
	final MovableCameraWorld movableCameraWorld = MovableCameraWorld.create(world, camera);
	WorldGUI.createAndStart(movableCameraWorld);
    }

    private static class TestWorld implements World
    {
	private Vector2D mousePos = Vector2D.createZero();

	@Override public void updateMousePos(final Vector2D newMousePos) {
	    this.mousePos.set(newMousePos);
	}

	@Override public void mousePressed(final MouseEvent mouseEvent) {
	}

	@Override public void mouseReleased(final MouseEvent mouseEvent) {
	}

	@Override public void mouseWheelMoved(final MouseWheelEvent mouseWheelEvent) {
	}

	@Override public void keyPressed(final KeyEvent keyEvent) {
	}

	@Override public void keyReleased(final KeyEvent keyEvent) {
	}

	@Override public void tick(final double deltaTime) {

	}

	@Override public void draw(final Graphics2D g, final RectangularRegion region) {
	    g.setColor(Color.WHITE);
	    g.fill(new Rectangle2D.Double(region.getLeftX(), region.getBottomY(), region.getWidth(), region.getHeight()));

	    g.setColor(Color.BLACK);
	    g.draw(new Rectangle2D.Double(0, 0, 0.1, 0.1));

	    final double radius = 0.01;
	    final double diameter = 2 * radius;
	    g.setColor(Color.BLACK);
	    g.fill(new Ellipse2D.Double(mousePos.getX() - radius, mousePos.getY() - radius, diameter, diameter));
	}
    }
}
