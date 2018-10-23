package ttp.algorithm;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import ttp.algorithm.fitness.FitnessFunction;
import ttp.algorithm.greedy.KnapsackSolver;
import ttp.model.Individual;
import ttp.model.Node;
import ttp.model.Population;
import ttp.model.Result;
import ttp.model.params.TabuSearchParams;
import ttp.model.wrapper.ProblemInfo;
import ttp.utils.ArrayUtils;

@AllArgsConstructor(staticName = "instance")
public class TabuSearch implements Algorithm {

    private final FitnessFunction fitnessFunction;
    private final TabuSearchParams tabuSearchParams;
    private final KnapsackSolver knapsackSolver;

    @Override
    public List<Population> solve(ProblemInfo problemInfo) {
        return Collections.emptyList();
    }

    @Override
    public Individual solveForBest(ProblemInfo problemInfo) {
        int[] currentSolution = ArrayUtils
                .shuffledCopy(problemInfo.getProblem().getNodes().stream().mapToInt(Node::getId).toArray());
        int numberOfIterations = tabuSearchParams.getIterations();
        int tabuSize = problemInfo.getProblem().getDimension();

        Tabu tabuList = Tabu.of(tabuSize);
        int[] bestSolution = Arrays.copyOf(currentSolution, currentSolution.length);
        Result bestResult = Individual.of(bestSolution, problemInfo, knapsackSolver, fitnessFunction).getResult();

        for (int i = 0; i < numberOfIterations; i++) {
            currentSolution = getBestNeighbour(tabuList, currentSolution, problemInfo);
            Result currentResult = Individual.of(currentSolution, problemInfo, knapsackSolver, fitnessFunction)
                    .getResult();
            if (currentResult.getValue() > bestResult.getValue()) {
                bestSolution = Arrays.copyOf(currentSolution, currentSolution.length);
            }
        }
        return Individual.of(bestSolution, problemInfo, knapsackSolver, fitnessFunction);
    }

    private int[] getBestNeighbour(Tabu tabuList, int[] initSolution, ProblemInfo problemInfo) {
        int[] bestSolution = Arrays.copyOf(initSolution, initSolution.length);
        Result bestResult = Individual.of(bestSolution, problemInfo, knapsackSolver, fitnessFunction).getResult();
        int node1 = 0;
        int node2 = 0;
        boolean firstNeighbour = true;

        for (int i = 1; i < bestSolution.length; i++) {
            for (int j = 2; j < bestSolution.length; j++) {
                if (i == j) {
                    continue;
                }
                int[] newBestSolution = Arrays.copyOf(bestSolution, bestSolution.length);
                swap(i, j, newBestSolution);
                Result newBestResult = Individual.of(newBestSolution, problemInfo, knapsackSolver, fitnessFunction)
                        .getResult();
                if ((newBestResult.getValue() > bestResult.getValue() || firstNeighbour)
                        && tabuList.getTabuValue(i, j) == 0) {
                    firstNeighbour = false;
                    node1 = i;
                    node2 = j;
                    bestSolution = Arrays.copyOf(newBestSolution, newBestSolution.length);
                    bestResult = newBestResult;
                }
            }
        }
        if (node1 != 0) {
            tabuList.decrementTabu();
            tabuList.tabuMove(node1, node2);
        }
        return bestSolution;
    }

    private int[] swap(int i, int j, int[] solution) {
        int temp = solution[i];
        solution[i] = solution[j];
        solution[j] = temp;
        return solution;
    }

    @EqualsAndHashCode
    @ToString(includeFieldNames = false)
    private static class Tabu {

        private final int[][] tabus;

        public static Tabu of(int numberOfNodes) {
            return new Tabu(numberOfNodes);
        }

        private Tabu(int numberOfNodes) {
            tabus = new int[numberOfNodes][numberOfNodes];
        }

        public int getTabuValue(int i, int j) {
            return tabus[i][j];
        }

        public void tabuMove(int node1, int node2) {
            tabus[node1][node2] += 5;
            tabus[node2][node1] += 5;
        }

        public void decrementTabu() {
            for (int i = 0; i < tabus.length; i++) {
                for (int j = 0; j < tabus.length; j++) {
                    tabus[i][j] -= tabus[i][j] <= 0 ? 0 : 1;
                }
            }
        }
    }
}
