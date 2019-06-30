package jellodev.obd2analyzerv10.obd.commands.protocol;


public class HeadersOffCommand extends ObdProtocolCommand {


    public HeadersOffCommand() {
        super("AT H0");
    }


    public HeadersOffCommand(HeadersOffCommand other) {
        super(other);
    }


    @Override
    public String getFormattedResult() {
        return getResult();
    }


    @Override
    public String getName() {
        return "Headers disabled";
    }

}
