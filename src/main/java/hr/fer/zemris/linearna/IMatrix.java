package hr.fer.zemris.linearna;

import hr.fer.zemris.linearna.exceptions.IncompatibleOperandException;

public interface IMatrix {

    public int getRowsCount();

    public int getColsCount();

    public double get(int i, int j);

    public IMatrix set(int i, int j, double value);

    public IMatrix copy();

    public IMatrix newInstance(int rows, int cols);

    public IMatrix nTransponse(boolean liveView);

    public IMatrix add(IMatrix that) throws IncompatibleOperandException;

    public IMatrix nAdd(IMatrix that) throws IncompatibleOperandException;

    public IMatrix sub(IMatrix that) throws IncompatibleOperandException;

    public IMatrix nSub(IMatrix that) throws IncompatibleOperandException;

    public IMatrix nMultiply(IMatrix that) throws IncompatibleOperandException;

    public double determinant();

    public IMatrix subMatrix(int row, int col, boolean liveView);

    public IMatrix nInvert();

    public double[][] toArray();

    public IVector toVector(boolean asRowMatrix);

}
