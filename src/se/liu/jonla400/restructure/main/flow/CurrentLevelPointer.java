package se.liu.jonla400.restructure.main.flow;

import se.liu.jonla400.restructure.main.LevelDefinition;
import se.liu.jonla400.restructure.math.Interval;

public class CurrentLevelPointer
{
    private LevelList list;
    private int currentIndex;

    private CurrentLevelPointer(final LevelList list, final int currentIndex) {
	this.list = list;
	this.currentIndex = currentIndex;
    }

    public static CurrentLevelPointer createStartingAtFirstLevel(final LevelList list) {
	return new CurrentLevelPointer(list, 0);
    }

    public LevelDefinition getCurrentLevel() {
	return list.get(currentIndex);
    }

    public void step(final int steps) {
	currentIndex += steps;
	final int levelCount = list.getCount();
	if (currentIndex < 0) {
	    currentIndex = 0;
	} else if (currentIndex > levelCount) {
	    currentIndex = levelCount - 1;
	}
    }
}
