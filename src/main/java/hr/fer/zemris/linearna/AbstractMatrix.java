package hr.fer.zemris.linearna;

import hr.fer.zemris.linearna.exceptions.IncompatibleOperandException;

import java.util.Objects;

/**
 * Class which implements methods which are familiar to any matrix implementation.
 *
 * @author filip
 *
 */
public abstract class AbstractMatrix implements IMatrix {

    /**
     * Defines default precision of string representation of this matrix's elements.
     */
    public static final int DEFAULT_REPRESENTATION_PRECISION = 3;

    @Override
    public IMatrix nTransponse(boolean liveView) {
        if(liveView) {
            return new MatrixTransponseView(this);
        }

        IMatrix resultMatrix = this.newInstance(this.getColsCount(), this.getRowsCount());
        for(int i = 0, rowsCount = this.getRowsCount(); i < rowsCount; ++i) {
            for(int j = 0, colsCount = this.getColsCount(); j < colsCount; ++j) {
                resultMatrix.set(j, i, this.get(i, j));
            }
        }

        return resultMatrix;
    }

    @Override
    public IMatrix add(IMatrix that) throws IncompatibleOperandException {
        if(!equalDimensions(this, Objects.requireNonNull(that))) {
            throw new IncompatibleOperandException();
        }

        for(int i = 0, rowsCount = this.getRowsCount(); i < rowsCount; ++i) {
            for(int j = 0, colsCount = this.getColsCount(); j < colsCount; ++j) {
                this.set(i, j, this.get(i, j) + that.get(i, j));
            }
        }

        return this;
    }

    @Override
    public IMatrix nAdd(IMatrix that) throws IncompatibleOperandException {
        return this.copy().add(that);
    }

    @Override
    public IMatrix sub(IMatrix that) throws IncompatibleOperandException {
        if(!equalDimensions(this, Objects.requireNonNull(that))) {
            throw new IncompatibleOperandException();
        }

        for(int i = 0, rowsCount = this.getRowsCount(); i < rowsCount; ++i) {
            for(int j = 0, colsCount = this.getColsCount(); j < colsCount; ++j) {
                this.set(i, j, this.get(i, j) - that.get(i, j));
            }
        }

        return this;
    }

    @Override
    public IMatrix nSub(IMatrix that) throws IncompatibleOperandException {
        return this.copy().sub(that);
    }

    private static boolean equalDimensions(IMatrix matrixOne, IMatrix matrixTwo) {
        return matrixOne.getRowsCount() == matrixTwo.getRowsCount() &&
                matrixOne.getColsCount() == matrixTwo.getColsCount();
    }

    @Override
    public IMatrix nMultiply(IMatrix that) throws IncompatibleOperandException {
        if(this.getColsCount() != Objects.requireNonNull(that).getRowsCount()) {
            throw new IncompatibleOperandException();
        }

        IMatrix resultMatrix = this.newInstance(this.getRowsCount(), that.getColsCount());

        for(int i = 0, rowsCount = this.getRowsCount(); i <  rowsCount; ++i) {
            for(int j = 0, thatColsCount = that.getColsCount(); j < thatColsCount; ++j) {
                    double cellResult = 0;
                    for(int k = 0, thatRowsCount = that.getRowsCount(); k < thatRowsCount; ++k) {
                        cellResult += this.get(i, k) * that.get(k, j);
                    }

                    resultMatrix.set(i, j, cellResult);
            }
        }

        return resultMatrix;
    }

    @Override
    public double determinant() {
        if(Integer.compare(this.getRowsCount(), this.getColsCount()) != 0) {
            throw new UnsupportedOperationException("Determinant is defined for square matrices only.");
        }
        if(this.getRowsCount() == 0) {
            return Double.NaN;
        }
        if(this.getRowsCount() == 1) {
            return this.get(0, 0);
        }
        if(this.getRowsCount() == 2) {
            return this.get(0, 0) * this.get(1, 1) - this.get(0, 1) * this.get(1, 0);
        }

        double result = 0.0;
        for(int i = 0, colsCount = this.getColsCount(); i < colsCount; ++i) {
            result += (i % 2 == 0 ? 1 : -1) * get(0, i) * new MatrixSubMatrixView(this, 0, i).determinant();
        }

        return result;
    }

