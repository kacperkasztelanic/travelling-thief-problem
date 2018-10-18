package ttp.algorithm;

import java.util.List;

import ttp.model.Individual;
import ttp.model.Population;
import ttp.model.wrapper.ProblemInfo;

public interface Algorithm {

    List<Population> solve(ProblemInfo problem);

    Individual solveForBest(ProblemInfo problem);
}
