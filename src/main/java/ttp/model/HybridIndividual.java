package ttp.model;

import java.util.Arrays;

import lombok.EqualsAndHashCode;
import ttp.algorithm.ImproveStrategy;
import ttp.algorithm.fitness.FitnessFunction;
import ttp.algorithm.greedy.KnapsackSolver;
import ttp.model.wrapper.ProblemInfo;

@EqualsAndHashCode(callSuper = true)
public class HybridIndividual extends Individual implements Cloneable {

    private final ImproveStrategy improveStrategy;

    public static HybridIndividual of(int[] nodes, ProblemInfo problemInfo, KnapsackSolver knapsackSolver,
            FitnessFunction fitnessFunction, ImproveStrategy improveStrategy) {
        return new HybridIndividual(nodes, knapsackSolver.solve(nodes), problemInfo, knapsackSolver, fitnessFunction,
                improveStrategy);
    }

    private HybridIndividual(int[] nodes, Item[] items, ProblemInfo problemInfo, KnapsackSolver knapsackSolver,
            FitnessFunction fitnessFunction, ImproveStrategy improveStrategy) {
        super(nodes, items, problemInfo, knapsackSolver, fitnessFunction);
        this.improveStrategy = improveStrategy;
    }

    @Override
    public Individual mutate(double probability) {
        if (random.nextDouble() < probability * 30) {
            return improveStrategy.tryToImprove(this);
        }
        return super.mutate(probability);
    }

    @Override
    public HybridIndividual clone() {
        int[] newNodes = Arrays.copyOf(nodes, nodes.length);
        Item[] newItems = Arrays.copyOf(items, items.length);
        return new HybridIndividual(newNodes, newItems, problemInfo, knapsackSolver, fitnessFunction, improveStrategy);
    }
}
