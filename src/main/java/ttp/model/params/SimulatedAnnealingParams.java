package ttp.model.params;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor(staticName = "of")
@EqualsAndHashCode
public class SimulatedAnnealingParams {

    @Getter
    private final int iterations;
    @Getter
    private final double startingTemperature;
    @Getter
    private final double coolingRate;
    @Getter
    private final double stopTemperature;

    @Override
    public String toString() {
        return "SimulatedAnnealingParams(iterations: " + iterations //
                + ", startingTemperature: " + startingTemperature//
                + ", coolingRate: " + coolingRate //
                + ", stopTemperature: " + stopTemperature + ")";
    }
}
