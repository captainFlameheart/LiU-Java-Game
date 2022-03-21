package se.liu.jonla400.project.main.world;

import se.liu.jonla400.project.math.RectangularRegion;
import se.liu.jonla400.project.math.Vector2D;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public interface World
{
    void updateMousePos(Vector2D newMousePos);

    void mousePressed(MouseEvent mouseEvent);

    void mouseReleased(MouseEvent mouseEvent);

    void mouseWheelMoved(MouseWheelEvent mouseWheelEvent);

    void keyPressed(KeyEvent keyEvent);

    void keyReleased(KeyEvent keyEvent);

    void tick(double deltaTime);

    void draw(Graphics2D g, RectangularRegion region);
}
