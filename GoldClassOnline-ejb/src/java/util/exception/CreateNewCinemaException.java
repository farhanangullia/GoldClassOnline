/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author KERK
 */
public class CreateNewCinemaException extends Exception {

    /**
     * Creates a new instance of <code>CreateNewCinemaException</code> without
     * detail message.
     */
    public CreateNewCinemaException() {
    }

    /**
     * Constructs an instance of <code>CreateNewCinemaException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public CreateNewCinemaException(String msg) {
        super(msg);
    }
}
