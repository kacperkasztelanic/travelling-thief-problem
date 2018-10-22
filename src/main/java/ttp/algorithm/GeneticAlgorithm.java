package ttp.algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.AllArgsConstructor;
import ttp.algorithm.fitness.FitnessFunction;
import ttp.algorithm.greedy.KnapsackSolver;
import ttp.model.GeneticParams;
import ttp.model.Individual;
import ttp.model.Population;
import ttp.model.wrapper.ProblemInfo;

@AllArgsConstructor(staticName = "instance")
public class GeneticAlgorithm implements Algorithm {

    private final FitnessFunction fittnessFunction;
    private final GeneticParams geneticParams;
    private final KnapsackSolver knapsackSolver;

    @Override
    public Individual solveForBest(ProblemInfo problemInfo) {
        return solve(problemInfo).stream().map(Population::getMembers).flatMap(Arrays::stream)
                .max((a, b) -> Double.compare(b.getResult().getValue(), a.getResult().getValue()))
                .orElseThrow(IllegalStateException::new);
    }

    @Override
    public List<Population> solve(ProblemInfo problemInfo) {
        List<Population> generations = new ArrayList<>(geneticParams.getNumberOfGenerations());
        Population first = Population.randomPopulation(geneticParams, problemInfo, knapsackSolver, fittnessFunction);
        generations.add(first);
        for (int i = 1; i < geneticParams.getNumberOfGenerations(); i++) {
            generations.add(generations.get(i - 1).nextGeneration());
        }
        return generations;
    }
}
