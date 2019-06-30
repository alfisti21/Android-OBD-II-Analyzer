package jellodev.obd2analyzerv10.obd.enums;

import java.lang.String;

public enum AvailableCommandNames {

    AIR_INTAKE_TEMP("Air Intake Temperature"),
    ENGINE_COOLANT_TEMP("Engine Coolant Temperature"),
    INTAKE_MANIFOLD_PRESSURE("Intake Manifold Pressure"),
    ENGINE_LOAD("Engine Load"),
    ENGINE_RPM("Engine RPM"),
    SPEED("Vehicle Speed"),
    ;

    private final String value;

    AvailableCommandNames(String value) {
        this.value = value;
    }

    public final String getValue() {
        return value;
    }

}
