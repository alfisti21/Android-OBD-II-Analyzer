package jellodev.obd2analyzerv10.obd.commands;

import java.util.Locale;

public abstract class PercentageObdCommand extends ObdCommand {

    protected float percentage = 0f;

    public PercentageObdCommand(String command) {
        super(command);
    }

    public PercentageObdCommand(PercentageObdCommand other) {
        super(other);
    }

    @Override
    protected void performCalculations() {
        // ignore first two bytes [hh hh] of the response
        percentage = (buffer.get(2) * 100.0f) / 255.0f;
    }

    @Override
    public String getFormattedResult() {
        return String.format(Locale.getDefault(), "%.1f%s", percentage, getResultUnit());
    }

    public float getPercentage() {
        return percentage;
    }

    @Override
    public String getResultUnit() {
        return "";
    }

    @Override
    public String getCalculatedResult() {
        return String.valueOf(percentage);
    }

}
