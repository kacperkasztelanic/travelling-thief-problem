package ttp.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor(staticName = "of")
@EqualsAndHashCode
public class Item {

    @Getter
    private final int id;
    @Getter
    private final int profit;
    @Getter
    private final int weight;
    @Getter
    private final int assignedNode;

    @Override
    public String toString() {
        return "Item(id: " + id + ", profit: " + profit + ", weight: " + weight + ", assignedNode: " + assignedNode
                + ")";
    }
}
