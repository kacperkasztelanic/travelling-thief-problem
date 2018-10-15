package ttp.model;

import java.util.Random;

import lombok.Getter;
import ttp.algorithm.FittnessFunction;
import ttp.algorithm.KnapsackSolver;

public class Individual {

    private static final int DIVISION_POINT_RATIO = 2;
    private static final Random random = new Random();

    public static Individual of(int[] nodes, ProblemInfo problemInfo, KnapsackSolver knapsackSolver,
            FittnessFunction fittnessFunction) {
        return new Individual(nodes, knapsackSolver.solve(nodes), problemInfo, knapsackSolver, fittnessFunction);
    }

    private Individual(int[] nodes, Item[] items, ProblemInfo problemInfo, KnapsackSolver knapsackSolver,
            FittnessFunction fittnessFunction) {
        this.nodes = nodes;
        this.items = items;
        this.problemInfo = problemInfo;
        this.knapsackSolver = knapsackSolver;
        this.fittnessFunction = fittnessFunction;
    }

    @Getter
    private final int[] nodes;
    @Getter
    private final Item[] items;

    private final ProblemInfo problemInfo;
    private final KnapsackSolver knapsackSolver;
    private final FittnessFunction fittnessFunction;

    private Result result;

    public Result getResult() {
        if (result == null) {
            result = fittnessFunction.calculate(problemInfo, this);
        }
        return result;
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
        return Individual.of(newNodes, problemInfo, knapsackSolver, fittnessFunction);
    }

    public Individual crossover(Individual other) {
        int divisionPoint = nodes.length / DIVISION_POINT_RATIO;
        int[] newNodes = new int[nodes.length];
        System.arraycopy(nodes, 0, newNodes, 0, divisionPoint);
        System.arraycopy(other.nodes, divisionPoint, newNodes, divisionPoint, nodes.length - divisionPoint);
        return Individual.of(repair(newNodes), problemInfo, knapsackSolver, fittnessFunction);
    }

    private int[] repair(int[] newNodes) {
        int[] counts = new int[newNodes.length];
        for (int i = 0; i < newNodes.length; i++) {
            counts[newNodes[i] - 1]++;
        }
        int[] duplicated = new int[counts.length / 2];
        int[] absent = new int[counts.length / 2];
        for (int i = 0, d = 0, a = 0; i < counts.length; i++) {
            if (counts[i] == 2) {
                duplicated[d++] = i + 1;
            } else if (counts[i] == 0) {
                absent[a++] = i + 1;
            }
        }
        for (int i = 0, d = 0; i < newNodes.length && d < duplicated.length && duplicated[d] != 0; i++) {
            if (newNodes[i] == duplicated[d]) {
                newNodes[i] = absent[d];
                d++;
            }
        }
        return newNodes;
    }
}
