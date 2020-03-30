package ttp.app.runner;

import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ttp.algorithm.Algorithm;
import ttp.algorithm.TabuSearch;
import ttp.algorithm.fitness.FitnessFunction;
import ttp.algorithm.greedy.KnapsackSolver;
import ttp.model.Individual;
import ttp.model.Statistics;
import ttp.model.factory.IndividualFactory;
import ttp.model.factory.SimpleIndividualFactory;
import ttp.model.params.TabuSearchParams;
import ttp.model.wrapper.ProblemInfo;
import ttp.presenter.ResultPresenter;
import ttp.utils.StatisticsUtils;

public class TabuSearchRunner extends AbstractRunner<TabuSearchParams> {

    public TabuSearchRunner(ProblemInfo problemInfo, TabuSearchParams params, FitnessFunction fitnessFunction,
            KnapsackSolver knapsackSolver, PrintWriter pw) {
        super(problemInfo, params, fitnessFunction, knapsackSolver, pw);
    }

    @Override
    public void run(int runs, List<ResultPresenter> presenters) {
        IndividualFactory individualFactory = SimpleIndividualFactory
                .instance(getProblemInfo(), getKnapsackSolver(), getFitnessFunction());
        Algorithm<Individual> algorithm = TabuSearch.instance(getParams(), individualFactory);
        List<List<Individual>> solution = Stream.generate(() -> algorithm.solve(getProblemInfo()))//
                .limit(runs)//
                .collect(Collectors.toList());
        List<Statistics> statistics = StatisticsUtils.analyzeMultipleIndividualListsEach(solution);
        Statistics res = StatisticsUtils.analyzeMultipleIndividualLists(solution);
        getPw().println("TabuSearch statistics:");
        presenters.forEach(p -> p.present(statistics));
        getPw().println(res);
    }
}
