package ttp.algorithm;

import ttp.model.Individual;
import ttp.model.Result;
import ttp.model.wrapper.ProblemInfo;

public interface FitnessFunction {

    Result calculate(ProblemInfo problemInfo, Individual individual);
}