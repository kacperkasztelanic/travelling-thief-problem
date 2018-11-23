package ttp.algorithm.fitness;

import ttp.model.Individual;
import ttp.model.Result;

public interface FitnessFunction {

    Result calculate(Individual individual);
}