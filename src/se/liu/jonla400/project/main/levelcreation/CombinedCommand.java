package se.liu.jonla400.project.main.levelcreation;

import java.util.Arrays;
import java.util.Collection;

public class CombinedCommand implements Command
{
    private Collection<Command> commands;

    private CombinedCommand(final Collection<Command> commands) {
	this.commands = commands;
    }

    public static CombinedCommand create(final Command... commands) {
	return new CombinedCommand(Arrays.asList(commands));
    }

    @Override public void execute(final LevelCreator levelCreator) {
	commands.forEach(command -> command.execute(levelCreator));
    }

    @Override public void undo(final LevelCreator levelCreator) {
	commands.forEach(command -> command.undo(levelCreator));
    }
}
