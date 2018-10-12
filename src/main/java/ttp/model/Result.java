package ttp.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor(staticName = "of")
@EqualsAndHashCode
@ToString
public class Result {

    @Getter
    private final int profit;
    @Getter
    private final int weight;
    @Getter
    private final double totalTime;
    @Getter
    private final double value;
}
