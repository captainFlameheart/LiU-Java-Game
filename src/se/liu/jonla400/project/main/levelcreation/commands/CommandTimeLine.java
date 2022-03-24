package se.liu.jonla400.project.main.levelcreation.commands;

import se.liu.jonla400.project.main.levelcreation.LevelCreator;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a {@link Command} time line, with a pointer to the current command (executed last).
 * When a command is undone, the pointer is moved backwards. When a command is redone, the
 * pointer is moved forward. When a command is executed, commands after the current is forgotten and
 * the pointer moves forward to the newly executed command.
 */
public class CommandTimeLine
{
    private List<Command> commands;
    private int currentCommandIndex;

    private CommandTimeLine(final List<Command> commands, final int currentCommandIndex) {
	this.commands = commands;
	this.currentCommandIndex = currentCommandIndex;
    }

    /**
     * Creates an empty time line
     *
     * @return The created time line
     */
    public static CommandTimeLine createEmpty() {
	return new CommandTimeLine(new ArrayList<>(), -1);
    }

    /**
     * Executes the given command on the level creator. Any commands that could be redone
     * are forgotten.
     *
     * @param levelCreator The level creator to execute the command on
     * @param command The command to execute
     */
    public void execute(final LevelCreator levelCreator, final Command command) {
	removeCommandsAfterCurrent();
	commands.add(command);
	currentCommandIndex++;
	command.execute(levelCreator);
    }

    private void removeCommandsAfterCurrent() {
	commands.subList(currentCommandIndex + 1, commands.size()).clear();
    }

    /**
     * Undo the current command in the time line, if any
     *
     * @param levelCreator The level creator to undo the command on
     */
    public void undo(final LevelCreator levelCreator) {
	if (currentCommandIndex == -1) {
	    return;
	}
	getCurrentCommand().undo(levelCreator);
	currentCommandIndex--;
    }

    /**
     * Redo the next command in the time line, if any
     *
     * @param levelCreator The level creator to redo the next command on
     */
    public void redo(final LevelCreator levelCreator) {
	if (currentCommandIndex == commands.size() - 1) {
	    return;
	}
	currentCommandIndex++;
	getCurrentCommand().execute(levelCreator);
    }

    private Command getCurrentCommand() {
	return commands.get(currentCommandIndex);
    }
}
