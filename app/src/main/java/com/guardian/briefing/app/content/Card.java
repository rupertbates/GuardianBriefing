package com.guardian.briefing.app.content;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class Card implements Serializable {

    public final Item item;
    @JsonCreator
    public Card(@JsonProperty("item") Item item){
        this.item = item;
    }
}
