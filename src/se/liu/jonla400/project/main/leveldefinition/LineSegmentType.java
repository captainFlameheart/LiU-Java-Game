package se.liu.jonla400.project.main.leveldefinition;

import java.awt.*;

public enum LineSegmentType
{
    DEFAULT(Color.BLACK), LOOSE(Color.RED), WIN(Color.GREEN);

    private final Color color;

    LineSegmentType(final Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}