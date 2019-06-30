package jellodev.obd2analyzerv10.obd.commands.protocol;

import jellodev.obd2analyzerv10.obd.enums.ObdProtocols;


public class SelectProtocolCommand extends ObdProtocolCommand {

    private final ObdProtocols protocol;


    public SelectProtocolCommand(final ObdProtocols protocol) {
        super("AT SP " + protocol.getValue());
        this.protocol = protocol;
    }


    @Override
    public String getFormattedResult() {
        return getResult();
    }


    @Override
    public String getName() {
        return "Select Protocol " + protocol.name();
    }

}
