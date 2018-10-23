package ttp.model.params;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@EqualsAndHashCode
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

    @Override
    public String toString() {
        return "GeneticParams(populationSize: " + populationSize + ", numberOfGenerations: " + numberOfGenerations
                + ", crossoverProbability: " + crossoverProbability + ", mutationProbability: " + mutationProbability
                + ", tournamentSize: " + tournamentSize + ")";
    }
}
