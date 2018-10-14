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
    private final Item[] items;

    public Individual mutate(double probability) {
        int[] newNodes = new int[nodes.length];
        System.arraycopy(nodes, 0, newNodes, 0, nodes.length);
        for (int i = 0; i < nodes.length - 1; i++) {
            if (random.nextDouble() <= probability) {
                int j = random.nextInt(newNodes.length - 1);
                int temp = newNodes[i];
                newNodes[i] = newNodes[j];
                newNodes[j] = temp;
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
        int[] counts = new int[newNodes.length];
        for (int i = 0; i < newNodes.length; i++) {
            counts[newNodes[i] - 1]++;
        }
        System.out.println("Counts: " + Arrays.toString(counts));
        int[] duplicated = new int[counts.length / 2];
        int[] absent = new int[counts.length / 2];
        for (int i = 0, d = 0, a = 0; i < counts.length; i++) {
            if (counts[i] == 2) {
                duplicated[d++] = i + 1;
            } else if (counts[i] == 0) {
                absent[a++] = i + 1;
            }
        }
        System.out.println("Duplicates: " + Arrays.toString(duplicated));
        System.out.println("Absent: " + Arrays.toString(absent));
        for (int i = 0, d = 0; i < newNodes.length && d < duplicated.length && duplicated[d] != 0; i++) {
            if (newNodes[i] == duplicated[d]) {
                newNodes[i] = absent[d];
                d++;
            }
        }
        return newNodes;
    }

    public static void main(String[] args) {
        Individual i = Individual.of(null, null);
        int[] a = { 4, 2, 2, 1 };
        System.out.println(Arrays.toString(a));
        i.repair(a);
        System.out.println(Arrays.toString(a));
    }
}
