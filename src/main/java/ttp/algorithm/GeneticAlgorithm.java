package ttp.algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import ttp.model.Individual;
import ttp.model.Population;
import ttp.model.factory.IndividualFactory;
import ttp.model.params.GeneticParams;
import ttp.model.wrapper.ProblemInfo;

@AllArgsConstructor(staticName = "instance")
@EqualsAndHashCode(of = { "geneticParams" })
@ToString(of = { "geneticParams" })
public class GeneticAlgorithm implements Algorithm<Population> {

    private final GeneticParams geneticParams;
    private final IndividualFactory individualFactory;

    @Override
    public List<Population> solve(ProblemInfo problemInfo) {
        List<Population> generations = new ArrayList<>(geneticParams.getNumberOfGenerations());
        Population first = Population.firstPopulation(geneticParams, problemInfo, individualFactory);
        generations.add(first);
        for (int i = 1; i < geneticParams.getNumberOfGenerations(); i++) {
            generations.add(generations.get(i - 1).nextGeneration());
        }
        return generations;
    }

    @Override
    public Individual solveForBest(ProblemInfo problemInfo) {
        return solve(problemInfo).stream()//
                .map(Population::getMembers)//
                .flatMap(Arrays::stream)//
                .max(Comparator.comparingDouble(a -> a.getResult().getValue()))//
                .orElseThrow(IllegalStateException::new);
    }
}
