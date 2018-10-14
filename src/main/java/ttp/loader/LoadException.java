package ttp.loader;

import java.io.IOException;

public class LoadException extends IOException {

    private static final long serialVersionUID = 1L;

    public LoadException(String message, Throwable cause) {
        super(message, cause);
    }
}
