package hr.fer.zemris.linearna.examples;

import hr.fer.zemris.linearna.IMatrix;
import hr.fer.zemris.linearna.IVector;
import hr.fer.zemris.linearna.Matrix;
import hr.fer.zemris.linearna.Vector;
import hr.fer.zemris.linearna.exceptions.DegenerateCaseException;
import hr.fer.zemris.linearna.exceptions.IncompatibleOperandException;

import java.util.Scanner;

public class LabAssignment {

    public static void firstSubtask() throws IncompatibleOperandException, DegenerateCaseException {
        System.out.println("First subtask:");
        System.out.println();

        IVector v1 = Vector.parseSimple("2 3 -4").add(Vector.parseSimple("-1 4 -3"));
        System.out.println("v1 = " + v1);

        System.out.println("s = " + v1.scalarProduct(Vector.parseSimple("-1 4 -3")));
        IVector v2 = v1.nVectorProduct(Vector.parseSimple("2 2 4"));

        System.out.println("v2 = " + v2);
        System.out.println("v3 = " + v2.copy().normalize());
        System.out.println("v4 = " + v2.scalarMultiply(-1));

        System.out.println("M1 = " + Matrix.parseSimple("1 2 3 | 2 1 3 | 4 5 1")
                                                .add(Matrix.parseSimple("-1 2 -3 | 5 -2 7 | -4 -1 3")));

        System.out.println("M2 = " + Matrix.parseSimple("1 2 3 | 2 1 3 | 4 5 1")
                                                .nMultiply(Matrix.parseSimple("-1 2 -3 | 5 -2 7 | -4 -1 3")
                                                            .nTransponse(false)));

        System.out.println("M3 = " + Matrix.parseSimple("-24 18 5 | 20 -15 -4 | -5 4 1")
                                                .nInvert()
                                                .nMultiply(Matrix.parseSimple("1 2 3 | 0 1 4 | 5 6 0").nInvert()));
    }

    public static void secondSubtask() throws IncompatibleOperandException {
        Scanner sc = new Scanner(System.in);
        IMatrix linearEquationValues = readMatrixFromUser(sc, 3, 3, "linear equation values");
        IMatrix linearEquationResults = readMatrixFromUser(sc, 3, 1, "linear equation results");

        System.out.println("[x, y, z] = " +
                linearEquationValues.nInvert().nMultiply(linearEquationResults).nTransponse(false));
    }

    private static IMatrix readMatrixFromUser(Scanner sc, int rows, int cols, String matrixKind) {
        System.out.print("Insert " + rows + "x" + cols + " " + matrixKind + " matrix: ");
        IMatrix result = Matrix.parseSimple(sc.nextLine());
        if(result.getRowsCount() != rows || result.getColsCount() != cols) {
            throw new IllegalArgumentException("Expected " + rows + "x" + cols + " matrix");
        }

        return result;
    }

    public static void thirdSubtask() throws IncompatibleOperandException {
        Scanner sc = new Scanner(System.in);

        IMatrix verticesCoords = readMatrixFromUser(sc, 3, 3, "triangle vertex coordinates");
        IMatrix pointCoord = readMatrixFromUser(sc, 1, 3, "point T coordinates");
        IMatrix result = verticesCoords.nInvert().nMultiply(pointCoord.nTransponse(false));

        System.out.println("Barycentric coordinates are: " + result.nTransponse(false));
    }

    public static void main(String[] args) throws DegenerateCaseException, IncompatibleOperandException {
        thirdSubtask();
    }
}
