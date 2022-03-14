package se.liu.jonla400.restructure.math;

import java.util.Arrays;

/**
 * Represents a 2 by 2 matrix of values with double precision. Can be used for
 * matrix operations as well as for solving systems of linear equations with
 * two linear equations and two unknowns.
 */
public class Matrix22
{
    public final static int DIMENSION = 2;

    private double[][] values;

    /**
     * Creates a new 2 by 2 matrix with the given values. The input
     * to this constructor is an array containing both of the rows,
     * and each row is an array of the values of that row. The values
     * will be copied, meaning that modifiying the input array after
     * this matrix has been created will not change the values of this
     * matrix.
     *
     * @param values The values to copy from
     */
    public Matrix22(final double[][] values) {
	if (values.length != DIMENSION) {
	    throw new IllegalArgumentException("Wrong number of rows: " + values.length);
	}
	if (values[0].length != DIMENSION) {
	    throw new IllegalArgumentException("Wrong number of columns: " + values[0].length);
	}

	// Initialize the values of this matrix by copying from the given values
	this.values = new double[DIMENSION][DIMENSION];
	for (int row = 0; row < DIMENSION; row++) {
	    for (int col = 0; col < DIMENSION; col++) {
		this.values[row][col] = values[row][col];
	    }
	}
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
     * Returns the value at the given row and column. The first row has the
     * index 0 and the second row has the index 1. The same goes for the columns.
     *
     * @param row The index of the row
     * @param col The index of the column
     * @return The value at the given row and column
     */
    public double getValueAt(final int row, final int col) {
	return values[row][col];
    }

    /**
     * Solves a system of linear equations: this * x = product where * represent
     * matrix multiplication
     *
     * @param product
     * @return
     */
    public Vector2D getFactorIfProductIs(final Vector2D product) {
	// ONLY TEMPORARY!!!
	double a11 = values[0][0];
	double a12 = values[0][1];
	double a21 = values[1][0];
	double a22 = values[1][1];

	double b1 = product.getX();
	double b2 = product.getY();

	double invDet = 1 / (a11 * a22 - a12 * a21);

	double x = (a22 * b1 - a12 * b2) * invDet;
	double y = (a11 * b2 - a21 * b1) * invDet;
	return Vector2D.createCartesianVector(x, y);
    }

    @Override public String toString() {
	return "Matrix22{" + "values=[" + Arrays.toString(values[0]) + ", " + Arrays.toString(values[1]) + "]}";
    }
}
