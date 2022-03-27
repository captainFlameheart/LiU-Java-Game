package se.liu.jonla400.project.main.world;

import se.liu.jonla400.project.math.RectangularRegion;
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

/**
 * Timesteps, draws and notifies a {@link FilmedWorld} about user input. Uses the camera of
 * the filmed world to determine how to transform the graphics so that the camera region is
 * atleast shown. Since the aspect ratio of this component and the camera are likely to be
 * different, the gui is likely to show more on one axis than what the camera requires.
 *
 * This gui also tells the filmed world about where the mouse position is in world space.
 */
public class WorldGUI extends JComponent implements MouseListener, MouseWheelListener, KeyListener
{
    private FilmedWorld filmedWorld;
    private Queue<Runnable> eventHandlingQueue; // Gives us controll of when to handle the input events

    private boolean hasStarted;

    private WorldGUI(final FilmedWorld filmedWorld, final Queue<Runnable> eventHandlingQueue, final boolean hasStarted) {
	this.filmedWorld = filmedWorld;
	this.eventHandlingQueue = eventHandlingQueue;
	this.hasStarted = hasStarted;
    }

    /**
     * Creates a WorldGUI for the given world
     *
     * @param filmedWorld The world to mantain graphics-, input and time-wise
     * @return The created WorldGUI
     */
    public static WorldGUI createFor(final FilmedWorld filmedWorld) {
	return new WorldGUI(filmedWorld, new LinkedList<>(), false);
    }

    /**
     * Starts this gui by putting it in a frame and start advancing time
     */
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
	getMousePosInWorldSpace().ifPresent(filmedWorld::updateMousePos);
	filmedWorld.tick(deltaTime);
	repaint();
    }

    private void handleEvents() {
	while (!eventHandlingQueue.isEmpty()) {
	    eventHandlingQueue.poll().run();
	}
    }

    private Optional<Vector2D> getMousePosInWorldSpace() {
	final Point mousePos = getMousePosition();
	if (mousePos == null) {
	    return Optional.empty();
	}

	final RectangularRegion visibleRegion = encloseCameraWithCurrentAspectRatio();

	// screenX -> worldX: 0 -> left of visible region, width -> right of visible region
	final Interval xInterval = new Interval(0, getWidth());
	final Interval xIntervalMappedTo = visibleRegion.getLeftToRightX();

	// screenY -> worldY: 0 -> top of visible region, height -> bottom of visible region
	final Interval yInterval = new Interval(0, getHeight());
	final Interval yIntervalMappedTo = visibleRegion.getTopToButtomY();

	return Optional.of(Vector2D.createCartesian(
		xInterval.mapValueToOtherInterval(mousePos.x, xIntervalMappedTo),
		yInterval.mapValueToOtherInterval(mousePos.y, yIntervalMappedTo)
	));
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
	final double scale = getWidth() / drawRegion.getSize().getX();
	g.scale(scale, scale);
	final Vector2D bottomLeft = drawRegion.getBottomLeft();
	g.translate(-bottomLeft.getX(), -bottomLeft.getY());
	filmedWorld.draw(g, drawRegion);

	g.setTransform(oldTransform);
    }

    private void flipGraphics(final Graphics2D g) {
	g.scale(1, -1);
	g.translate(0, -getHeight());
	// (0, 0) in graphics space is now at the bottom left of the screen
	// and increasing y-coordinates means higher up on the screen
    }

    private RectangularRegion encloseCameraWithCurrentAspectRatio() {
	final RectangularRegion camera = filmedWorld.getCamera();
	final Vector2D cameraSize = camera.getSize();
	final double cameraWidth = cameraSize.getX();
	final double cameraHeight = cameraSize.getY();

	final double cameraWidthToHeightRatio = cameraWidth / cameraHeight;
	final double targetWidthToHeightRatio = (double) getWidth() / getHeight();

	final double enclosingWidth;
	final double enclosingHeight;
	if (cameraWidthToHeightRatio > targetWidthToHeightRatio) {
	    // Let the enclosing width be the camera width
	    enclosingWidth = cameraWidth;
	    // Then compute the enclosing height that maintains the screen's width to height ratio
	    enclosingHeight = enclosingWidth / targetWidthToHeightRatio;
	} else {
	    // Let the enclosing height be the camera height
	    enclosingHeight = cameraHeight;
	    // Then compute the enclosing width that maintains the screen's width to height ratio
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
