package se.liu.jonla400.restructure.main.levelcreation;

public class SetStateCommand implements Command
{
    private LevelCreatorState state;
    private LevelCreatorState stateBefore;

    public SetStateCommand(final LevelCreatorState state) {
	this.state = state;
	stateBefore = null;
    }

    @Override public void execute(final LevelCreator levelCreator) {
	stateBefore = levelCreator.getState();
	levelCreator.setState(state);
    }

    @Override public void undo(final LevelCreator levelCreator) {
	levelCreator.setState(stateBefore);
    }
}
