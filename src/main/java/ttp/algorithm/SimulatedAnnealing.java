package ttp.algorithm;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import ttp.model.Individual;
import ttp.model.Node;
import ttp.model.factory.IndividualFactory;
import ttp.model.params.SimulatedAnnealingParams;
import ttp.model.wrapper.ProblemInfo;
import ttp.utils.ArrayUtils;

@RequiredArgsConstructor(staticName = "instance")
@EqualsAndHashCode(of = { "simulatedAnnealingParams" })
@ToString(of = { "simulatedAnnealingParams" })
public class SimulatedAnnealing implements Algorithm<Individual>, ImproveStrategy {

    private final SimulatedAnnealingParams simulatedAnnealingParams;
    private final IndividualFactory individualFactory;
    private final Random random = new Random();

    @Override
    public List<Individual> solve(ProblemInfo problemInfo) {
        int[] currentSolution = ArrayUtils.shuffledCopy(//
                problemInfo.getProblem().getNodes().stream()//
                        .mapToInt(Node::getId)//
                        .toArray()//
        );
        return solveInternal(currentSolution);
    }

    @Override
    public Individual solveForBest(ProblemInfo problemInfo) {
        return findBest(solve(problemInfo));
    }

    @Override
    public Individual tryToImprove(Individual individual) {
        Individual bestFound = findBest(solveInternal(individual.getNodes()));
        return bestFound.getResult().getValue() > individual.getResult().getValue() ? bestFound : individual;
    }

    private Individual findBest(List<Individual> individuals) {
        return individuals.stream()//
                .max(Comparator.comparingDouble(i -> i.getResult().getValue()))//
                .orElseThrow(IllegalStateException::new);
    }

    private List<Individual> solveInternal(int[] currentSolution) {
        List<Individual> result = new ArrayList<>();
        Individual best = individualFactory.newIndividual(currentSolution);
        double currentTemperature = simulatedAnnealingParams.getStartingTemperature();
        for (int i = 0; i < simulatedAnnealingParams.getIterations(); i++) {
            if (currentTemperature > simulatedAnnealingParams.getStopTemperature()) {
                int r1 = random.nextInt(currentSolution.length);
                int r2 = random.nextInt(currentSolution.length);
                ArrayUtils.swap(r1, r2, currentSolution);
                Individual current = individualFactory.newIndividual(currentSolution);
                if (current.getResult().getValue() > best.getResult().getValue()) {
                    best = current;
                }
                else if (Math
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
}
