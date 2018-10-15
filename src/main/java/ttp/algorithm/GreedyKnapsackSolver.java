package ttp.algorithm;

import lombok.AllArgsConstructor;
import ttp.model.Item;
import ttp.model.ProblemInfo;

@AllArgsConstructor(staticName = "instance")
public class GreedyKnapsackSolver implements KnapsackSolver {

    private final ProblemInfo problemInfo;

    public Item[] solve(int[] nodes) {
        Item[] result = new Item[nodes.length];
        int current = 0;
        int maxCapacity = problemInfo.getProblem().getCapacityOfKnapsack();
        for (int i = 0; i < nodes.length - 1; i++) {
            Item item = problemInfo.getProblem().getItems().get(i);
            if (current + item.getWeight() < maxCapacity) {
                result[i] = item;
                current += item.getWeight();
            }
        }
        return result;
    }
}
