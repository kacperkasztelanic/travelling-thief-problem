package ttp.model.params;

import java.util.Properties;

import lombok.Getter;

public class PropertySimulatedAnnealingParamsProvider {

    public static SimulatedAnnealingParams forProperties(Properties props) {
        int iterations = Integer.parseInt(props.getProperty(SimulatedAnnealingProperties.ITERATIONS.getKey()));
        double startingTemperature = Double
                .parseDouble(props.getProperty(SimulatedAnnealingProperties.STARTING_TEMPERATURE.getKey()));
        double coolingRate = Double.parseDouble(props.getProperty(SimulatedAnnealingProperties.COOLING_RATE.getKey()));
        double stopTemperature = Double
                .parseDouble(props.getProperty(SimulatedAnnealingProperties.STOP_TEMPERATURE.getKey()));
        return SimulatedAnnealingParams.of(iterations, startingTemperature, coolingRate, stopTemperature);
    }

    private PropertySimulatedAnnealingParamsProvider() {
    }

    public enum SimulatedAnnealingProperties {

        ITERATIONS("sa_iterations"),//
        STARTING_TEMPERATURE("starting_temperature"),//
        COOLING_RATE("cooling_rate"),//
        STOP_TEMPERATURE("stop_temperature");

        @Getter
        private final String key;

        SimulatedAnnealingProperties(String key) {
            this.key = key;
        }
    }
}
