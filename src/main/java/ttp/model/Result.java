package ttp.model;

import java.util.Locale;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor(staticName = "of")
@EqualsAndHashCode
public class Result {

    @Getter
    private final int profit;
    @Getter
    private final int weight;
    @Getter
    private final double totalTime;
    @Getter
    private final double value;

    @Override
    public String toString() {
        return String.format(//
                Locale.US, //
                "Result(profit: %d, weight: %d, totalTime: %.2f, value: %.2f)",//
                profit, weight, totalTime, value//
        );
    }
}
