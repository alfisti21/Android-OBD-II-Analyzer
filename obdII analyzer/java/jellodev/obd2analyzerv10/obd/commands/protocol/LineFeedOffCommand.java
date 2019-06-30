package jellodev.obd2analyzerv10.obd.commands.protocol;


public class LineFeedOffCommand extends ObdProtocolCommand {


    public LineFeedOffCommand() {
        super("AT L0");
    }


    public LineFeedOffCommand(LineFeedOffCommand other) {
        super(other);
    }


    @Override
    public String getFormattedResult() {
        return getResult();
    }


    @Override
    public String getName() {
        return "Line Feed Off";
    }

}
