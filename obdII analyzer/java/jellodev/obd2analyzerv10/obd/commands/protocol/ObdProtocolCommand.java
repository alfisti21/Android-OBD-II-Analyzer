package jellodev.obd2analyzerv10.obd.commands.protocol;


import jellodev.obd2analyzerv10.obd.commands.ObdCommand;


public abstract class ObdProtocolCommand extends ObdCommand {

    public ObdProtocolCommand(String command) {
        super(command);
    }


    public ObdProtocolCommand(ObdProtocolCommand other) {
        this(other.cmd);
    }


    protected void performCalculations() {
        // ignore
    }


    protected void fillBuffer() {
        // settings commands don't return a value appropriate to place into the
        // buffer, so do nothing
    }


    @Override
    public String getCalculatedResult() {
        return String.valueOf(getResult());
    }
}
