package hr.fer.zemris.linearna;

import java.util.Objects;

public class VectorMatrixView extends AbstractVector {

    private final IMatrix matrix;
    private final int dimension;
    private final boolean rowMatrix;

    public VectorMatrixView(IMatrix matrix, boolean rowMatrix) {
        this.matrix = Objects.requireNonNull(matrix);
        this.dimension = rowMatrix ? matrix.getRowsCount() : matrix.getColsCount();
        this.rowMatrix = rowMatrix;
    }

    @Override
    public double get(int i) {
        if(i < 0 || i >= dimension) {
            throw new IllegalArgumentException("Expected i in [0, " + getDimension() + ") range, got " + i);
        }

        return rowMatrix ? matrix.get(0, i) : matrix.get(i, 0);
    }

    @Override
    public IVector set(int i, double value) {
        if(i < 0 || i >= dimension) {
            throw new IllegalArgumentException("Expected i in [0, " + getDimension() + ") range, got " + i);
        }

        if(rowMatrix) {
            matrix.set(0, i, value);
        } else {
            matrix.set(i, 0, value);
        }

        return this;
    }

    @Override
    public int getDimension() {
        return rowMatrix ? matrix.getColsCount() : matrix.getRowsCount();
    }

    @Override
    public IVector copy() {
        return new VectorMatrixView(matrix.copy(), rowMatrix);
    }

    @Override
    public IVector newInstance(int i) {
        if(i < 0) {
            throw new IllegalArgumentException("Expected i >= 0, got " + i);
        }

        return new Vector(new double[i]);
    }
}
