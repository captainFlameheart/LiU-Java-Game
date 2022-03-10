package se.liu.jonla400.project.temp.rootfinding;

public class InputToErrorStateMap<R extends ErrorState>
{
    private double input;
    private R errorState;

    public InputToErrorStateMap(final double input, final R errorState) {
	this.input = input;
	this.errorState = errorState;
    }

    public double getInput() {
	return input;
    }

    public R getErrorState() {
	return errorState;
    }
}
