package hr.fer.zemris.linearna;

import java.util.Objects;

public class MatrixVectorView extends AbstractMatrix {

    private final IVector vector;
    private final boolean asRowMatrix;

    public MatrixVectorView(IVector vector, boolean asRowMatrix) {
        this.vector = Objects.requireNonNull(vector);
        this.asRowMatrix = Objects.requireNonNull(asRowMatrix);
    }

    @Override
    public int getRowsCount() {
        return asRowMatrix ? 1 : vector.getDimension();
    }

    @Override
    public int getColsCount() {
        return asRowMatrix ? vector.getDimension() : 1;
    }

    @Override
    public double get(int i, int j) {
        if(i < 0 || i >= getRowsCount()) {
            throw new IllegalArgumentException("Expected i in [0, " + getRowsCount() + ") range, got " + i);
        }
        if(j < 0 || i >= getColsCount()) {
            throw new IllegalArgumentException("Expected j in [0, " + getColsCount() + ") range, got " + j);
        }

        return asRowMatrix ? vector.get(j) : vector.get(i);
    }

    @Override
    public IMatrix set(int i, int j, double value) {
        if(i < 0 || i >= getRowsCount()) {
            throw new IllegalArgumentException("Expected i in [0, " + getRowsCount() + ") range, got " + i);
        }
        if(j < 0 || i >= getColsCount()) {
            throw new IllegalArgumentException("Expected j in [0, " + getColsCount() + ") range, got " + j);
        }

        if(asRowMatrix) {
            vector.set(j, value);
        } else {
            vector.set(i, value);
        }

        return this;
    }

    @Override
    public IMatrix copy() {
        return new MatrixVectorView(vector.copy(), asRowMatrix);
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
}
