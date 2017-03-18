package hr.fer.zemris.linearna.examples;

import hr.fer.zemris.linearna.IMatrix;
import hr.fer.zemris.linearna.IVector;
import hr.fer.zemris.linearna.Matrix;
import hr.fer.zemris.linearna.Vector;
import hr.fer.zemris.linearna.exceptions.IncompatibleOperandException;

public class BarycentricCoordinates {

    public static void firstTechnique() throws IncompatibleOperandException {
        IVector a = Vector . parseSimple ( "1 0 0" ) ;
        IVector b = Vector . parseSimple ( "5 0 0" ) ;
        IVector c = Vector . parseSimple ( "3 8 0" ) ;
        IVector t = Vector. parseSimple ( "3 4 0" ) ;

        double pov = b.nSub(a).nVectorProduct(c.nSub(a)).norm() / 2.0;
        double povA = b.nSub(t).nVectorProduct(c.nSub(t)).norm() / 2.0;
        double povB = a.nSub(t).nVectorProduct(c.nSub(t)).norm() / 2.0;
        double povC = a.nSub(t).nVectorProduct(b.nSub(t)).norm() / 2.0;

        double t1 = povA / pov;
        double t2 = povB / pov;
        double t3 = povC / pov;

        System.out.println(" Barycentric coordinates are : (" + t1 + ", " + t2 + ", " + t3 + "). ");
    }

    public static void secondTechnique() throws IncompatibleOperandException {
        IMatrix triangleCoords = Matrix.parseSimple("1 5 3 | 0 0 8 | 1 1 1");
        IMatrix pointCoords = Matrix.parseSimple("3 | 4 | 1");
        IMatrix result = triangleCoords.nInvert().nMultiply(pointCoords);

        System.out.println(
                " Barycentric coordinates are : (" +
                        result.get(0, 0) +
                        ", " +
                        result.get(1, 0) +
                        ", " +
                        result.get(2, 0)
                        + "). ");
    }

    public static void main(String[] args) throws IncompatibleOperandException {
        firstTechnique();
        secondTechnique();
    }

}
