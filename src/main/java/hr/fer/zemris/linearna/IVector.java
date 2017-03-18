package hr.fer.zemris.linearna;

import hr.fer.zemris.linearna.exceptions.DegenerateCaseException;
import hr.fer.zemris.linearna.exceptions.IncompatibleOperandException;

/**
 * Models a mathematical object vector. Methods with prefix 'n' return do not modify vectors on which they were called,
 * but instead return a new vector as result each time they are invoked.
 *
 * @author filip
 *
 */
public interface IVector {

    public double get(int i);

    public IVector set(int i, double value);

    public int getDimension();

    public IVector copy();

    /**
     * Copies this vector up to length "i" and returns it as new instance. If given length is
     * greater than this vector's dimension the remaining part is filled with zeroes.
     *
     * @param i copied part length
     * @return copied vector
     */
    public IVector copyPart(int i);

    /**
     * Creates an instance of @IVector with dimension i, and values of its
     * elements set to 0.
     *
     * @param i dimension of vector
     * @return new vector
     */
    public IVector newInstance(int i);

    public IVector add(IVector that) throws IncompatibleOperandException;

    public IVector nAdd(IVector that) throws IncompatibleOperandException;

    public IVector sub(IVector that) throws IncompatibleOperandException;

    public IVector nSub(IVector that) throws IncompatibleOperandException;

    public IVector scalarMultiply(double x);

    public IVector nScalarMultiply(double x);

    public double norm();

    public IVector normalize() throws DegenerateCaseException;

    public IVector nNormalize() throws DegenerateCaseException;

    public double cosine(IVector that) throws IncompatibleOperandException, DegenerateCaseException;

    public double scalarProduct(IVector that) throws IncompatibleOperandException;

    public IVector nVectorProduct(IVector that) throws IncompatibleOperandException;

    public IVector nFromHomogeneus() throws DegenerateCaseException;

    public IMatrix toRowMatrix(boolean liveView);

    public IMatrix toColumnMatrix(boolean liveView);

    public double[] toArray();

}
