package hr.fer.zemris.linearna;

import hr.fer.zemris.linearna.exceptions.DegenerateCaseException;
import hr.fer.zemris.linearna.exceptions.IncompatibleOperandException;

import java.util.Arrays;
import java.util.Objects;

public class Vector extends AbstractVector {

    private final double[] elements;
    private final int dimension;
    /**
     * If true prevents users from modifying this vector's elements.
     */
    private final boolean readOnly;

    /**
     * Constructs instance of this class from given array of elements.
     * NOTE: Content of given array will be copied and its content will not be changed.
     *
     * @param elements elements of new vector
     */
    public Vector(double[] elements) {
        Objects.requireNonNull(elements);

        this.dimension = elements.length;
        this.elements = Arrays.copyOf(elements, dimension);
        readOnly = false;
    }

    /**
     * Constructs instance of this class with respect to given read-only and use-array flags
     * from given array of elements.
     *
     * @param readOnly if true, disables any element-changing operation on this vector
     * @param useArgumentArray if true, uses reference to given array of elements as its own
     * @param elements elements of new vector
     */
    public Vector(boolean readOnly, boolean useArgumentArray, double[] elements) {
        Objects.requireNonNull(elements);

        this.dimension = elements.length;
        this.elements = useArgumentArray ? elements : Arrays.copyOf(elements, dimension);

        this.readOnly = readOnly;
    }

    @Override
    public double get(int i) {
        if(i < 0 || i >= dimension) {
            throw new IllegalArgumentException("Expected number from range [0," + (dimension - 1) + "].");
        }

        return elements[i];
    }

    @Override
    public IVector set(int i, double value) {
        if(i < 0 || i >= dimension) {
            throw new IllegalArgumentException("Expected number from range [0," + (dimension - 1) + "].");
        }
        if(readOnly) {
            throw new UnsupportedOperationException("Cannot modify read-only vector.");
        }

        this.elements[i] = value;
        return this;
    }

    @Override
    public int getDimension() {
        return dimension;
    }

    @Override
    public IVector copy() {
        return new Vector(readOnly, false, elements);
    }

    @Override
    public IVector newInstance(int n) {
        return new Vector(false, true, new double[n]);
    }

    public static Vector parseSimple(String string) {
        Objects.requireNonNull(string);

        double[] numbers = parseForDoubles(string);

        return new Vector(false, true, numbers);
    }

    private static double[] parseForDoubles(String string) {
        if(string.trim().isEmpty()) {
            return new double[0];
        }

        String[] numberStrings = string.trim().replaceAll("\\s+", " ").split(" ");
        double[] numbers = new double[numberStrings.length];

        for(int i = 0, length = numberStrings.length; i < length; ++i) {
            try {
                numbers[i] = Double.parseDouble(numberStrings[i]);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid number representation: " + numberStrings[i]);
            }
        }

        return numbers;
    }

    @Override
    public IVector add(IVector that) throws IncompatibleOperandException {
        if(readOnly) {
            throw new UnsupportedOperationException("Cannot modify read-only vector.");
        }

        return super.add(that);
    }

    @Override
    public IVector sub(IVector that) throws IncompatibleOperandException {
        if(readOnly) {
            throw new UnsupportedOperationException("Cannot modify read-only vector.");
        }

        return super.sub(that);
    }

    @Override
    public IVector scalarMultiply(double x) {
        if(readOnly) {
            throw new UnsupportedOperationException("Cannot modify read-only vector.");
        }

        return super.scalarMultiply(x);
    }

    @Override
    public IVector normalize() throws DegenerateCaseException {
        if(readOnly) {
            throw new UnsupportedOperationException("Cannot modify read-only vector.");
        }

        return super.normalize();
    }
}
