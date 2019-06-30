package jellodev.obd2analyzerv10.obd.exceptions;

public class UnknownErrorException extends ResponseException {

    public UnknownErrorException() {
        super("ERROR");
    }

}
