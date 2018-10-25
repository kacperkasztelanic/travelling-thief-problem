package ttp.algorithm;

import java.util.List;

import ttp.model.Individual;
import ttp.model.wrapper.ProblemInfo;

public interface Algorithm<T> {

    List<T> solve(ProblemInfo problem);

    Individual solveForBest(ProblemInfo problem);
}
