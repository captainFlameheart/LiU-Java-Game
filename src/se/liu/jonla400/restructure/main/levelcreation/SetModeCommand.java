package se.liu.jonla400.restructure.main.levelcreation;

public class SetModeCommand implements Command
{
    private LevelCreatorMode state;
    private LevelCreatorMode stateBefore;

    public SetModeCommand(final LevelCreatorMode state) {
	this.state = state;
	stateBefore = null;
    }

    @Override public void execute(final LevelCreator levelCreator) {
	stateBefore = levelCreator.getCurrentMode();
	levelCreator.setCurrentMode(state);
    }

    @Override public void undo(final LevelCreator levelCreator) {
	levelCreator.setCurrentMode(stateBefore);
    }
}
