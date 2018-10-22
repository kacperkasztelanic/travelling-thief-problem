package ttp.algorithm.greedy;

import java.util.Comparator;
import java.util.function.Function;

import ttp.model.Item;
import ttp.model.wrapper.ProblemInfo;

public class SimpleGreedyKnapsackSolver implements KnapsackSolver {

    private final ProblemInfo problemInfo;
    private final Item[] itemsByValueToWeightRatio;

    public static SimpleGreedyKnapsackSolver instance(ProblemInfo problemInfo) {
        return new SimpleGreedyKnapsackSolver(problemInfo);
    }

    private SimpleGreedyKnapsackSolver(ProblemInfo problemInfo) {
        this.problemInfo = problemInfo;
        this.itemsByValueToWeightRatio = itemsByValueToWeightRatio();
    }

    @Override
    public Item[] solve(int[] nodes) {
        return fillKnapsack();
    }

    private Item[] itemsByValueToWeightRatio() {
        Function<Item, Double> profitToWeightRatio = i -> ((double) i.getProfit()) / i.getWeight();
        return problemInfo.getProblem().getItems().stream()
                .sorted(Comparator.comparingDouble(profitToWeightRatio::apply).reversed()).toArray(Item[]::new);
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
