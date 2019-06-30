package jellodev.obd2analyzerv10.obd.exceptions;

public class MisunderstoodCommandException extends ResponseException {

    public MisunderstoodCommandException() {
        super("?");
    }

}
