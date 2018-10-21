package ttp.algorithm;

import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import ttp.model.Item;

public class CachedKnapsachSolver implements KnapsackSolver {

    private final KnapsackSolver knapsackSolver;
    private final Map<IntArrayWrapper, Item[]> cache = new HashMap<>();

    public static CachedKnapsachSolver instance(KnapsackSolver knapsackSolver) {
        return new CachedKnapsachSolver(knapsackSolver);
    }

    private CachedKnapsachSolver(KnapsackSolver knapsackSolver) {
        this.knapsackSolver = knapsackSolver;
    }

    @Override
    public Item[] solve(int[] nodes) {
        return cache.computeIfAbsent(IntArrayWrapper.of(nodes), k -> knapsackSolver.solve(k.getArray()));
    }

    @AllArgsConstructor(staticName = "of")
    @EqualsAndHashCode
    @ToString(includeFieldNames = false)
    private static class IntArrayWrapper {
        
        @Getter
        private final int[] array;
    }
}
