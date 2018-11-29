package ttp.model.params;

import java.util.Properties;

import lombok.Getter;

public class PropertyHybridParamsProvider {

    public static HybridParams forProperties(Properties props) {
        double hybridProbability = Double.parseDouble(props.getProperty(HybridProperties.HYBRID_PROBABILITY.getKey()));
        boolean hybridInitialization = Boolean
                .parseBoolean(props.getProperty(HybridProperties.HYBRID_INITIALIZATION.getKey()));
        return HybridParams.of(hybridProbability, hybridInitialization);
    }

    private PropertyHybridParamsProvider() {
    }

    public enum HybridProperties {

        HYBRID_PROBABILITY("hybrid_probability"), HYBRID_INITIALIZATION("hybrid_initialization");

        @Getter
        private final String key;

        private HybridProperties(String key) {
            this.key = key;
        }
    }
}
