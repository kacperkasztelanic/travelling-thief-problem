package ttp.algorithm.greedy;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import ttp.model.Item;
import ttp.model.Node;
import ttp.model.Problem;
import ttp.model.wrapper.ProblemInfo;

@AllArgsConstructor(staticName = "instance")
@EqualsAndHashCode
@ToString
public class AdvancedGreedyKnapsackSolver implements KnapsackSolver {

    private final ProblemInfo problemInfo;

    @Override
    public Item[] solve(int[] nodes) {
        RouteDistanceInfo routeDistanceInfo = calculateRouteDistance(nodes);
        ItemWithValue[] itemsWithValue = sortItemsByRelativeValue(routeDistanceInfo);
        return fillKnapsack(itemsWithValue);
    }

    private RouteDistanceInfo calculateRouteDistance(int[] nodeIds) {
        List<Node> nodes = problemInfo.getProblem().getNodes();
        NodeWithDistanceFromStart[] nodesWithDistanceFromStart = new NodeWithDistanceFromStart[nodeIds.length];
        double entireDistance = 0;
        for (int i = 0; i < nodeIds.length; i++) {
            Node node = nodes.get(nodeIds[i] - 1);
            int nextNodeIndex = (i + 1) % nodeIds.length;
            Node nextNode = nodes.get(nodeIds[nextNodeIndex] - 1);
            nodesWithDistanceFromStart[i] = NodeWithDistanceFromStart.of(node, entireDistance);
            entireDistance += problemInfo.distanceBetween(node, nextNode);
        }
        return RouteDistanceInfo.of(entireDistance, nodesWithDistanceFromStart);
    }

    private ItemWithValue[] sortItemsByRelativeValue(RouteDistanceInfo routeDistanceInfo) {
        double routeDistance = routeDistanceInfo.getEntireDistance();
        NodeWithDistanceFromStart[] nodeWithDistanceFromStart = routeDistanceInfo.getNodesWithDistanceFromStart();
        Problem problem = problemInfo.getProblem();
        ItemWithValue[] result = new ItemWithValue[problem.getNumberOfItems()];
        double rentingRatio = problem.getRentingRatio();
        double maxSpeed = problem.getMaxSpeed();
        double minSpeed = problem.getMinSpeed();
        double capacity = problem.getCapacityOfKnapsack();

        for (int i = 0; i < problem.getItems().size(); i++) {
            Item item = problem.getItems().get(i);
            double distanceToFinish = routeDistance
                    - nodeWithDistanceFromStart[item.getAssignedNode() - 1].getDistanceFromStart();
            double dVx = ((minSpeed - maxSpeed) / capacity) * ((double) item.getWeight());
            double itemValue = (double) item.getProfit()
                    - rentingRatio * ((distanceToFinish / (maxSpeed + dVx)) - (distanceToFinish / maxSpeed));
            result[i] = ItemWithValue.of(item, itemValue);
        }
        Arrays.sort(result, Comparator.comparingDouble(ItemWithValue::getValue).reversed());
        return result;
    }

    private Item[] fillKnapsack(ItemWithValue[] itemsWithValue) {
        Item[] result = new Item[problemInfo.getProblem().getItems().size()];
        int capacity = problemInfo.getProblem().getCapacityOfKnapsack();
        int currentWeight = 0;

        for (int i = 0; currentWeight < capacity && i < itemsWithValue.length; i++) {
            Item item = itemsWithValue[i].getItem();
            if (currentWeight + item.getWeight() < capacity && itemsWithValue[i].getValue() > 0) {
                result[item.getId() - 1] = item;
                currentWeight += item.getWeight();
            }
        }
        return result;
    }

    @AllArgsConstructor(staticName = "of")
    @EqualsAndHashCode(of = { "nodesWithDistanceFromStart" })
    @ToString()
    private static class RouteDistanceInfo {

        @Getter
        private final double entireDistance;
        @Getter
        private final NodeWithDistanceFromStart[] nodesWithDistanceFromStart;
    }

    @AllArgsConstructor(staticName = "of")
    @EqualsAndHashCode(of = { "node" })
    @ToString()
    private static class NodeWithDistanceFromStart {

        @Getter
        private final Node node;
        @Getter
        private final double distanceFromStart;
    }

    @AllArgsConstructor(staticName = "of")
    @EqualsAndHashCode(of = { "item" })
    @ToString()
    private static class ItemWithValue {

        @Getter
        private final Item item;
        @Getter
        private final double value;
    }
}
