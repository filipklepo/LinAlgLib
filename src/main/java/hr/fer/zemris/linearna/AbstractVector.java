package hr.fer.zemris.linearna;

import hr.fer.zemris.linearna.exceptions.DegenerateCaseException;
import hr.fer.zemris.linearna.exceptions.IncompatibleOperandException;

import java.text.DecimalFormat;
import java.util.Objects;

/**
 * Class which implements methods which are familiar to any vector implementation.
 *
 * @author filip
 *
 */
public abstract class AbstractVector implements IVector {

    /**
     * Defines default precision of string representation of this vector's elements.
     */
    private static final int TO_STRING_PRECISION = 3;

    @Override
    public IVector add(IVector that) throws IncompatibleOperandException {
        Objects.requireNonNull(that);
        if(Integer.compare(this.getDimension(), that.getDimension()) != 0) {
            throw new IncompatibleOperandException();
        }

        for(int i = 0, dimension = this.getDimension(); i < dimension; ++i) {
            this.set(i, this.get(i) + that.get(i));
        }

        return this;
    }

    @Override
    public IVector nAdd(IVector that) throws IncompatibleOperandException {
        return this.copy().add(that);
    }

    @Override
    public IVector sub(IVector that) throws IncompatibleOperandException {
        Objects.requireNonNull(that);
        if(Integer.compare(this.getDimension(), that.getDimension()) != 0) {
            throw new IncompatibleOperandException();
        }

        for(int i = 0, dimension = this.getDimension(); i < dimension; ++i) {
            this.set(i, this.get(i) - that.get(i));
        }

        return this;
    }

    @Override
    public IVector nSub(IVector that) throws IncompatibleOperandException {
        return this.copy().sub(that);
    }

    @Override
    public IVector scalarMultiply(double x) {
        for(int i = 0, dimension = this.getDimension(); i < dimension; ++i) {
            this.set(i, this.get(i) * x);
        }

        return this;
    }

    @Override
    public IVector nScalarMultiply(double x) {
        return this.copy().scalarMultiply(x);
    }

    @Override
    public double norm() {
        double squaresSum = 0.0;
        for(int i = 0, dimension = this.getDimension(); i < dimension; ++i){
            squaresSum += Math.pow(this.get(i), 2);
        }

        return Math.sqrt(squaresSum);
    }

    @Override
    public IVector normalize() throws DegenerateCaseException {
        double norm = this.norm();
        if(Double.compare(norm, 0) == 0) {
            throw new DegenerateCaseException("Cannot normalize zero vector.");
        }

        for(int i = 0, dimension = this.getDimension(); i < dimension; ++i) {
            this.set(i, this.get(i) / norm);
        }

        return this;
    }

    @Override
    public IVector nNormalize() throws DegenerateCaseException {
        return this.copy().normalize();
    }

    @Override
    public double cosine(IVector that) throws IncompatibleOperandException, DegenerateCaseException {
        Objects.requireNonNull(that);
        if(this.getDimension() != that.getDimension()) {
            throw new IncompatibleOperandException();
        }
        if(this.norm() == 0 || that.norm() == 0) {
            throw new DegenerateCaseException(
                    (this.norm() == 0 ? "This" : "Given") + " vector is a zero vector.");
        }

        return this.scalarProduct(that) / (this.norm() * that.norm());
    }

    @Override
    public double scalarProduct(IVector that) throws IncompatibleOperandException {
        Objects.requireNonNull(that);
        if(Integer.compare(this.getDimension(), that.getDimension()) != 0) {
            throw new IncompatibleOperandException();
        }

        double result = 0.0;
        for(int i = 0, dimension = this.getDimension(); i < dimension; ++i) {
            result += this.get(i) * that.get(i);
        }

        return result;
    }

    @Override
    public IVector nVectorProduct(IVector that) throws IncompatibleOperandException {
        Objects.requireNonNull(that);
        if(Integer.compare(this.getDimension(), that.getDimension()) != 0) {
            throw new IncompatibleOperandException();
        }
        if(Integer.compare(this.getDimension(), 3) != 0) {
            throw new UnsupportedOperationException(
                    "Vector product is defined for 3-dimensional vectors.");
        }

        IVector result = this.newInstance(3);
        result.set(0, this.get(1) * that.get(2) - this.get(2) * that.get(1));
        result.set(1, -1 * (this.get(0) * that.get(2) - this.get(2) * that.get(0)));
        result.set(2, this.get(0) * that.get(1) - this.get(1) * that.get(0));

        return result;
    }

    @Override
    public IVector nFromHomogeneus() throws DegenerateCaseException {
        if(this.getDimension() <= 1) {
            return newInstance(0);
        }

        double last = this.get(this.getDimension() - 1);
        if(Double.compare(last, 0) == 0) {
            throw new DegenerateCaseException("Homogeneus component is equal to 0.");
        }
        IVector result = copyPart( this.getDimension() - 1);

        for(int i = 0, dimension = result.getDimension() - 1; i < dimension; ++i) {
            result.set(i, result.get(i) / last);
        }

        return result;
    }

    @Override
    public double[] toArray() {
        double[] array = new double[this.getDimension()];

        for(int i = 0, dimension = this.getDimension(); i < dimension; ++i) {
            array[i] = this.get(i);
        }

        return array;
    }

    @Override
    public IVector copyPart(int n) {
        if(n < 1) {
            throw new IllegalArgumentException("Expected number >= 1.");
        }

        IVector result = newInstance(n);
        int length = Integer.compare(this.getDimension(), n) < 0 ? this.getDimension() : n;
        for(int i = 0; i < length; ++i) {
            result.set(i, this.get(i));
        }

        return result;
    }

    /**
     * Gets vector's string representation with elements represented with precision
     * equal to given argument.
     *
     * @param precision vector's elements representation precision
     * @throws IllegalArgumentException if given precision is less than 0
     * @return vector representation
     */
    public String toString(int precision) {
        if(precision < 0) {
            throw new IllegalArgumentException("Precision value must be >= 0.");
        }

        DecimalFormat decimalFormat =
                new DecimalFormat("#." + new String(new char[precision]).replace("\0", "#"));
        StringBuilder sb = new StringBuilder();

        sb.append("[");
        for(int i = 0, dimension = this.getDimension(); i < dimension; ++i) {
            sb.append(decimalFormat.format(this.get(i))).append(i < dimension - 1 ? " " : "");
        }
        sb.append("]");

        return sb.toString();
    }

    @Override
    public String toString() {
        return this.toString(TO_STRING_PRECISION);
    }

    @Override
    public IMatrix toRowMatrix(boolean liveView) {
        return liveView ? new MatrixVectorView(this, true)
                        : new MatrixVectorView(this.copy(), true);
    }

    @Override
    public IMatrix toColumnMatrix(boolean liveView) {
        return liveView ? new MatrixVectorView(this, false)
                        : new MatrixVectorView(this.copy(), false);
    }
}
