package se.liu.jonla400.project.math.rootfinding;

public interface ContinousErrorStateFunction<R extends ErrorState>
{
    R getErrorStateOf(double input);
}
