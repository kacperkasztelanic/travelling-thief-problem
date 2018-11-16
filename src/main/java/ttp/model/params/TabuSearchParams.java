package ttp.model.params;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor(staticName = "of")
@EqualsAndHashCode
public class TabuSearchParams {

    @Getter
    private final int iterations;
    @Getter
    private final int tabuDuration;

    @Override
    public String toString() {
        return "TabuSearchParams(iterations: " + iterations + ", tabuDurarion: " + tabuDuration + ")";
    }
}
