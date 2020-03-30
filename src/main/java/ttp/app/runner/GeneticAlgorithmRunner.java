package ttp.app.runner;

import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ttp.algorithm.Algorithm;
import ttp.algorithm.GeneticAlgorithm;
import ttp.algorithm.fitness.FitnessFunction;
import ttp.algorithm.greedy.KnapsackSolver;
import ttp.model.Population;
import ttp.model.Statistics;
import ttp.model.factory.IndividualFactory;
import ttp.model.factory.SimpleIndividualFactory;
import ttp.model.params.GeneticParams;
import ttp.model.wrapper.ProblemInfo;
import ttp.presenter.ResultPresenter;
import ttp.utils.StatisticsUtils;

public class GeneticAlgorithmRunner extends AbstractRunner<GeneticParams> {

    public GeneticAlgorithmRunner(ProblemInfo problemInfo, GeneticParams params, FitnessFunction fitnessFunction,
            KnapsackSolver knapsackSolver, PrintWriter pw) {
        super(problemInfo, params, fitnessFunction, knapsackSolver, pw);
    }

    @Override
    public void run(int runs, List<ResultPresenter> presenters) {
        IndividualFactory individualFactory = SimpleIndividualFactory
                .instance(getProblemInfo(), getKnapsackSolver(), getFitnessFunction());
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
