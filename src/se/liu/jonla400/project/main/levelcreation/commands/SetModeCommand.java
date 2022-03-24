package se.liu.jonla400.project.main.levelcreation.commands;

import se.liu.jonla400.project.main.levelcreation.LevelCreator;
import se.liu.jonla400.project.main.levelcreation.modes.Mode;

/**
 * Represents a {@link Command} that sets the mode of a {@link LevelCreator}. When undon,
 * it resets the old mode.
 */
public class SetModeCommand implements Command
{
    private Mode oldMode;
    private Mode newMode;

    private SetModeCommand(final Mode oldMode, final Mode newMode) {
	this.oldMode = oldMode;
	this.newMode = newMode;
    }

    /**
     * Creates a SetModeCommand that, when undon, resets the mode that the level creator currently has
     *
     * @param levelCreator The level creator containing the mode to go back to when undoing the command
     * @param newMode The new mode to set
     * @return The created SetModeCommand
     */
    public static SetModeCommand createFromCurrentMode(final LevelCreator levelCreator, final Mode newMode) {
	return new SetModeCommand(levelCreator.getCurrentMode(), newMode);
    }

    /**
     * Sets the mode
     *
     * @param levelCreator The level creator to change the state of
     */
    @Override public void execute(final LevelCreator levelCreator) {
	levelCreator.setCurrentMode(newMode);
    }

    /**
     * Resets the mode
     *
     * @param levelCreator The level creator to undo the state of
     */
    @Override public void undo(final LevelCreator levelCreator) {
	levelCreator.setCurrentMode(oldMode);
    }
}
