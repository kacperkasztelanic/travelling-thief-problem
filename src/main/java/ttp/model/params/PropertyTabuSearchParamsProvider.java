package ttp.model.params;

import java.util.Properties;

import lombok.Getter;

public class PropertyTabuSearchParamsProvider {

    public static TabuSearchParams forProperties(Properties props) {
        int iterations = Integer.parseInt(props.getProperty(TabuSearchProperties.ITERATIONS.getKey()));
        int tabuDuration = Integer.parseInt(props.getProperty(TabuSearchProperties.TABU_DURATION.getKey()));
        return TabuSearchParams.of(iterations, tabuDuration);
    }

    private PropertyTabuSearchParamsProvider() {
    }

    public enum TabuSearchProperties {

        ITERATIONS("ts_iterations"),//
        TABU_DURATION("tabu_duration");

        @Getter
        private final String key;

        TabuSearchProperties(String key) {
            this.key = key;
        }
    }
}
