package se.liu.jonla400.project.main.levelcreation.commands;

import se.liu.jonla400.project.main.levelcreation.LevelCreator;

public class ReversedCommand implements Command
{
    private Command command;

    public ReversedCommand(final Command command) {
	this.command = command;
    }

    @Override public void execute(final LevelCreator levelCreator) {
	command.undo(levelCreator);
    }

    @Override public void undo(final LevelCreator levelCreator) {
	command.execute(levelCreator);
    }
}
