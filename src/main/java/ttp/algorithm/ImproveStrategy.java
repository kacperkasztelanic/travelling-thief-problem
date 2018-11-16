package ttp.algorithm;

import ttp.model.Individual;

public interface ImproveStrategy {

    Individual tryToImprove(Individual individual);
}
