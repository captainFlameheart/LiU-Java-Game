package se.liu.jonla400.restructure.main;

import se.liu.jonla400.restructure.math.Interval;
import se.liu.jonla400.restructure.math.Vector2D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;

public class Screen extends JComponent implements MouseListener, MouseMotionListener, MouseWheelListener, KeyListener
{
    private final static int CAMERA_BUTTON = MouseEvent.BUTTON3, WORLD_ACTION_BUTTON = MouseEvent.BUTTON1;

    private FilmedWorld filmedWorld;

    private Vector2D oldMouseScreenPos;
    private boolean movingCamera;

    private Screen(final FilmedWorld filmedWorld, final Vector2D oldMouseScreenPos, final boolean movingCamera) {
        this.filmedWorld = filmedWorld;
        this.oldMouseScreenPos = oldMouseScreenPos;
        this.movingCamera = movingCamera;
    }

    public void setFilmedWorld(final FilmedWorld filmedWorld) {
        this.filmedWorld = filmedWorld;
    }

    public static Screen create(final FilmedWorld filmedWorld) {
        final Screen screen = new Screen(filmedWorld, Vector2D.createZero(), false);
        screen.addMouseListener(screen);
        screen.addMouseMotionListener(screen);
        screen.addMouseWheelListener(screen);
        screen.addKeyListener(screen);
        screen.setFocusable(true);
        return screen;
    }

    Vector2D oldPos = Vector2D.createZero();
    double x = 0;
    public void tick(final double deltaTime) {
        filmedWorld.getWorld().tick(deltaTime);
        repaint();
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
        filmedWorld.getWorld().draw(g, drawRegion);

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

    @Override public void mouseClicked(final MouseEvent e) {}

    @Override public void mousePressed(final MouseEvent e) {
        final int button = e.getButton();
        if (button == CAMERA_BUTTON) {
            movingCamera = true;
        } else if (button == WORLD_ACTION_BUTTON) {
            filmedWorld.getWorld().cursorPressed();
        }
    }

    @Override public void mouseReleased(final MouseEvent e) {
        final int button = e.getButton();
        if (button == CAMERA_BUTTON) {
            movingCamera = false;
        } else if (button == WORLD_ACTION_BUTTON) {
            filmedWorld.getWorld().cursorReleased();
        }
    }

    @Override public void mouseEntered(final MouseEvent e) {}

    @Override public void mouseExited(final MouseEvent e) {}

    @Override public void mouseDragged(final MouseEvent e) {
        onMouseMoved(e);
    }

    @Override public void mouseMoved(final MouseEvent e) {
        onMouseMoved(e);
    }

    private void onMouseMoved(MouseEvent e) {
        final Vector2D newMouseScreenPos = Vector2D.createCartesian(e.getX(), e.getY());
        final Vector2D newMouseWorldPos = convertToWorldPoint(newMouseScreenPos);
        if (movingCamera) {
            final Vector2D oldMouseWorldPos = convertToWorldPoint(oldMouseScreenPos);

            final Vector2D deltaCameraPos = oldMouseWorldPos.subtract(newMouseWorldPos);
            filmedWorld.getCamera().move(deltaCameraPos);
        } else {
            filmedWorld.getWorld().cursorMoved(newMouseWorldPos);
        }
        oldMouseScreenPos.set(newMouseScreenPos);
    }

    @Override public void mouseWheelMoved(final MouseWheelEvent e) {
        if (movingCamera) {
            return;
        }
        final double scaleFactor = 1.1;
        final double scale = Math.pow(scaleFactor, e.getPreciseWheelRotation());
        filmedWorld.getCamera().scale(scale);
        onMouseMoved(e); // The mouse position in the world has changed since the camera scaled
    }

    private Vector2D convertToWorldPoint(final Vector2D screenPoint) {
        final Interval screenXInterval = new Interval(0, getWidth());
        final Interval screenYInterval = new Interval(0, getHeight());

        final RectangularRegion drawRegion = encloseCameraWithCurrentAspectRatio();
        final Interval gameXInterval = drawRegion.getMinToMaxX();
        final Interval gameYInterval = drawRegion.getMinToMaxY();

        return Vector2D.createCartesian(
                screenXInterval.mapValueToOtherInterval(screenPoint.getX(), gameXInterval),
                screenYInterval.mapValueToOtherInterval(screenPoint.getY(), gameYInterval)
        );
    }

    @Override public void keyTyped(final KeyEvent e) {
    }

    @Override public void keyPressed(final KeyEvent e) {
        filmedWorld.getWorld().keyPressed(e);
    }

    @Override public void keyReleased(final KeyEvent e) {
        filmedWorld.getWorld().keyReleased(e);
    }
}
