package jellodev.obd2analyzerv10.obd.commands.protocol;

public class TimeoutCommand extends ObdProtocolCommand {

    public TimeoutCommand(int timeout) {
        super("AT ST " + Integer.toHexString(0xFF & timeout));
    }

    public TimeoutCommand(TimeoutCommand other) {
        super(other);
    }


    @Override
    public String getFormattedResult() {
        return getResult();
    }


    @Override
    public String getName() {
        return "Timeout";
    }

}
