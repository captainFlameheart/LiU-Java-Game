package se.liu.jonla400.project.main.game;

/**
 * Represents a listener to interesting level events, which includes both when a level is completed
 * and when a level is failed.
 */
public interface LevelListener
{
    /**
     * What to do when a level is completed
     */
    void onLevelCompleted();

    /**
     * What to do when a level is failed
     */
    void onLevelFailed();
}
