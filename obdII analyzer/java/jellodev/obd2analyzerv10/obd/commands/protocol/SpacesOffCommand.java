package jellodev.obd2analyzerv10.obd.commands.protocol;



public class SpacesOffCommand extends ObdProtocolCommand {

    public SpacesOffCommand() {
        super("AT S0");
    }


    public SpacesOffCommand(SpacesOffCommand other) {
        super(other);
    }

    @Override
    public String getFormattedResult() {
        return getResult();
    }

    @Override
    public String getName() {
        return "Spaces Off";
    }
}
