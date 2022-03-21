package se.liu.jonla400.project.main.levelcreation;

public class SetModeCommand implements Command
{
    private Mode oldMode;
    private Mode newMode;

    private SetModeCommand(final Mode oldMode, final Mode newMode) {
	this.oldMode = oldMode;
	this.newMode = newMode;
    }

    public static SetModeCommand createFromCurrentMode(final LevelCreator levelCreator, final Mode newMode) {
	return new SetModeCommand(levelCreator.getCurrentMode(), newMode);
    }

    @Override public void execute(final LevelCreator levelCreator) {
	levelCreator.setCurrentMode(newMode);
    }

    @Override public void undo(final LevelCreator levelCreator) {
	levelCreator.setCurrentMode(oldMode);
    }
}
