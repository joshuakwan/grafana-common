package grafana.beans;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Time {
    @Override
    public String toString() {
        return "Time{" +
                "from='" + from + '\'' +
                ", to='" + to + '\'' +
                '}';
    }

    @JsonProperty
    String from;

    @JsonProperty
    String to;

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }
}
