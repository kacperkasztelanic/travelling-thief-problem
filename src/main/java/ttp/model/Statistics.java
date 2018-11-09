package ttp.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor(staticName = "of")
@EqualsAndHashCode
@ToString(includeFieldNames = true)
public class Statistics {

    @Getter
    private final double minValue;
    @Getter
    private final double maxValue;
    @Getter
    private final double avgValue;
    @Getter
    private final double stdDev;
}
