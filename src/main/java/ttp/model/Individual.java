package ttp.model;

import java.util.Arrays;
import java.util.Random;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(staticName = "of")
public class Individual {

    private static final int DIVISION_POINT_RATIO = 2;
    private static final Random random = new Random();

    @Getter
    private final int[] nodes;
    @Getter
    private final boolean[] items;

    public Individual mutate(double probability) {
        int[] newNodes = new int[nodes.length];
        System.arraycopy(nodes, 0, newNodes, 0, nodes.length);
        for (int i = 0; i < nodes.length - 1; i++) {
            if (random.nextDouble() <= probability) {
                int j = random.nextInt(nodes.length - 1);
                int temp = newNodes[i];
                newNodes[i] = newNodes[j];
                newNodes[j] = temp;
                if (i == 0) {
                    newNodes[newNodes.length - 1] = newNodes[0];
                }
            }
        }
        return Individual.of(newNodes, Arrays.copyOf(items, items.length));
    }

    public Individual crossover(Individual other) {
        int divisionPoint = nodes.length / DIVISION_POINT_RATIO;
        int[] newNodes = new int[nodes.length];
        System.arraycopy(nodes, 0, newNodes, 0, divisionPoint);
        System.arraycopy(other.nodes, divisionPoint, newNodes, divisionPoint, nodes.length - divisionPoint);
        return Individual.of(repair(newNodes), Arrays.copyOf(items, items.length));
    }

    private int[] repair(int[] newNodes) {
        // TODO implement repair method
        return newNodes;
    }
}
