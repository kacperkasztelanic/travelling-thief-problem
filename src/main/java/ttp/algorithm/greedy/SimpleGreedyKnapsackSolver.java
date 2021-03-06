package ttp.algorithm.greedy;

import java.util.Comparator;
import java.util.function.ToDoubleFunction;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import ttp.model.Item;
import ttp.model.wrapper.ProblemInfo;

@EqualsAndHashCode
@ToString(of = { "problemInfo" })
public class SimpleGreedyKnapsackSolver implements KnapsackSolver {

    private final ProblemInfo problemInfo;
    private final Item[] itemsByValueToWeightRatio;
    private final Item[] knapsack;

    public static SimpleGreedyKnapsackSolver instance(ProblemInfo problemInfo) {
        return new SimpleGreedyKnapsackSolver(problemInfo);
    }

    private SimpleGreedyKnapsackSolver(ProblemInfo problemInfo) {
        this.problemInfo = problemInfo;
        this.itemsByValueToWeightRatio = itemsByValueToWeightRatio();
        this.knapsack = fillKnapsack();
    }

    @Override
    public Item[] solve(int[] nodes) {
        return knapsack;
    }

    private Item[] itemsByValueToWeightRatio() {
        ToDoubleFunction<Item> profitToWeightRatio = i -> ((double) i.getProfit()) / i.getWeight();
        return problemInfo.getProblem().getItems().stream()//
                .sorted(Comparator.comparingDouble(profitToWeightRatio).reversed())//
                .toArray(Item[]::new);
    }

    private Item[] fillKnapsack() {
        Item[] result = new Item[problemInfo.getProblem().getItems().size()];
        int capacity = problemInfo.getProblem().getCapacityOfKnapsack();
        int currentWeight = 0;

        for (int i = 0; currentWeight < capacity && i < itemsByValueToWeightRatio.length; i++) {
            Item item = itemsByValueToWeightRatio[i];
            if (currentWeight + item.getWeight() < capacity) {
                result[item.getId() - 1] = item;
                currentWeight += item.getWeight();
            }
        }
        return result;
    }
}
