package ttp.loader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import lombok.Getter;
import ttp.model.Item;
import ttp.model.Node;
import ttp.model.Problem;
import ttp.model.Problem.ProblemBuilder;

public class Loader {

    private static final String DEFAULT_NUMERIC = "-1";
    private static final String BASIC_DATA_DELIMETER = ":";
    private static final String ALL_WHITESPACES_REGEX = "\\s+";

    public Problem load(String resource) throws IOException {
        List<String> lines = readLines(resource);
        Map<RawDataType, List<String>> linesByRawDataType = groupLines(lines);
        return parse(linesByRawDataType);
    }

    private Problem parse(Map<RawDataType, List<String>> linesByRawDataType) {
        Map<BasicDataType, String> basicData = parseBasicData(
                linesByRawDataType.getOrDefault(RawDataType.BASIC, Collections.emptyList()));
        List<Node> nodes = linesByRawDataType.getOrDefault(RawDataType.NODE_COORDS, Collections.emptyList()).stream()
                .map(this::parseNode).collect(Collectors.toList());
        List<Item> items = linesByRawDataType.getOrDefault(RawDataType.ITEMS, Collections.emptyList()).stream()
                .map(this::parseItem).collect(Collectors.toList());

        ProblemBuilder builder = Problem.builder();
        builder.problemName(basicData.getOrDefault(BasicDataType.PROBLEM_NAME, ""));
        builder.knapsackDataType(basicData.getOrDefault(BasicDataType.KNAPSACK_DATA_TYPE, ""));
        builder.dimension(Integer.parseInt(basicData.getOrDefault(BasicDataType.DIMENSION, DEFAULT_NUMERIC)));
        builder.numberOfItems(Integer.parseInt(basicData.getOrDefault(BasicDataType.NUMBER_OF_ITEMS, DEFAULT_NUMERIC)));
        builder.capacityOfKnapsack(
                Integer.parseInt(basicData.getOrDefault(BasicDataType.CAPACITY_OF_KNAPSACK, DEFAULT_NUMERIC)));
        builder.minSpeed(Double.parseDouble(basicData.getOrDefault(BasicDataType.MIN_SPEED, DEFAULT_NUMERIC)));
        builder.maxSpeed(Double.parseDouble(basicData.getOrDefault(BasicDataType.MAX_SPEED, DEFAULT_NUMERIC)));
        builder.rentingRatio(Double.parseDouble(basicData.getOrDefault(BasicDataType.RENTING_RATIO, DEFAULT_NUMERIC)));
        builder.edgeWeightType(basicData.getOrDefault(BasicDataType.EDGE_WEIGHT_TYPE, ""));
        builder.nodes(nodes);
        builder.items(items);
        return builder.build();
    }

    private Map<BasicDataType, String> parseBasicData(List<String> lines) {
        return lines.stream().map(l -> l.split(BASIC_DATA_DELIMETER))
                .collect(Collectors.toMap(a -> BasicDataType.forMarker(a[0]), a -> a[1].trim(), (p, r) -> p,
                        () -> new EnumMap<>(BasicDataType.class)));
    }

    private Node parseNode(String line) {
        String[] tokens = line.split(ALL_WHITESPACES_REGEX);
        int id = Integer.parseInt(tokens[0]);
        double x = Double.parseDouble(tokens[1]);
        double y = Double.parseDouble(tokens[2]);
        return Node.of(id, x, y);
    }

    private Item parseItem(String line) {
        String[] tokens = line.split(ALL_WHITESPACES_REGEX);
        int id = Integer.parseInt(tokens[0]);
        int profit = Integer.parseInt(tokens[1]);
        int weight = Integer.parseInt(tokens[2]);
        int assignedNode = Integer.parseInt(tokens[3]);
        return Item.of(id, profit, weight, assignedNode);
    }

    private Map<RawDataType, List<String>> groupLines(List<String> lines) {
        Map<RawDataType, List<String>> result = Arrays.stream(RawDataType.values()).collect(Collectors.toMap(
                Function.identity(), t -> new ArrayList<>(), (p, r) -> p, () -> new EnumMap<>(RawDataType.class)));
        RawDataType currentType = null;
        for (String line : lines) {
            RawDataType temp = RawDataType.forMarker(line);
            if (temp != null) {
                currentType = temp;
                if (!currentType.isInlineValue()) {
                    continue;
                }
            }
            if (currentType == null) {
                continue;
            }
            result.get(currentType).add(line);
        }
        return result;
    }

    private List<String> readLines(String resource) throws IOException {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(getClass().getClassLoader().getResourceAsStream(resource)))) {
            return br.lines().collect(Collectors.toList());
        }
    }

    private static enum RawDataType {

        BASIC("PROBLEM NAME", true), NODE_COORDS("NODE_COORD_SECTION", false), ITEMS("ITEMS SECTION", false);

        @Getter
        private final String marker;

        @Getter
        private final boolean inlineValue;

        private RawDataType(String marker, boolean inlineValue) {
            this.marker = marker;
            this.inlineValue = inlineValue;
        }

        public static RawDataType forMarker(String line) {
            return Arrays.stream(RawDataType.values()).filter(t -> line.trim().contains(t.getMarker())).findFirst()
                    .orElse(null);
        }
    }

    private static enum BasicDataType {

        PROBLEM_NAME("PROBLEM NAME"), 
        KNAPSACK_DATA_TYPE("KNAPSACK DATA TYPE"), 
        DIMENSION("DIMENSION"), 
        NUMBER_OF_ITEMS("NUMBER OF ITEMS"), 
        CAPACITY_OF_KNAPSACK("CAPACITY OF KNAPSACK"), 
        MIN_SPEED("MIN SPEED"), 
        MAX_SPEED("MAX SPEED"), 
        RENTING_RATIO("RENTING RATIO"), 
        EDGE_WEIGHT_TYPE("EDGE_WEIGHT_TYPE");

        @Getter
        private final String marker;

        private BasicDataType(String marker) {
            this.marker = marker;
        }

        public static BasicDataType forMarker(String line) {
            return Arrays.stream(BasicDataType.values()).filter(t -> line.trim().contains(t.getMarker())).findFirst()
                    .orElse(null);
        }
    }
}
