package jellodev.obd2analyzerv10.obd.commands.pressure;

import jellodev.obd2analyzerv10.obd.enums.AvailableCommandNames;

public class IntakeManifoldPressureCommand extends PressureCommand {

    public IntakeManifoldPressureCommand() {
        super("01 0B");
    }

    public IntakeManifoldPressureCommand(IntakeManifoldPressureCommand other) {
        super(other);
    }

    @Override
    public String getName() {
        return AvailableCommandNames.INTAKE_MANIFOLD_PRESSURE.getValue();
    }

}
