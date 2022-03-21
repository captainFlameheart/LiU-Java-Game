package se.liu.jonla400.project.main.levelcreation;

import java.util.ArrayList;
import java.util.List;

public class CommandTimeLine
{
    private List<Command> commands;
    private int currentCommandIndex;

    private CommandTimeLine(final List<Command> commands, final int currentCommandIndex) {
	this.commands = commands;
	this.currentCommandIndex = currentCommandIndex;
    }

    public static CommandTimeLine createEmpty() {
	return new CommandTimeLine(new ArrayList<>(), -1);
    }

    public void execute(final LevelCreator levelCreator, final Command command) {
	removeCommandsAfterCurrent();
	commands.add(command);
	currentCommandIndex++;
	command.execute(levelCreator);
    }

    private void removeCommandsAfterCurrent() {
	commands.subList(currentCommandIndex + 1, commands.size()).clear();
    }

    public void undo(final LevelCreator levelCreator) {
	if (currentCommandIndex == -1) {
	    return;
	}
	commands.get(currentCommandIndex).undo(levelCreator);
	currentCommandIndex--;
    }

    public void redo(final LevelCreator levelCreator) {
	if (currentCommandIndex == commands.size() - 1) {
	    return;
	}
	currentCommandIndex++;
	commands.get(currentCommandIndex).execute(levelCreator);
    }
}
