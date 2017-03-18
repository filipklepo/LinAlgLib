package hr.fer.zemris.linearna;

import java.util.Arrays;
import java.util.Objects;

public class MatrixSubMatrixView extends AbstractMatrix {

    private final IMatrix matrix;
    private final int[] rowIndexes;
    private final int[] colIndexes;

    public MatrixSubMatrixView(IMatrix matrix, int row, int col) {
        Objects.requireNonNull(matrix);
        if(row < 0 || row >= matrix.getRowsCount()) {
            throw new IllegalArgumentException(
                    "Row argument not in valid [0, " + matrix.getRowsCount() + ") range, got " + row);
        }
        if(col < 0 || col >= matrix.getColsCount()) {
            throw new IllegalArgumentException(
                    "Col argument not in valid [0, " + matrix.getColsCount() + ") range, got " + col);
        }

        this.matrix = matrix;
        this.rowIndexes = rangeWithout(0, matrix.getRowsCount() - 1, row);
        this.colIndexes = rangeWithout(0, matrix.getColsCount() - 1, col);
    }

    private MatrixSubMatrixView(IMatrix matrix, int[] rowIndexes, int[] colIndexes) {
        this.matrix = Objects.requireNonNull(matrix);

        this.rowIndexes = rowIndexes;
        this.colIndexes = colIndexes;
    }

    private static int[] rangeWithout(int from, int to, int without) {
        int[] result = new int[to - from];

        for(int i = from, resultI = 0; i <= to; ++i) {
           if(i == without) continue;

           result[resultI++] = i;
        }

        return result;
    }

    @Override
    public int getRowsCount() {
        return rowIndexes.length;
    }

    @Override
    public int getColsCount() {
        return colIndexes.length;
    }

    @Override
    public double get(int i, int j) {
        if(i < 0 || i >= rowIndexes.length) {
            throw new IllegalArgumentException("Expected i in [0, " + rowIndexes.length + ") range, got " + i);
        }
        if(j < 0 || j >= colIndexes.length) {
            throw new IllegalArgumentException("Expected j in [0, " + colIndexes.length + ") range, got " + j);
        }

        return matrix.get(rowIndexes[i], colIndexes[j]);
    }

    @Override
    public IMatrix set(int i, int j, double value) {
        if(i < 0 || i >= rowIndexes.length) {
            throw new IllegalArgumentException("Expected i in [0, " + rowIndexes.length + ") range, got " + i);
        }
        if(j < 0 || j >= colIndexes.length) {
            throw new IllegalArgumentException("Expected j in [0, " + colIndexes.length + ") range, got " + j);
        }

        this.matrix.set(this.rowIndexes[i], this.colIndexes[j], value);
        return this;
    }

    @Override
    public IMatrix copy() {
        return new MatrixSubMatrixView(this.matrix, this.rowIndexes, this.colIndexes) ;
    }

    @Override
    public IMatrix newInstance(int rows, int cols) {
        return matrix.newInstance(rows, cols);
    }
}
