package se.liu.jonla400.restructure.main.levelcreation;

public class ChangeStateCommand implements Command
{
    private LevelCreatorState state;
    private LevelCreatorState stateBefore;

    public ChangeStateCommand(final LevelCreatorState state) {
	this.state = state;
	stateBefore = null;
    }

    @Override public void execute(final LevelCreator1 levelCreator) {
	stateBefore = levelCreator.getState();
	levelCreator.setState(state);
    }

    @Override public void undo(final LevelCreator1 levelCreator) {
	levelCreator.setState(stateBefore);
    }
}
