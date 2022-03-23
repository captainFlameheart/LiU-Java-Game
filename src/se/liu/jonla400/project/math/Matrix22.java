package se.liu.jonla400.project.math;

import java.util.Arrays;

public class Matrix22
{
    private final static int DIMENSION = 2;

    private double[][] values;

    private Matrix22(final double[][] values) {
	this.values = values;
    }

    public static Matrix22 create(
	    final double row0Col0, final double row0Col1,
	    final double row1Col0, final double row1Col1)
    {
	return new Matrix22(new double[][]{
		{row0Col0, row0Col1},
		{row1Col0, row1Col1}
	});
    }

    /**
     * Returns the result of performing matrix addition between this and the other matrix
     *
     * @param other The matrix to add
     * @return The result of the addition
     */
    public Matrix22 add(final Matrix22 other) {
	final double[][] sum = new double[DIMENSION][DIMENSION];
	for (int row = 0; row < DIMENSION; row++) {
	    for (int col = 0; col < DIMENSION; col++) {
		sum[row][col] = values[row][col] + other.values[row][col];
	    }
	}
	return new Matrix22(sum);
    }

    /**
     * Returns the sum of each value multiplied by the corresponding weight
     *
     * @param weights The weight of each value
     * @return The weighted sum
     */
    public double getWeightedSum(final Matrix22 weights) {
	double weightedSum = 0;
	for (int row = 0; row < DIMENSION; row++) {
	    for (int col = 0; col < DIMENSION; col++) {
		weightedSum += values[row][col] * weights.values[row][col];
	    }
	}
	return weightedSum;
    }

    @Override public String toString() {
	return "Matrix22{" + "values=[" + Arrays.toString(values[0]) + ", " + Arrays.toString(values[1]) + "]}";
    }
}
