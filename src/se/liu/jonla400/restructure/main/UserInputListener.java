package se.liu.jonla400.restructure.main;

import se.liu.jonla400.restructure.math.Vector2D;

public interface UserInputListener
{
    void mouseMoved(Vector2D mousePos);

    void mousePressed(int button);

    void mouseReleased(int button);

    void mouseWheelMoved(double scrollAmount);

    void keyPressed(int keyCode);

    void keyReleased(int keyCode);
}
