package ttp.algotithm;

import java.util.Optional;

import ttp.model.Individual;
import ttp.model.Item;
import ttp.model.Node;
import ttp.model.Problem;
import ttp.model.ProblemInfo;
import ttp.model.Result;

public class FittnessFunction {

    public Result calculate(ProblemInfo problemInfo, Individual individual) {
        Problem problem = problemInfo.getProblem();
        int profit = 0;
        int weight = 0;
        double totalTime = 0;

        for (int i = 0; i < individual.getNodes().length; i++) {
            Node node = problem.getNodes().get(i);
            Node nextNode = problem.getNodes().get(i);
            Optional<Item> selectedItemOptional = problemInfo.itemsForNode(node.getId()).stream()
                    .filter(e -> individual.getItems()[e.getId()]).findAny();
            if (selectedItemOptional.isPresent()) {
                Item selectedItem = selectedItemOptional.get();
                profit += selectedItem.getProfit();
                weight += selectedItem.getWeight();
            }
            totalTime += problemInfo.distanceBetween(node, nextNode)
                    / (1 - weight * (problem.getMaxSpeed() - problem.getMinSpeed()) / problem.getCapacityOfKnapsack());
        }
        double value = profit - totalTime * problemInfo.getProblem().getRentingRatio();
        return Result.of(profit, weight, totalTime, value);
    }
}
