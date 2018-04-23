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
public class HallNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>HallNotFoundException</code> without
     * detail message.
     */
    public HallNotFoundException() {
    }

    /**
     * Constructs an instance of <code>HallNotFoundException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public HallNotFoundException(String msg) {
        super(msg);
    }
}
