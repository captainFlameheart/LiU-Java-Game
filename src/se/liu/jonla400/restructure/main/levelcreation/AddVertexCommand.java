package se.liu.jonla400.restructure.main.levelcreation;

import se.liu.jonla400.restructure.math.Vector2D;

public class AddVertexCommand implements Command
{
    private Vector2D vertex;

    public AddVertexCommand(final Vector2D vertex) {
	this.vertex = vertex;
    }

    @Override public void execute(final LevelCreator levelCreator) {
	levelCreator.add(vertex);
    }

    @Override public void undo(final LevelCreator levelCreator) {
	levelCreator.removeVertex();
    }
}
