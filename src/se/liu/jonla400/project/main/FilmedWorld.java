package se.liu.jonla400.project.main;

public class FilmedWorld
{
    private World world;
    private RectangularRegion camera;

    public FilmedWorld(final World world, final RectangularRegion camera) {
	this.world = world;
	this.camera = camera.copy();
    }

    public World getWorld() {
	return world;
    }

    public void setWorld(final World world) {
	this.world = world;
    }

    public RectangularRegion getCamera() {
	return camera;
    }

    public void setCamera(final RectangularRegion camera) {
	this.camera = camera;
    }
}
