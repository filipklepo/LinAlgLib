package hr.fer.zemris.linearna;


import java.util.Objects;

public class MatrixTransponseView extends AbstractMatrix {

    private final IMatrix matrix;

    public MatrixTransponseView(IMatrix matrix) {
        this.matrix = Objects.requireNonNull(matrix);
    }

    @Override
    public int getRowsCount() {
        return matrix.getColsCount();
    }

    @Override
    public int getColsCount() {
        return matrix.getRowsCount();
    }

    @Override
    public double get(int i, int j) {
        if(i >= matrix.getColsCount() || i < 0) {
            throw new IllegalArgumentException(
                    "Row index is not in [0, " + matrix.getColsCount() + ") range, got + " + i);
        }
        if(j >= matrix.getRowsCount() || j < 0) {
            throw new IllegalArgumentException(
                    "Column index is not in [0, " + matrix.getRowsCount() + ") range, got + " + j);
        }

        return matrix.get(j, i);
    }

    @Override
    public IMatrix set(int i, int j, double value) {
        if(i >= matrix.getColsCount() || i < 0) {
            throw new IllegalArgumentException(
                    "Row index is not in [0, " + matrix.getColsCount() + ") range, got + " + i);
        }
        if(j >= matrix.getRowsCount() || j < 0) {
            throw new IllegalArgumentException(
                    "Column index is not in [0, " + matrix.getRowsCount() + ") range, got + " + j);
        }

        matrix.set(j, i, value);
        return this;
    }

    @Override
    public IMatrix copy() {
        return new MatrixTransponseView(this.matrix);
    }

    @Override
    public IMatrix newInstance(int rows, int cols) {
        return matrix.newInstance(rows, cols);
    }

    @Override
    public double[][] toArray() {
        double[][] result = new double[this.getRowsCount()][this.getColsCount()];

        for(int i = 0, rowsCount = this.getRowsCount(); i < rowsCount; ++i) {
            for(int j = 0, colsCount = this.getColsCount(); j < colsCount; ++j) {
                result[i][j] = this.get(i, j);
            }
        }

        return result;
    }
}