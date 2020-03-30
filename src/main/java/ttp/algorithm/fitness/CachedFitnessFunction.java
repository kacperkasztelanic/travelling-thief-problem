package ttp.algorithm.fitness;

import java.util.HashMap;
import java.util.Map;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import ttp.model.Individual;
import ttp.model.Result;

@RequiredArgsConstructor(staticName = "instance")
@EqualsAndHashCode
@ToString(of = { "fitnessFunction" })
public class CachedFitnessFunction implements FitnessFunction {

    private final FitnessFunction fitnessFunction;
    private final Map<Individual, Result> cache = new HashMap<>();

    @Override
    public Result calculate(Individual individual) {
        return cache.computeIfAbsent(individual, fitnessFunction::calculate);
    }
}
