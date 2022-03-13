package se.liu.jonla400.project.main;

public class LevelTimeStepResult
{
    private boolean levelIsCompleted;

    public LevelTimeStepResult(final boolean levelIsCompleted) {
	this.levelIsCompleted = levelIsCompleted;
    }

    public boolean levelIsCompleted() {
	return levelIsCompleted;
    }
}
