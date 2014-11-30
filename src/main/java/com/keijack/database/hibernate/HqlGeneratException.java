package com.keijack.database.hibernate;

/**
 * 
 * @author keijack.wu
 * 
 */
public class HqlGeneratException extends RuntimeException {

    private static final long serialVersionUID = 2L;

    public HqlGeneratException() {
	super();
    }

    public HqlGeneratException(String message) {
	super(message);
    }

    public HqlGeneratException(Throwable cause) {
	super(cause);
    }

    public HqlGeneratException(String message, Throwable cause) {
	super(message, cause);
    }

}
