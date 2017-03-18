package hr.fer.zemris.linearna.examples;

import hr.fer.zemris.linearna.IVector;
import hr.fer.zemris.linearna.Vector;
import hr.fer.zemris.linearna.exceptions.IncompatibleOperandException;

import java.util.Scanner;

public class ReflectedVector {

    private static IVector readVectorFromUser(Scanner sc, String vectorKind) {
        System.out.print("Insert " + vectorKind + " vector coordinates: ");
        return Vector.parseSimple(sc.nextLine());
    }

    public static void main(String[] args) throws IncompatibleOperandException {
        Scanner sc = new Scanner(System.in);
        IVector mirrorVector = readVectorFromUser(sc, "mirror");
        IVector reflectionVector = readVectorFromUser(sc, "reflection");

        if(mirrorVector.getDimension() != reflectionVector.getDimension()) {
            throw new IllegalArgumentException("Vectors from input are not of same dimensionality");
        }
        if(mirrorVector.getDimension() != 2 && mirrorVector.getDimension() != 3) {
            throw new IllegalArgumentException("Vectors must be in 2D or 3D.");
        }
        System.out.println();

        IVector reflectedVector = mirrorVector.scalarMultiply(2 / mirrorVector.norm())
                                              .scalarMultiply(mirrorVector.scalarProduct(
                                                      reflectionVector) / mirrorVector.norm())
                                              .sub(reflectionVector);

        System.out.println("Reflected vector is: " + reflectedVector);
    }
}
