package se.liu.jonla400.project.math;

import java.util.Arrays;

/**
 * Represents a 2-by-2 matrix of values in double-precision. Contains methods for adding
 * to matricies and computing the weighted sum of a matrix.
 */
public class Matrix22
{
    private final static int DIMENSION = 2;

    private double[][] values;

    private Matrix22(final double[][] values) {
	this.values = values;
    }

    /**
     * Creates a Matrix22 with the given values
     * @param row0Col0 The value at the first row and first column
     * @param row0Col1 The value at the first row and second column
     * @param row1Col0 The value at the second row and first column
     * @param row1Col1 The value at the second row and second column
     * @return The created Matrix22
     */
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
