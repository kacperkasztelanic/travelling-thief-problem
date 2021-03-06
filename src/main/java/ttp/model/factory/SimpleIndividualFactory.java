package ttp.model.factory;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import ttp.algorithm.fitness.FitnessFunction;
import ttp.algorithm.greedy.KnapsackSolver;
import ttp.model.Individual;
import ttp.model.wrapper.ProblemInfo;
import ttp.utils.ArrayUtils;

@EqualsAndHashCode
@ToString
public class SimpleIndividualFactory implements IndividualFactory {

    protected final ProblemInfo problemInfo;
    protected final KnapsackSolver knapsackSolver;
    protected final FitnessFunction fitnessFunction;

    public static IndividualFactory instance(ProblemInfo problemInfo, KnapsackSolver knapsackSolver,
            FitnessFunction fitnessFunction) {
        return new SimpleIndividualFactory(problemInfo, knapsackSolver, fitnessFunction);
    }

    protected SimpleIndividualFactory(ProblemInfo problemInfo, KnapsackSolver knapsackSolver,
            FitnessFunction fitnessFunction) {
        this.problemInfo = problemInfo;
        this.knapsackSolver = knapsackSolver;
        this.fitnessFunction = fitnessFunction;
    }

    @Override
    public Individual newIndividual(int[] nodes) {
        return Individual.of(nodes, problemInfo, knapsackSolver, fitnessFunction);
    }

    @Override
    public Individual randomIndividual(int[] nodes) {
        return newIndividual(ArrayUtils.shuffledCopy(nodes));
    }
}
