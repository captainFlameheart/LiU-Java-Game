package se.liu.jonla400.restructure.main;

import se.liu.jonla400.restructure.math.Vector2D;

import java.awt.*;
import java.awt.event.KeyEvent;

public interface World
{
    void cursorMoved(Vector2D newCursorPos);

    void cursorPressed();

    void cursorReleased();

    void keyPressed(KeyEvent keyEvent);

    void keyReleased(KeyEvent keyEvent);

    void tick(double deltaTime);

    void draw(Graphics2D g, RectangularRegion drawRegion);
}
