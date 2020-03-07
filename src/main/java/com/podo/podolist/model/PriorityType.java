package com.podo.podolist.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.stream.Stream;

public enum PriorityType {
    @JsonProperty("urgent")
    URGENT(1),
    @JsonProperty("high")
    HIGH(2),
    @JsonProperty("medium")
    MEDIUM(3),
    @JsonProperty("low")
    LOW(4),
    @JsonProperty("none")
    NONE(5);

    public final int id;

    PriorityType(int id) {
        this.id = id;
    }

    public static PriorityType valueOf(int id) {
        return Stream.of(values())
                .filter(o -> o.id == id)
                .findFirst()
                .orElse(null);
    }
}
