package ttp.model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import ttp.algorithm.fitness.FitnessFunction;
import ttp.algorithm.greedy.KnapsackSolver;
import ttp.model.wrapper.ProblemInfo;

@EqualsAndHashCode
public class Individual {

    private static final int DIVISION_POINT_RATIO = 2;

    @Getter
    protected final int[] nodes;
    @Getter
    protected final Item[] items;

    protected final ProblemInfo problemInfo;
    protected final KnapsackSolver knapsackSolver;
    protected final FitnessFunction fitnessFunction;
    protected final Random random = new Random();

    @Getter(lazy = true)
    private final Result result = fitnessFunction.calculate(problemInfo, this);

    public static Individual of(int[] nodes, ProblemInfo problemInfo, KnapsackSolver knapsackSolver,
            FitnessFunction fitnessFunction) {
        return new Individual(nodes, knapsackSolver.solve(nodes), problemInfo, knapsackSolver, fitnessFunction);
    }

    protected Individual(int[] nodes, Item[] items, ProblemInfo problemInfo, KnapsackSolver knapsackSolver,
            FitnessFunction fitnessFunction) {
        this.nodes = nodes;
        this.items = items;
        this.problemInfo = problemInfo;
        this.knapsackSolver = knapsackSolver;
        this.fitnessFunction = fitnessFunction;
    }

    public Individual mutate(double probability) {
        int[] newNodes = new int[nodes.length];
        System.arraycopy(nodes, 0, newNodes, 0, nodes.length);
        for (int i = 0; i < nodes.length - 1; i++) {
            if (random.nextDouble() <= probability) {
                int j = random.nextInt(newNodes.length - 1);
                int temp = newNodes[i];
                newNodes[i] = newNodes[j];
                newNodes[j] = temp;
            }
        }
        return Individual.of(newNodes, problemInfo, knapsackSolver, fitnessFunction);
    }

    public Individual crossover(Individual other) {
        int divisionPoint = nodes.length / DIVISION_POINT_RATIO;
        int[] newNodes = new int[nodes.length];
        System.arraycopy(nodes, 0, newNodes, 0, divisionPoint);
        System.arraycopy(other.nodes, divisionPoint, newNodes, divisionPoint, nodes.length - divisionPoint);
        return Individual.of(repair(newNodes), problemInfo, knapsackSolver, fitnessFunction);
    }

    @SuppressWarnings("all")
    private int[] repair(int[] newNodes) {
        int[] counts = new int[newNodes.length];
        for (int i = 0; i < newNodes.length; i++) {
            counts[newNodes[i] - 1]++;
        }
        Set<Integer> duplicated = new HashSet<>(counts.length / 2);
        int[] absent = new int[counts.length / 2];
        for (int i = 0, a = 0; i < counts.length; i++) {
            if (counts[i] == 2) {
                duplicated.add(i + 1);
            } else if (counts[i] == 0) {
                absent[a++] = i + 1;
            }
        }
        for (int i = 0, a = 0; i < newNodes.length && a < absent.length && absent[a] != 0; i++) {
            if (duplicated.contains(newNodes[i])) {
                duplicated.remove(newNodes[i]);
                newNodes[i] = absent[a++];
            }
        }
        return newNodes;
    }

    @Override
    public String toString() {
        return "Individual(nodes: " + Arrays.toString(nodes) + ", items: " + Arrays.toString(items) + ")";
    }

    public Individual copy() {
        int[] newNodes = Arrays.copyOf(nodes, nodes.length);
        Item[] newItems = Arrays.copyOf(items, items.length);
        return new Individual(newNodes, newItems, problemInfo, knapsackSolver, fitnessFunction);
    }
}
