package ttp.model.params;

import java.util.Properties;

import lombok.Getter;

public class PropertyTabuSearchParamsProvider {

    public static TabuSearchParams forProperties(Properties props) {
        int iterations = Integer.parseInt(props.getProperty(TabuSearchProperties.ITERATIONS.getKey()));
        int multiplier = Integer.parseInt(props.getProperty(TabuSearchProperties.MULTIPLIER.getKey()));
        return TabuSearchParams.of(iterations, multiplier);
    }

    private PropertyTabuSearchParamsProvider() {
    }

    public enum TabuSearchProperties {

        ITERATIONS("iterations"), 
        MULTIPLIER("multiplier");

        @Getter
        private final String key;

        private TabuSearchProperties(String key) {
            this.key = key;
        }
    }
}
