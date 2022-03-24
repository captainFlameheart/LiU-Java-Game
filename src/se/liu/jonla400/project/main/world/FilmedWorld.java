package se.liu.jonla400.project.main.world;

import se.liu.jonla400.project.math.RectangularRegion;

/**
 * Represents a {@link World} with a camera.
 * Please note that the camera represents the smallest possible region visible in a
 * {@link WorldGUI}. A world GUI might however display more of the world since the
 * aspect ratio of the GUI can be different than the camera's aspect ratio.
 */
public interface FilmedWorld extends World
{
    RectangularRegion getCamera();
}
