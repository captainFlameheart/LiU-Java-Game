package se.liu.jonla400.restructure.main.levelcreation;

import se.liu.jonla400.restructure.math.Vector2D;

public class IndexedVertex
{
    private int index;
    private Vector2D vertex;

    public IndexedVertex(final int index, final Vector2D vertex) {
	this.index = index;
	this.vertex = vertex;
    }

    public int getIndex() {
	return index;
    }

    public Vector2D getVertex() {
	return vertex;
    }
}
