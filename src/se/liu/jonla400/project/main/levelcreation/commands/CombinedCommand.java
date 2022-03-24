package se.liu.jonla400.project.main.levelcreation.commands;

import se.liu.jonla400.project.main.levelcreation.LevelCreator;

import java.util.Arrays;
import java.util.Collection;

/**
 * Represents a set of {@link Command} that are executed/undon collectively.
 * In what order the commands are executed/undone in is left unspecified.
 */
public class CombinedCommand implements Command
{
    private Collection<Command> commands;

    private CombinedCommand(final Collection<Command> commands) {
	this.commands = commands;
    }

    /**
     * Creates a new CombinedCommand of the given commands. The order of the input commands
     * are not taken into consideration.
     *
     * @param commands The commands to be executed/undon collectively
     * @return The created CombinedCommand
     */
    public static CombinedCommand create(final Command... commands) {
	return new CombinedCommand(Arrays.asList(commands));
    }

    /**
     * Executes each command one by one, in no particular order
     *
     * @param levelCreator The level creator to execute the commands on
     */
    @Override public void execute(final LevelCreator levelCreator) {
	commands.forEach(command -> command.execute(levelCreator));
    }

    /**
     * Undos each command one by one, in no particular order
     *
     * @param levelCreator The level creator to execute the commands on
     */
    @Override public void undo(final LevelCreator levelCreator) {
	commands.forEach(command -> command.undo(levelCreator));
    }
}
