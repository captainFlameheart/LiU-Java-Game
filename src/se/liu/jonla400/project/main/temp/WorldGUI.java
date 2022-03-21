package se.liu.jonla400.project.main.temp;

import se.liu.jonla400.project.main.RectangularRegion;
import se.liu.jonla400.project.math.Interval;
import se.liu.jonla400.project.math.Vector2D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;

public class WorldGUI extends JComponent implements MouseListener, MouseWheelListener, KeyListener
{
    private FilmedWorld filmedWorld;
    private Queue<Runnable> eventHandlingQueue;

    private boolean hasStarted;

    private WorldGUI(final FilmedWorld filmedWorld, final Queue<Runnable> eventHandlingQueue, final boolean hasStarted) {
	this.filmedWorld = filmedWorld;
	this.eventHandlingQueue = eventHandlingQueue;
	this.hasStarted = hasStarted;
    }

    public static WorldGUI createFor(final FilmedWorld filmedWorld) {
	return new WorldGUI(filmedWorld, new LinkedList<>(), false);
    }

    public void start() {
	if (hasStarted) {
	    throw new IllegalStateException("Has already started!");
	}
	hasStarted = true;
	listenToUserInput();
	putInFrame();
	startTickTimer();
    }

    private void listenToUserInput() {
	addMouseListener(this);
	addMouseWheelListener(this);
	addKeyListener(this);
	setFocusable(true);
    }

    private void putInFrame() {
	final JFrame frame = new JFrame("Test!");
	frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	frame.setContentPane(this);
	frame.setSize(400, 400);
	frame.setLocationRelativeTo(null);
	frame.setExtendedState(Frame.MAXIMIZED_BOTH);
	frame.setVisible(true);
    }

    private void startTickTimer() {
	final int tickRate = 90;
	final double deltaSecondsPerTick = 1.0 / tickRate;
	final int deltaMillisecondsPerTick = 1000 / tickRate;
	final Timer tickTimer = new Timer(deltaMillisecondsPerTick, e -> tick(deltaSecondsPerTick));
	tickTimer.setCoalesce(true);
	tickTimer.start();
    }

    private void tick(final double deltaTime) {
	handleEvents();
	getMousePositionInWorldSpace().ifPresent(filmedWorld::updateMousePos);
	filmedWorld.tick(deltaTime);
	repaint();
    }

    private void handleEvents() {
	while (!eventHandlingQueue.isEmpty()) {
	    eventHandlingQueue.poll().run();
	}
    }

    private Optional<Vector2D> getMousePositionInWorldSpace() {
	final Point mousePoint = getMousePosition();
	if (mousePoint == null) {
	    return Optional.empty();
	}
	final Vector2D mouseVec = Vector2D.createCartesian(mousePoint.x, mousePoint.y);
	return Optional.of(convertToWorldPoint(mouseVec));
    }

    private Vector2D convertToWorldPoint(final Vector2D screenPoint) {
	// WRONG!

	final Interval screenXInterval = new Interval(0, getWidth());
	final Interval screenYInterval = new Interval(0, getHeight());

	final RectangularRegion drawRegion = encloseCameraWithCurrentAspectRatio();
	final Interval worldXInterval = drawRegion.getMinToMaxX();
	final Interval worldYInterval = drawRegion.getMinToMaxY();

	return Vector2D.createCartesian(
		screenXInterval.mapValueToOtherInterval(screenPoint.getX(), worldXInterval),
		screenYInterval.mapValueToOtherInterval(screenPoint.getY(), worldYInterval)
	);
    }

    @Override protected void paintComponent(final Graphics g) {
	super.paintComponent(g);
	final Graphics2D g2d = (Graphics2D) g;
	drawWorld(g2d);
    }

    private void drawWorld(final Graphics2D g) {
	final AffineTransform oldTransform = g.getTransform();

	flipGraphics(g);
	final RectangularRegion drawRegion = encloseCameraWithCurrentAspectRatio();
	final double scale = getWidth() / drawRegion.getWidth();
	g.scale(scale, scale);
	g.translate(-drawRegion.getLeftX(), -drawRegion.getBottomY());
	filmedWorld.draw(g, drawRegion);

	g.setTransform(oldTransform);
    }

    private void flipGraphics(final Graphics2D g) {
	g.scale(1, -1);
	g.translate(0, -getHeight());
    }

    private RectangularRegion encloseCameraWithCurrentAspectRatio() {
	final RectangularRegion camera = filmedWorld.getCamera();
	final double cameraWidth = camera.getWidth();
	final double cameraHeight = camera.getHeight();

	final double cameraWidthToHeightRatio = cameraWidth / cameraHeight;
	final double targetWidthToHeightRatio = (double) getWidth() / getHeight();

	final double enclosingWidth;
	final double enclosingHeight;
	if (cameraWidthToHeightRatio > targetWidthToHeightRatio) {
	    enclosingWidth = cameraWidth;
	    enclosingHeight = enclosingWidth / targetWidthToHeightRatio;
	} else {
	    enclosingHeight = cameraHeight;
	    enclosingWidth = enclosingHeight * targetWidthToHeightRatio;
	}

	return RectangularRegion.createFromCenter(camera.getCenter(), Vector2D.createCartesian(enclosingWidth, enclosingHeight));
    }

    @Override public void mousePressed(final MouseEvent e) {
	eventHandlingQueue.offer(() -> filmedWorld.mousePressed(e));
    }

    @Override public void mouseReleased(final MouseEvent e) {
	eventHandlingQueue.offer(() -> filmedWorld.mouseReleased(e));
    }

    @Override public void mouseWheelMoved(final MouseWheelEvent e) {
	eventHandlingQueue.offer(() -> filmedWorld.mouseWheelMoved(e));
    }

    @Override public void keyPressed(final KeyEvent e) {
	eventHandlingQueue.offer(() -> filmedWorld.keyPressed(e));
    }

    @Override public void keyReleased(final KeyEvent e) {
	eventHandlingQueue.offer(() -> filmedWorld.keyReleased(e));
    }

    @Override public void mouseClicked(final MouseEvent e) {}

    @Override public void mouseEntered(final MouseEvent e) {}

    @Override public void mouseExited(final MouseEvent e) {}

    @Override public void keyTyped(final KeyEvent e) {}
}
