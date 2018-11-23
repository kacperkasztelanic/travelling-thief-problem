package ttp.algorithm.fitness;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import ttp.model.Individual;
import ttp.model.Item;
import ttp.model.Node;
import ttp.model.Problem;
import ttp.model.Result;
import ttp.model.wrapper.ProblemInfo;

@AllArgsConstructor(staticName = "instance")
@EqualsAndHashCode
@ToString
public class TtpFitnessFunction implements FitnessFunction {

    private final ProblemInfo problemInfo;

    @Override
    public Result calculate(Individual individual) {
        Problem problem = problemInfo.getProblem();
        int profit = 0;
        int weight = 0;
        double totalTime = 0;

        for (int i = 0; i < individual.getNodes().length; i++) {
            Node node = problem.getNodes().get(individual.getNodes()[i] - 1);
            int nextNodeIndex = (i + 1) % individual.getNodes().length;
            Node nextNode = problem.getNodes().get(individual.getNodes()[nextNodeIndex] - 1);
            for (Item availableItem : problemInfo.itemsForNode(node.getId())) {
                Item item = individual.getItems()[availableItem.getId() - 1];
                if (item != null) {
                    profit += item.getProfit();
                    weight += item.getWeight();
                }
            }
            totalTime += problemInfo.distanceBetween(node, nextNode)
                    / (1 - weight * (problem.getMaxSpeed() - problem.getMinSpeed()) / problem.getCapacityOfKnapsack());
        }
        double value = profit - totalTime * problemInfo.getProblem().getRentingRatio();
        return Result.of(profit, weight, totalTime, value);
    }
}
