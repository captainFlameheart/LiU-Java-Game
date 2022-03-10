package se.liu.jonla400.project.temp.rootfinding;

import se.liu.jonla400.project.math.Interval;

import java.util.Optional;

// TODO: REMOVE!!!
public class Test
{
    public static void main(String[] args) {
	final Test1 function = new Test1();
	final SmallErrorFinder smallErrorFinder = new SmallErrorFinder(10, 100, 0.000000001);
	final Optional<InputToErrorStateMap<Test2>> map = smallErrorFinder.findSmallErrorState(function, new Interval(0.9999, -2));
	System.out.println(map.get().getInput() + " -> " + map.get().getErrorState().getError());
	System.out.println(map.get().getErrorState().a);
    }

    private static class Test1 implements ContinousErrorStateFunction<Test2> {

	@Override public Test2 getErrorStateOf(final double x) {
	    return new Test2(x + 1, x * x - 1);
	}
    }

    private static class Test2 implements ErrorState {

	private double a;
	private double error;

	public Test2(final double a, final double error) {
	    this.a = a;
	    this.error = error;
	}

	@Override public double getError() {
	    return error;
	}
    }

}
