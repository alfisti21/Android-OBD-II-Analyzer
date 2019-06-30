package jellodev.obd2analyzerv10.obd.commands.engine;

import jellodev.obd2analyzerv10.obd.commands.PercentageObdCommand;
import jellodev.obd2analyzerv10.obd.enums.AvailableCommandNames;

public class LoadCommand extends PercentageObdCommand {


    public LoadCommand() {
        super("01 04");
    }


    public LoadCommand(LoadCommand other) {
        super(other);
    }


    @Override
    public String getName() {
        return AvailableCommandNames.ENGINE_LOAD.getValue();
    }

}
