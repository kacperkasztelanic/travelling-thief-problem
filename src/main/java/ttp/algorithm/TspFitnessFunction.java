package ttp.algorithm;

import lombok.AllArgsConstructor;
import ttp.model.Individual;
import ttp.model.Node;
import ttp.model.Problem;
import ttp.model.Result;
import ttp.model.wrapper.ProblemInfo;

@AllArgsConstructor(staticName = "instance")
public class TspFitnessFunction implements FitnessFunction {

    @Override
    public Result calculate(ProblemInfo problemInfo, Individual individual) {
        Problem problem = problemInfo.getProblem();
        int profit = 0;
        int weight = 0;
        double totalTime = 0;

        for (int i = 0; i < individual.getNodes().length; i++) {
            Node node = problem.getNodes().get(individual.getNodes()[i] - 1);
            int nextNodeIndex = (i + 1) % individual.getNodes().length;
            Node nextNode = problem.getNodes().get(individual.getNodes()[nextNodeIndex] - 1);
            totalTime += problemInfo.distanceBetween(node, nextNode);
        }
        double value = totalTime;
        return Result.of(profit, weight, totalTime, value);
    }
}