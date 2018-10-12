package ttp.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor(staticName = "of")
@EqualsAndHashCode
@ToString(includeFieldNames = false)
public class Node {

    @Getter
    private final int id;
    @Getter
    private final double x;
    @Getter
    private final double y;

    public double distanceTo(Node other) {
        return Math.hypot(x - other.getX(), y - other.getY());
    }
}
