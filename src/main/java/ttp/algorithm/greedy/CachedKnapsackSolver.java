package ttp.algorithm.greedy;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import ttp.model.Item;

@EqualsAndHashCode(of = { "knapsackSolver" })
@ToString(of = { "knapsackSolver" })
public class CachedKnapsackSolver implements KnapsackSolver {

    private final KnapsackSolver knapsackSolver;
    private final Map<IntArrayWrapper, Item[]> cache = new HashMap<>();

    public static CachedKnapsackSolver instance(KnapsackSolver knapsackSolver) {
        return new CachedKnapsackSolver(knapsackSolver);
    }

    private CachedKnapsackSolver(KnapsackSolver knapsackSolver) {
        this.knapsackSolver = knapsackSolver;
    }

    @Override
    public Item[] solve(int[] nodes) {
        return cache.computeIfAbsent(IntArrayWrapper.of(nodes), k -> knapsackSolver.solve(k.getArray()));
    }

    @AllArgsConstructor(staticName = "of")
    @ToString(includeFieldNames = false)
    private static class IntArrayWrapper {

        @Getter
        private final int[] array;

        @Override
        public int hashCode() {
            return Arrays.hashCode(array);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            IntArrayWrapper other = (IntArrayWrapper) obj;
            if (hashCode() != other.hashCode()) {
                return false;
            }
            return Arrays.equals(array, other.array);
        }
    }
}
