package se.liu.jonla400.restructure.main.levelcreation;

import se.liu.jonla400.restructure.math.Vector2D;

import java.util.List;

public class AddVertexCommand implements Command
{
    private Vector2D vertex;

    public AddVertexCommand(final Vector2D vertex) {
	this.vertex = vertex;
    }

    @Override public void execute(final LevelCreator1 levelCreator) {
	levelCreator.getVertices().add(vertex);
    }

    @Override public void undo(final LevelCreator1 levelCreator) {
	final List<Vector2D> vertices = levelCreator.getVertices();
	vertices.remove(vertices.size() - 1);
    }
}
