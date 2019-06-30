package jellodev.obd2analyzerv10.obd.commands.temperature;

import jellodev.obd2analyzerv10.obd.enums.AvailableCommandNames;


public class AirIntakeTemperatureCommand extends TemperatureCommand {


    public AirIntakeTemperatureCommand() {
        super("01 0F");
    }


    public AirIntakeTemperatureCommand(AirIntakeTemperatureCommand other) {
        super(other);
    }


    @Override
    public String getName() {
        return AvailableCommandNames.AIR_INTAKE_TEMP.getValue();
    }

}
