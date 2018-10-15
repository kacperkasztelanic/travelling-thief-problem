package ttp.algorithm;

import lombok.AllArgsConstructor;
import ttp.model.Individual;
import ttp.model.Item;
import ttp.model.Node;
import ttp.model.Problem;
import ttp.model.ProblemInfo;
import ttp.model.Result;

@AllArgsConstructor(staticName = "instance")
public class FittnessFunction {

    public Result calculate(ProblemInfo problemInfo, Individual individual) {
        Problem problem = problemInfo.getProblem();
        int profit = 0;
        int weight = 0;
        double totalTime = 0;

        for (int i = 0; i < individual.getNodes().length; i++) {
            Node node = problem.getNodes().get(i);
            int nextNodeIndex = (i + 1) % individual.getNodes().length;
            Node nextNode = problem.getNodes().get(nextNodeIndex);
            Item item = individual.getItems()[i];
            if (item != null) {
                profit += item.getProfit();
                weight += item.getWeight();
            }
            totalTime += problemInfo.distanceBetween(node, nextNode)
                    / (1 - weight * (problem.getMaxSpeed() - problem.getMinSpeed()) / problem.getCapacityOfKnapsack());
        }
        double value = profit - totalTime * problemInfo.getProblem().getRentingRatio();
        return Result.of(profit, weight, totalTime, value);
    }
}
