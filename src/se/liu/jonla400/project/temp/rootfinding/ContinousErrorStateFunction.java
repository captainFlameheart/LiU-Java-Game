package se.liu.jonla400.project.temp.rootfinding;

public interface ContinousErrorStateFunction<R extends ErrorState>
{
    R getErrorStateOf(double input);
}
