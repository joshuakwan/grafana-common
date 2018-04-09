package grafana.beans;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GridPosition {
    @JsonProperty
    Integer h;

    @JsonProperty
    Integer w;

    @JsonProperty
    Integer x;

    @JsonProperty
    Integer y;

    @Override
    public String toString() {
        return "GridPosition{" +
                "h=" + h +
                ", w=" + w +
                ", x=" + x +
                ", y=" + y +
                '}';
    }

    public Integer getH() {
        return h;
    }

    public Integer getW() {
        return w;
    }

    public Integer getX() {
        return x;
    }

    public Integer getY() {
        return y;
    }
}
