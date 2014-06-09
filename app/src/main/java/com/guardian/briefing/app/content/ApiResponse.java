package com.guardian.briefing.app.content;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class ApiResponse implements Serializable {
    public final Card[] cards;

    @JsonCreator
    public ApiResponse(@JsonProperty("cards") Card[] cards) {
        this.cards = cards;
    }
}
