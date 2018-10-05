package ttp.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor(staticName = "of")
@EqualsAndHashCode
@ToString(includeFieldNames = false)
public class Item {

    @Getter
    private final int id;
    @Getter
    private final int profit;
    @Getter
    private final int weight;
    @Getter
    private final int assignedNode;
}
