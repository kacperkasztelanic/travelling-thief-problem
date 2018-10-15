package ttp.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@EqualsAndHashCode
@ToString
public class GeneticParams {

    @Getter
    private final int populationSize;
    @Getter
    private final int numberOfGenerations;
    @Getter
    private final double crossoverProbability;
    @Getter
    private final double mutationProbability;
    @Getter
    private final double tournamentSize;
}
