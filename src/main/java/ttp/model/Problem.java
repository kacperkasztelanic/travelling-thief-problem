package ttp.model;

import java.util.List;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@EqualsAndHashCode(of = { "problemName" })
@ToString(includeFieldNames = false, of = { "problemName" })
public class Problem {

    @Getter
    private final String problemName;
    @Getter
    private final String knapsackDataType;
    @Getter
    private final int dimension;
    @Getter
    private final int numberOfItems;
    @Getter
    private final int capacityOfKnapsack;
    @Getter
    private final double minSpeed;
    @Getter
    private final double maxSpeed;
    @Getter
    private final double rentingRatio;
    @Getter
    private final String edgeWeightType;
    @Getter
    private final List<Node> nodes;
    @Getter
    private final List<Item> items;
}
