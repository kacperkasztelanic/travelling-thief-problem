package ttp.presenter;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor(staticName = "of")
@EqualsAndHashCode
@ToString(includeFieldNames = false)
public class Statistics {

    @Getter
    private final double minValue;
    @Getter
    private final double maxValue;
    @Getter
    private final double avgValue;
}
