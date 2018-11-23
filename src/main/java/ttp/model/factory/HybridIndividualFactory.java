package ttp.model.factory;

import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;
import ttp.algorithm.ImproveStrategy;
import ttp.algorithm.fitness.FitnessFunction;
import ttp.algorithm.greedy.KnapsackSolver;
import ttp.model.HybridIndividual;
import ttp.model.Individual;
import ttp.model.wrapper.ProblemInfo;
import ttp.utils.ArrayUtils;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class HybridIndividualFactory extends SimpleIndividualFactory {

    @Setter
    private ImproveStrategy improveStrategy;

    public static HybridIndividualFactory instance(ProblemInfo problemInfo, KnapsackSolver knapsackSolver,
            FitnessFunction fitnessFunction) {
        return new HybridIndividualFactory(problemInfo, knapsackSolver, fitnessFunction);
    }

    private HybridIndividualFactory(ProblemInfo problemInfo, KnapsackSolver knapsackSolver,
            FitnessFunction fitnessFunction) {
        super(problemInfo, knapsackSolver, fitnessFunction);
    }

    @Override
    public HybridIndividual newIndividual(int[] nodes) {
        return HybridIndividual.of(nodes, problemInfo, knapsackSolver, fitnessFunction, improveStrategy);
    }

    @Override
    public Individual randomIndividual(int[] nodes) {
        return improveStrategy.tryToImprove(newIndividual(ArrayUtils.shuffledCopy(nodes)));
    }
}
