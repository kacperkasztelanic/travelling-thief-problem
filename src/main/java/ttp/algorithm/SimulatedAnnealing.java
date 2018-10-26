package ttp.algorithm;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import lombok.RequiredArgsConstructor;
import ttp.algorithm.fitness.FitnessFunction;
import ttp.algorithm.greedy.KnapsackSolver;
import ttp.model.Individual;
import ttp.model.Node;
import ttp.model.params.SimulatedAnnealingParams;
import ttp.model.wrapper.ProblemInfo;
import ttp.utils.ArrayUtils;

@RequiredArgsConstructor(staticName = "instance")
public class SimulatedAnnealing implements Algorithm<Individual> {

    private final FitnessFunction fitnessFunction;
    private final SimulatedAnnealingParams simulatedAnnealingParams;
    private final KnapsackSolver knapsackSolver;
    private final Random random = new Random();

    @Override
    public List<Individual> solve(ProblemInfo problemInfo) {
        int[] currentSolution = ArrayUtils
                .shuffledCopy(problemInfo.getProblem().getNodes().stream().mapToInt(Node::getId).toArray());
        List<Individual> result = new ArrayList<>();
        Individual best = Individual.of(currentSolution, problemInfo, knapsackSolver, fitnessFunction);
        double currentTemperature = simulatedAnnealingParams.getStartingTemperature();
        for (int i = 0; i < simulatedAnnealingParams.getIterations(); i++) {
            if (currentTemperature > simulatedAnnealingParams.getStopTemperature()) {
                int r1 = random.nextInt(currentSolution.length);
                int r2 = random.nextInt(currentSolution.length);
                ArrayUtils.swap(r1, r2, currentSolution);
                Individual current = Individual.of(currentSolution, problemInfo, knapsackSolver, fitnessFunction);
                if (current.getResult().getValue() > best.getResult().getValue()) {
                    best = current;
                } else if (Math
                        .exp((current.getResult().getValue() - best.getResult().getValue()) / currentTemperature) < Math
                                .random()) {
                    ArrayUtils.swap(r1, r2, currentSolution);
                }
                result.add(current);
                currentTemperature -= currentTemperature * simulatedAnnealingParams.getCoolingRate();
            }
        }
        return result;
    }

    @Override
    public Individual solveForBest(ProblemInfo problemInfo) {
        return solve(problemInfo).stream().max(Comparator.comparingDouble(i -> i.getResult().getValue()))
                .orElseThrow(IllegalStateException::new);
    }
}
