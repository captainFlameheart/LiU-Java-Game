package se.liu.jonla400.restructure.main.levelcreation;

public interface Command
{
    void execute(LevelCreator1 levelCreator);
    void undo(LevelCreator1 levelCreator);
}
