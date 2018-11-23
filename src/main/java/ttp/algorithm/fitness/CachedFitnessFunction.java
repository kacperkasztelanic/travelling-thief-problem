package ttp.algorithm.fitness;

import java.util.HashMap;
import java.util.Map;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import ttp.model.Individual;
import ttp.model.Result;

@EqualsAndHashCode
@ToString(of = { "fitnessFunction" })
public class CachedFitnessFunction implements FitnessFunction {

    private final FitnessFunction fitnessFunction;
    private final Map<Individual, Result> cache = new HashMap<>();

    public static CachedFitnessFunction instance(FitnessFunction fitnessFunction) {
        return new CachedFitnessFunction(fitnessFunction);
    }

    private CachedFitnessFunction(FitnessFunction fitnessFunction) {
        this.fitnessFunction = fitnessFunction;
    }

    @Override
    public Result calculate(Individual individual) {
        return cache.computeIfAbsent(individual, fitnessFunction::calculate);
    }
}
