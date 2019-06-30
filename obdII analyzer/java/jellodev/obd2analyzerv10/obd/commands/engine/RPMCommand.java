package jellodev.obd2analyzerv10.obd.commands.engine;

import java.util.Locale;

import jellodev.obd2analyzerv10.obd.commands.ObdCommand;
import jellodev.obd2analyzerv10.obd.enums.AvailableCommandNames;

public class RPMCommand extends ObdCommand {

    private int rpm = -1;

    public RPMCommand() {
        super("01 0C");
    }

    public RPMCommand(RPMCommand other) {
        super(other);
    }


    @Override
    protected void performCalculations() {
        // ignore first two bytes [41 0C] of the response((A*256)+B)/4
        rpm = (buffer.get(2) * 256 + buffer.get(3)) / 4;
    }


    @Override
    public String getFormattedResult() {
        return String.format(Locale.getDefault(), "%d%s", rpm, getResultUnit());
    }


    @Override
    public String getCalculatedResult() {
        return String.valueOf(rpm);
    }


    @Override
    public String getResultUnit() {
        return "";
    }


    @Override
    public String getName() {
        return AvailableCommandNames.ENGINE_RPM.getValue();
    }


    public int getRPM() {
        return rpm;
    }

}
