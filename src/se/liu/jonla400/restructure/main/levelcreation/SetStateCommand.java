package se.liu.jonla400.restructure.main.levelcreation;

public class SetStateCommand implements Command
{
    private LevelCreatorMode state;
    private LevelCreatorMode stateBefore;

    public SetStateCommand(final LevelCreatorMode state) {
	this.state = state;
	stateBefore = null;
    }

    @Override public void execute(final LevelCreator levelCreator) {
	stateBefore = levelCreator.getMode();
	levelCreator.setMode(state);
    }

    @Override public void undo(final LevelCreator levelCreator) {
	levelCreator.setMode(stateBefore);
    }
}
