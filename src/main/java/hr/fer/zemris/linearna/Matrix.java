package hr.fer.zemris.linearna;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.Arrays.stream;

public class Matrix extends AbstractMatrix {

    private final double[][] elements;
    private final int rows;
    private final int cols;

    /**
     * Constructs an instance of Matrix with given elements.
     *
     * @param elements elements of new matrix
     * @throws NullPointerException if given argument is null
     */
    public Matrix(double[][] elements) {
        this.elements = Objects.requireNonNull(elements);
        rows = elements.length;
        cols = rows == 0 ? 0 : elements[0].length;
    }

    @Override
    public int getRowsCount() {
        return rows;
    }

    @Override
    public int getColsCount() {
        return cols;
    }

    @Override
    public double get(int i, int j) {
        if(i < 0 || i >= getRowsCount()) {
            throw new IllegalArgumentException("Expected i in range [0, " + getRowsCount() +  "), got " + i);
        }
        if(j < 0 || j >= getColsCount()) {
            throw new IllegalArgumentException("Expected cols in range [0, " + getColsCount() +  "), got " + j);
        }

        return elements[i][j];
    }

    @Override
    public IMatrix set(int i, int j, double value) {
        if(i < 0 || i >= getRowsCount()) {
            throw new IllegalArgumentException("Expected i in range [0, " + getRowsCount() +  "), got " + i);
        }
        if(j < 0 || j >= getColsCount()) {
            throw new IllegalArgumentException("Expected cols in range [0, " + getColsCount() +  "), got " + j);
        }

        elements[i][j] = value;
        return this;
    }

    @Override
    public IMatrix copy() {
        return new Matrix(stream(elements).map(double[]::clone).toArray(double[][]::new));
    }

    @Override
    public IMatrix newInstance(int rows, int cols) {
        if(rows < 0) {
            throw new IllegalArgumentException("Expected rows >= 0, got " + rows);
        }
        if(cols < 0) {
            throw new IllegalArgumentException("Expected cols >= 0, got " + cols);
        }

        return new Matrix(new double[rows][cols]);
    }

    /**
     * Parses input string for matrix representation. Valid matrix string representation is one which has
     * numbers in the same row separated by whitespaces and rows separated by '|' character.
     * <p>Here is a example of a valid string representation of a 3x3 matrix:
     * <ul><li>3 44 -2 | 5 1 0 | 9 7 50</li></ul>
     *
     * @param string matrix representation
     * @throws NullPointerException if null is given as argument
     * @throws IllegalArgumentException if given string is not a valid representation
     * @return matrix represented by string
     */
    public static IMatrix parseSimple(String string) {
        Objects.requireNonNull(string);

        String[] rows = string.trim().replaceAll("\\s+", " ").split("\\s\\|\\s");
        if(rows.length == 1 && rows[0].matches("|\\s")) {
            return new Matrix(new double[0][0]);
        }

        List<double[]> elementsByRows = new ArrayList<>();
        for(String row : rows) {
            String[] stringRowElements = row.split(" ");
            double[] rowElements = new double[stringRowElements.length];

            for(int i = 0, length = stringRowElements.length; i < length; ++i) {
                try {
                    rowElements[i] = Double.parseDouble(stringRowElements[i]);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid number representation: " + e.getMessage());
                }
            }

            elementsByRows.add(rowElements);
        }

        if(elementsByRows.stream().map(t -> t.length).distinct().toArray().length != 1) {
            throw new IllegalArgumentException("All matrix rows must be of equal length.");
        }

        return new Matrix(elementsByRows.toArray(new double[0][]));
    }

    public static void main(String[] args) {
        System.out.println(parseSimple("1000 3 4 | 5 4 6 | 2 3 4"));
    }

}
