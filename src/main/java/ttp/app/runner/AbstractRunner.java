package ttp.app.runner;

import java.io.PrintWriter;
import java.util.List;

import lombok.Getter;
import ttp.algorithm.fitness.FitnessFunction;
import ttp.algorithm.greedy.KnapsackSolver;
import ttp.model.wrapper.ProblemInfo;
import ttp.presenter.ResultPresenter;

public abstract class AbstractRunner<T> {

    @Getter
    private final ProblemInfo problemInfo;
    @Getter
    private final T params;
    @Getter
    private final FitnessFunction fitnessFunction;
    @Getter
    private final KnapsackSolver knapsackSolver;
    @Getter
    private final PrintWriter pw;

    public AbstractRunner(ProblemInfo problemInfo, T params, FitnessFunction fitnessFunction,
            KnapsackSolver knapsackSolver, PrintWriter pw) {
        this.problemInfo = problemInfo;
        this.params = params;
        this.fitnessFunction = fitnessFunction;
        this.knapsackSolver = knapsackSolver;
        this.pw = pw;
    }

    public abstract void run(int runs, List<ResultPresenter> presenters);
}
