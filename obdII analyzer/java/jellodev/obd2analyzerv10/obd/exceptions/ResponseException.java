package jellodev.obd2analyzerv10.obd.exceptions;

public class ResponseException extends RuntimeException {

    private String message;

    private String response;

    private String command;

    private boolean matchRegex;

     ResponseException(String message) {
        this.message = message;
    }

     ResponseException(String message, boolean matchRegex) {
        this.message = message;
        this.matchRegex = matchRegex;
    }

    private static String clean(String s) {
        return s == null ? "" : s.replaceAll("\\s", "").toUpperCase();
    }

    public boolean isError(String response) {
        this.response = response;
        if (matchRegex) {
            return clean(response).matches(clean(message));
        } else {
            return clean(response).contains(clean(message));
        }
    }

    public void setCommand(String command) {
        this.command = command;
    }

    @Override
    public String getMessage() {
        return "Error running " + command + ", response: " + response;
    }

}
