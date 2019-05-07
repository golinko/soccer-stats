package com.golinko.soccerstats.model;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum TeamType {
    HOME("Home"),
    AWAY("Away");

    private final String type;

    @JsonCreator
    TeamType(String type) {
        this.type = type;
    }
}
