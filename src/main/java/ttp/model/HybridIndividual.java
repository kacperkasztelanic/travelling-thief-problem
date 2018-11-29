package ttp.model;

import java.util.Arrays;

import lombok.EqualsAndHashCode;
import ttp.algorithm.ImproveStrategy;
import ttp.algorithm.fitness.FitnessFunction;
import ttp.algorithm.greedy.KnapsackSolver;
import ttp.model.params.HybridParams;
import ttp.model.wrapper.ProblemInfo;

@EqualsAndHashCode(callSuper = true)
public class HybridIndividual extends Individual {

    private final ImproveStrategy improveStrategy;
    private final HybridParams hybridParams;

    public static HybridIndividual of(int[] nodes, ProblemInfo problemInfo, KnapsackSolver knapsackSolver,
            FitnessFunction fitnessFunction, ImproveStrategy improveStrategy, HybridParams hybridParams) {
        return new HybridIndividual(nodes, knapsackSolver.solve(nodes), problemInfo, knapsackSolver, fitnessFunction,
                improveStrategy, hybridParams);
    }

    private HybridIndividual(int[] nodes, Item[] items, ProblemInfo problemInfo, KnapsackSolver knapsackSolver,
            FitnessFunction fitnessFunction, ImproveStrategy improveStrategy, HybridParams hybridParams) {
        super(nodes, items, problemInfo, knapsackSolver, fitnessFunction);
        this.improveStrategy = improveStrategy;
        this.hybridParams = hybridParams;
    }

    @Override
    public Individual mutate(double probability) {
        Individual mutated = super.mutate(probability);
        if (random.nextDouble() < hybridParams.getHybridProbability()) {
            return improveStrategy.tryToImprove(mutated);
        }
        return mutated;
    }

    @Override
    public HybridIndividual copy() {
        int[] newNodes = Arrays.copyOf(nodes, nodes.length);
        Item[] newItems = Arrays.copyOf(items, items.length);
        return new HybridIndividual(newNodes, newItems, problemInfo, knapsackSolver, fitnessFunction, improveStrategy,
                hybridParams);
    }
}
