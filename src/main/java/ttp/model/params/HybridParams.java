package ttp.model.params;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor(staticName = "of")
@EqualsAndHashCode
public class HybridParams {

    @Getter
    private final double hybridProbability;
    @Getter
    private final boolean hybridInitialization;

    @Override
    public String toString() {
        return "HybridParams(hybridProbability: " + hybridProbability + ", hybridInitialization: "
                + hybridInitialization + ")";
    }
}
