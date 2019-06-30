package jellodev.obd2analyzerv10.obd.commands;

import java.util.Locale;

import jellodev.obd2analyzerv10.obd.enums.AvailableCommandNames;

public class SpeedCommand extends ObdCommand implements SystemOfUnits {

    private int metricSpeed = 0;

    public SpeedCommand() {
        super("01 0D");
    }

    public SpeedCommand(SpeedCommand other) {
        super(other);
    }

    @Override
    protected void performCalculations() {
        // Ignore first two bytes [hh hh] of the response.
        metricSpeed = buffer.get(2);
    }

    public int getMetricSpeed() {
        return metricSpeed;
    }

    public float getImperialSpeed() {
        return getImperialUnit();
    }

    public float getImperialUnit() {
        return metricSpeed * 0.621371192F;
    }

    public String getFormattedResult() {
        return useImperialUnits ? String.format(Locale.getDefault(), "%.2f%s", getImperialUnit(), getResultUnit())
                : String.format(Locale.getDefault(), "%d%s", getMetricSpeed(), getResultUnit());
    }

    @Override
    public String getCalculatedResult() {
        return useImperialUnits ? String.valueOf(getImperialUnit()) : String.valueOf(getMetricSpeed());
    }

    @Override
    public String getResultUnit() {
        return useImperialUnits ? "" : "";
    }

    @Override
    public String getName() {
        return AvailableCommandNames.SPEED.getValue();
    }

}
