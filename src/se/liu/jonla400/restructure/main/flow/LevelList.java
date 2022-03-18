package se.liu.jonla400.restructure.main.flow;

import se.liu.jonla400.restructure.main.LevelDefinition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class LevelList
{
    private List<LevelDefinition> levels;

    private LevelList(final List<LevelDefinition> levels) {
	this.levels = levels;
    }

    public static LevelList copyFrom(final Collection<LevelDefinition> levels) {
	if (levels.isEmpty()) {
	    throw new IllegalArgumentException("No levels");
	}
	return new LevelList(new ArrayList<>(levels));
    }

    public LevelDefinition get(final int index) {
	return levels.get(index);
    }

    public int getCount() {
	return levels.size();
    }
}
