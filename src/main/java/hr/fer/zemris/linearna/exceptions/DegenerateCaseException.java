package hr.fer.zemris.linearna.exceptions;

/**
 * Exception which is thrown when a degeneracy in mathematical sense occurs in some context.
 *
 * @author filip
 *
 */
public class DegenerateCaseException extends Exception {

    public DegenerateCaseException(String s) {
        super(s);
    }
}
