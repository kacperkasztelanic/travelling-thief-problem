package ttp.algorithm;

import lombok.AllArgsConstructor;
import ttp.model.Item;
import ttp.model.wrapper.ProblemInfo;

@AllArgsConstructor(staticName = "instance")
public class SimpleGreedyKnapsackSolver implements KnapsackSolver {

    private final ProblemInfo problemInfo;

    @Override
    public Item[] solve(int[] nodes) {
        return problemInfo.getProblem().getItems().stream().limit(nodes.length).toArray(Item[]::new);
    }
}
