package ttp.model.factory;

import ttp.model.Individual;

public interface IndividualFactory {

    Individual newIndividual(int[] nodes);
}