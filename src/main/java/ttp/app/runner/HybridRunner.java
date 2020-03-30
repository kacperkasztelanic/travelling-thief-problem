package ttp.app.runner;

import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ttp.algorithm.Algorithm;
import ttp.algorithm.GeneticAlgorithm;
import ttp.algorithm.ImproveStrategy;
import ttp.algorithm.SimulatedAnnealing;
import ttp.algorithm.fitness.FitnessFunction;
import ttp.algorithm.greedy.KnapsackSolver;
import ttp.model.Population;
import ttp.model.Statistics;
import ttp.model.factory.HybridIndividualFactory;
import ttp.model.params.GeneticParams;
import ttp.model.params.HybridParams;
import ttp.model.params.SimulatedAnnealingParams;
import ttp.model.wrapper.ProblemInfo;
import ttp.presenter.ResultPresenter;
import ttp.utils.StatisticsUtils;

public class HybridRunner extends AbstractRunner<GeneticParams> {

    private final SimulatedAnnealingParams simulatedAnnealingParams;
    private final HybridParams hybridParams;

    public HybridRunner(ProblemInfo problemInfo, GeneticParams params, FitnessFunction fitnessFunction,
            KnapsackSolver knapsackSolver, PrintWriter pw, SimulatedAnnealingParams simulatedAnnealingParams,
            HybridParams hybridParams) {
        super(problemInfo, params, fitnessFunction, knapsackSolver, pw);
        this.simulatedAnnealingParams = simulatedAnnealingParams;
        this.hybridParams = hybridParams;
    }

    @Override
    public void run(int runs, List<ResultPresenter> presenters) {
        HybridIndividualFactory individualFactory = HybridIndividualFactory
                .instance(getProblemInfo(), getKnapsackSolver(), getFitnessFunction(), hybridParams);
        ImproveStrategy improveStrategy = SimulatedAnnealing.instance(simulatedAnnealingParams, individualFactory);
        individualFactory.setImproveStrategy(improveStrategy);
        Algorithm<Population> algorithm = GeneticAlgorithm.instance(getParams(), individualFactory);
        List<List<Population>> solution = Stream.generate(() -> algorithm.solve(getProblemInfo()))//
                .limit(runs)//
                .collect(Collectors.toList());
        List<Statistics> statistics = StatisticsUtils.analyzeMultiplePopulationListsEach(solution);
        Statistics res = StatisticsUtils.analyzeMultiplePopulationLists(solution);
        getPw().println("GeneticAlgorithm statistics:");
        presenters.forEach(p -> p.present(statistics));
        getPw().println(res);
    }
}
