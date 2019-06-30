package jellodev.obd2analyzerv10.obd.commands.protocol;


public class EchoOffCommand extends ObdProtocolCommand {


    public EchoOffCommand() {
        super("AT E0");
    }


    public EchoOffCommand(EchoOffCommand other) {
        super(other);
    }


    @Override
    public String getFormattedResult() {
        return getResult();
    }


    @Override
    public String getName() {
        return "Echo Off";
    }

}
