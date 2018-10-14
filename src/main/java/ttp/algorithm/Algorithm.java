package ttp.algorithm;

import ttp.model.Individual;
import ttp.model.Problem;

public interface Algorithm {

    Individual solve(Problem problem, FittnessFunction function);
}
