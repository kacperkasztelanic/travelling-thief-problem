package ttp.model.factory;

import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;
import ttp.algorithm.ImproveStrategy;
import ttp.algorithm.fitness.FitnessFunction;
import ttp.algorithm.greedy.KnapsackSolver;
import ttp.model.HybridIndividual;
import ttp.model.Individual;
import ttp.model.params.HybridParams;
import ttp.model.wrapper.ProblemInfo;
import ttp.utils.ArrayUtils;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class HybridIndividualFactory extends SimpleIndividualFactory {

    private final HybridParams hybridParams;
    
    @Setter
    private ImproveStrategy improveStrategy;

    public static HybridIndividualFactory instance(ProblemInfo problemInfo, KnapsackSolver knapsackSolver,
            FitnessFunction fitnessFunction, HybridParams hybridParams) {
        return new HybridIndividualFactory(problemInfo, knapsackSolver, fitnessFunction, hybridParams);
    }

    private HybridIndividualFactory(ProblemInfo problemInfo, KnapsackSolver knapsackSolver,
            FitnessFunction fitnessFunction, HybridParams hybridParams) {
        super(problemInfo, knapsackSolver, fitnessFunction);
        this.hybridParams = hybridParams;
    }

    @Override
    public HybridIndividual newIndividual(int[] nodes) {
        return HybridIndividual.of(nodes, problemInfo, knapsackSolver, fitnessFunction, improveStrategy, hybridParams);
    }

    @Override
    public Individual randomIndividual(int[] nodes) {
        if(!hybridParams.isHybridInitialization()) {
            return super.randomIndividual(nodes);
        }
        return improveStrategy.tryToImprove(newIndividual(ArrayUtils.shuffledCopy(nodes)));            
    }
}
