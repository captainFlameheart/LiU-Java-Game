package se.liu.jonla400.project.main.levelcreation.commands;

import se.liu.jonla400.project.main.levelcreation.LevelCreator;

/**
 * Represents a command that can be executed on a {@link LevelCreator} to change its state.
 * A command can also be undon which, given that the level creator is in the state that
 * the execution of this command resulted in, promises to revert the state of the level creator.
 */
public interface Command
{
    /**
     * Executes this command on the level creator
     *
     * @param levelCreator The level creator to change the state of
     */
    void execute(LevelCreator levelCreator);

    /**
     * Undos this command on the level creator, given that the level creator is in the state
     * the execution of this command resulted in
     *
     * @param levelCreator The level creator to undo the state of
     */
    void undo(LevelCreator levelCreator);
}
