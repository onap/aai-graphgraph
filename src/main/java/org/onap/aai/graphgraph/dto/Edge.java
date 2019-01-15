package org.onap.aai.graphgraph.dto;

import java.util.List;

public class Edge {
    private String source;
    private String target;
    private String type;
    private List<Property> tooltipProperties;

    public Edge(String source, String target, String type, List<Property> tooltipProperties) {
        this.source = source;
        this.target = target;
        this.type = type;
        this.tooltipProperties = tooltipProperties;
    }

    public List<Property> getTooltipProperties() {
        return tooltipProperties;
    }

    public void setTooltipProperties(List<Property> tooltipProperties) {
        this.tooltipProperties = tooltipProperties;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
