package ttp.model;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode(of = { "problem" })
@ToString(of = { "problem" }, includeFieldNames = false)
public class ProblemInfo {

    @Getter
    private final Problem problem;

    private final Map<Integer, List<Item>> itemsByNodes;
    private final double[][] distances;

    public static final ProblemInfo of(Problem problem) {
        return new ProblemInfo(problem);
    }

    private ProblemInfo(Problem problem) {
        this.problem = problem;
        this.itemsByNodes = problem.getItems().stream().collect(Collectors.groupingBy(Item::getAssignedNode));
        this.distances = calculateDistances(problem.getNodes());
    }

    public List<Item> itemsForNode(int nodeId) {
        return itemsByNodes.getOrDefault(nodeId, Collections.emptyList());
    }

    public double distanceBetween(Node a, Node b) {
        return distances[a.getId() - 1][b.getId() - 1];
    }

    private static double[][] calculateDistances(List<Node> nodes) {
        double[][] distances = new double[nodes.size()][nodes.size()];
        for (int row = 0; row < distances.length; row++) {
            for (int col = 0; col <= row; col++) {
                double distance = nodes.get(row).distanceTo(nodes.get(col));
                distances[row][col] = distance;
                distances[col][row] = distance;
            }
        }
        return distances;
    }
}