    @Override
    public IMatrix subMatrix(int row, int col, boolean liveView) {
        if(row < 0 || row >= getRowsCount()) {
            throw new IllegalArgumentException("Expected rows in range [0, " + getRowsCount() +  "), got " + row);
        }
        if(col < 0 || col >= getColsCount()) {
            throw new IllegalArgumentException("Expected cols in range [0, " + getColsCount() +  "), got " + col);
        }

        if(liveView) {
            return new MatrixSubMatrixView(this, row, col);
        }

        IMatrix subMatrix = this.newInstance(this.getRowsCount() - 1, this.getColsCount() - 1);
        for(int i = 0, rowsCount = this.getRowsCount(), newI = 0; i < rowsCount; ++i){
            if(i == row) continue;

            for(int j = 0, colsCount = this.getColsCount(), newJ = 0; j < colsCount; ++j) {
                if(j == col) continue;

                subMatrix.set(newI, newJ++, this.get(i, j));
            }
            newI++;
        }

        return subMatrix;
    }

    /**
     * Calculates inverse matrix using the adjoint method. Adjoint method is used for calculating the
     * inverse of non-singular matrix A by multiplying adjoint matrix of A with 1 / det A.
     * Adjoint matrix of A is the transponsed version of cofactor matrix of A. Cofactor matrix of A
     * consists of elements that are cofactors, term-by-term, of given matrix.
     *
     * @throws UnsupportedOperationException if this is a singular matrix or a non-square matrix
     * @return inverse matrix
     */
    @Override
    public IMatrix nInvert() {
        double determinant = determinant();
        if(determinant == 0) {
            throw new UnsupportedOperationException("Cannot calculate inverse of singular matrix.");
        }
        if(Integer.compare(getRowsCount(), getColsCount()) != 0) {
            throw new UnsupportedOperationException("Cannot calculate inverse of non-square matrix.");
        }

        return nCofactorMatrixMultipliedWithScalar(this, 1 / determinant).nTransponse(false);
    }

    public static IMatrix nCofactorMatrixMultipliedWithScalar(IMatrix matrix, double scalar) {
        IMatrix cofactorMatrix = matrix.newInstance(matrix.getRowsCount(), matrix.getColsCount());

        for(int i = 0, rowsCount = matrix.getRowsCount(), newI = 0; i < rowsCount; ++i){

            for(int j = 0, colsCount = matrix.getColsCount(), newJ = 0; j < colsCount; ++j) {
                cofactorMatrix.set(i,
                                   j,
                                   ((i + j) % 2 == 0 ? 1 : -1) * matrix.subMatrix(i, j, false).determinant() * scalar);
            }
        }

        return cofactorMatrix;
    }

    @Override
    public double[][] toArray() {
        double[][] array = new double[this.getRowsCount()][this.getColsCount()];

        for(int i = 0, rowsCount = this.getRowsCount(); i < rowsCount; ++i) {
            for(int j = 0, colsCount = this.getColsCount(); j < colsCount; ++j) {
                array[i][j] = this.get(i, j);
            }
        }

        return array;
    }

    @Override
    public String toString() {
        return this.toString(DEFAULT_REPRESENTATION_PRECISION);
    }

    /**
     * Gets matrix's string representation with elements represented with precision
     * equal to given argument.
     *
     * @param precision matrix's elements representation precision
     * @throws IllegalArgumentException if given precision is less than 0
     * @return vector representation
     */
    public String toString(int precision) {
        if(precision < 0) {
            throw new IllegalArgumentException("Precision value must be >= 0.");
        }

        StringBuilder sb = new StringBuilder();
        String formatString = "%" + maximumIntegerPartLengthOfElements() + "." + precision + "f";

        for(int i = 0, rowsCount = this.getRowsCount(); i < rowsCount; ++i) {
            sb.append("[");
            for(int j = 0, colsCount = this.getColsCount(); j < colsCount; ++j) {
                sb.append(String.format(formatString, this.get(i, j))).append(j == colsCount - 1 ? "]\n" : ", ");
            }
        }

        return sb.toString();
    }

    /**
     * Gets maximum length of integer parts of matrix's elements, with default (minimum) value being one.
     *
     * @return maximum length
     */
    private int maximumIntegerPartLengthOfElements() {
        int max = 1;

        for(int i = 0, rowsCount = this.getRowsCount(); i < rowsCount; ++i) {
            for(int j = 0, colsCount = this.getColsCount(); j < colsCount; ++j) {
                int wholePartLength = Double.valueOf(get(i, j)).toString().split("\\.")[0].length();
                max = Integer.compare(max, wholePartLength) < 0 ? wholePartLength : max;
            }
        }

        return max;
    }

    @Override
    public IVector toVector(boolean asRowMatrix) {
        return new VectorMatrixView(this, asRowMatrix);
    }
}
