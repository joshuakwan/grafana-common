package grafana;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.ResolvableDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import grafana.beans.dashboard.Panel;

import java.io.IOException;

public class GrafanaDeserializer extends StdDeserializer<Panel> implements ResolvableDeserializer {
    private final JsonDeserializer<?> defaultDeserializer;

    public GrafanaDeserializer() {
        this(null);
    }

    public GrafanaDeserializer(JsonDeserializer<?> defaultDeserializer) {
        super(Panel.class);
        this.defaultDeserializer = defaultDeserializer;
    }

//    private static <T> T getValue(JsonNode node, String key, T defaultValue) {
//        if (node.has(key)) {
//            if
//        } else
//            return defaultValue;
//    }

    private static String getStringValue(JsonNode node, String key, String defaultValue) {
        if (node.has(key)) {
            return node.get(key).toString();
        } else
            return defaultValue;
    }

    private static Integer getIntValue(JsonNode node, String key, Integer defaultValue) {
        if (node.has(key)) {
            return node.get(key).intValue();
        } else
            return defaultValue;
    }

    private static Boolean getBooleanValue(JsonNode node, String key, Boolean defaultValue) {
        if (node.has(key)) {
            return node.get(key).booleanValue();
        } else
            return defaultValue;
    }

    @Override
    public Panel deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
//        JsonNode node = p.getCodec().readTree(p);
//        if (node.has("thresholds")) {
//            if (!node.get("thresholds").isArray()) {
//                System.out.println("Fxxked");
//            }
//        }
        Panel panel = (Panel) defaultDeserializer.deserialize(p, ctxt);
        return panel;
    }

//    @Override
//    public Panel deserialize(JsonParser p, DeserializationContext ctxt)
//            throws IOException, JsonProcessingException {
//        JsonNode node = p.getCodec().readTree(p);
//        System.out.println(node);
//
//        Panel panel = new Panel();
//        panel.title(node.get("title").toString());
//        panel.datasource(node.get("datasource").toString());
//
//        panel.type(getStringValue(node, "type", "graph"));
//
//        if(node.has("format")){
//            panel.format(node.get("format").toString());
//        }
//
//        panel.fill(getIntValue(node, "fill", 1));
//        panel.linewidth(getIntValue(node, "linewidth", 1));
//        panel.lines(getBooleanValue(node,"lines", true));
//
//        if (node.has("stack")) {
//            panel.stack(node.get("stack").booleanValue());
//        }
//        if (node.has("decimals")) {
//            panel.decimals(node.get("decimals").intValue());
//        }
//        if (node.has("valueName")) {
//            panel.valueName(node.get("valueName").toString());
//        }
//        if (node.has("nullPointMode")) {
//            panel.nullPointMode(node.get("nullPointMode").toString());
//        }
//        if (node.has("repeat")) {
//            panel.repeat(node.get("repeat").toString());
//        }
//        if (node.has("repeatDirection")) {
//            panel.repeatDirection(node.get("repeatDirection").toString());
//        }
//        if (node.has("minSpan")) {
//            panel.minSpan(node.get("minSpan").intValue());
//        }
//
//        Panel panel = (Panel)
//
//
//
//        return panel;
//    }

    @Override
    public void resolve(DeserializationContext ctxt) throws JsonMappingException {
        ((ResolvableDeserializer) defaultDeserializer).resolve(ctxt);
    }
}
