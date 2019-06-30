package jellodev.obd2analyzerv10.obd.commands.temperature;

import java.util.Locale;
import java.lang.String;

import jellodev.obd2analyzerv10.obd.commands.ObdCommand;
import jellodev.obd2analyzerv10.obd.commands.SystemOfUnits;


public abstract class TemperatureCommand extends ObdCommand implements
        SystemOfUnits {

    private float temperature = 0.0f;


    public TemperatureCommand(String cmd) {
        super(cmd);
    }


    public TemperatureCommand(TemperatureCommand other) {
        super(other);
    }


    @Override
    protected void performCalculations() {
        // ignore first two bytes [hh hh] of the response
        temperature = buffer.get(2) - 40;
    }



    @Override
    public String getFormattedResult() {
        return useImperialUnits ? String.format(Locale.getDefault(), "%.1f%s", getImperialUnit(), getResultUnit())
                : String.format(Locale.getDefault(), "%.0f%s", temperature, getResultUnit());
    }


    @Override
    public String getCalculatedResult() {
        return useImperialUnits ? String.valueOf(getImperialUnit()) : String.valueOf(temperature);
    }


    @Override
    public String getResultUnit() {
        return useImperialUnits ? "" : "";
    }


    public float getTemperature() {
        return temperature;
    }


    public float getImperialUnit() {
        return temperature * 1.8f + 32;
}


    public float getKelvin() {
        return temperature + 273.15f;
    }


    public abstract String getName();

}
