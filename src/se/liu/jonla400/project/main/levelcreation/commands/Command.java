package se.liu.jonla400.project.main.levelcreation.commands;

import se.liu.jonla400.project.main.levelcreation.LevelCreator;

public interface Command
{
    void execute(LevelCreator levelCreator);

    void undo(LevelCreator levelCreator);
}
