package se.liu.jonla400.project.main.levelcreation.commands;

import se.liu.jonla400.project.main.levelcreation.LevelCreator;

/**
 * Represents a reversed {@link Command} that when executed undos the original command and when
 * undon executes the original command
 */
public class ReversedCommand implements Command
{
    private Command command;

    /**
     * Creates a reversed version of the given command
     *
     * @param command The command to reverse
     */
    public ReversedCommand(final Command command) {
	this.command = command;
    }

    /**
     * Undos the original command
     *
     * @param levelCreator The level creator to change the state of
     */
    @Override public void execute(final LevelCreator levelCreator) {
	command.undo(levelCreator);
    }

    /**
     * Executes the original command
     *
     * @param levelCreator The level creator to undo the state of
     */
    @Override public void undo(final LevelCreator levelCreator) {
	command.execute(levelCreator);
    }
}
