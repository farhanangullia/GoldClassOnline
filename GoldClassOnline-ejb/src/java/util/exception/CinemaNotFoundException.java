/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author 
 */
public class CinemaNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>CinemaNotFoundException</code> without
     * detail message.
     */
    public CinemaNotFoundException() {
    }

    /**
     * Constructs an instance of <code>CinemaNotFoundException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public CinemaNotFoundException(String msg) {
        super(msg);
    }
}
