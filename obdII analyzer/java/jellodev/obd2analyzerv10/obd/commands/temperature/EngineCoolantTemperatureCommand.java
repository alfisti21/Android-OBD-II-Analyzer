package jellodev.obd2analyzerv10.obd.commands.temperature;

import jellodev.obd2analyzerv10.obd.enums.AvailableCommandNames;


public class EngineCoolantTemperatureCommand extends TemperatureCommand {


    public EngineCoolantTemperatureCommand() {
        super("01 05");
    }


    public EngineCoolantTemperatureCommand(TemperatureCommand other) {
        super(other);
    }


    @Override
    public String getName() {
        return AvailableCommandNames.ENGINE_COOLANT_TEMP.getValue();
}

}
