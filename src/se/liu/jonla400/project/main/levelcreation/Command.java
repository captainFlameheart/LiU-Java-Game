package se.liu.jonla400.project.main.levelcreation;

public interface Command
{
    void execute(LevelCreator levelCreator);

    void undo(LevelCreator levelCreator);
}
