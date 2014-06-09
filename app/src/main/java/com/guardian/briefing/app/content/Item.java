package com.guardian.briefing.app.content;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class Item implements Serializable {
    public final String title;
    public final Links links;
    public final DisplayImage[] displayImages;

    @JsonCreator
    public Item(@JsonProperty("title") String title,
                @JsonProperty("links") Links links,
                @JsonProperty("displayImages")  DisplayImage[] displayImages) {
        this.title = title;
        this.links = links;
        this.displayImages = displayImages;
    }

    @JsonIgnore
    public DisplayImage getMainImage() {
        //TODO: Is this logic correct?
        if (hasMainImage())
            return displayImages[0];
        return null;
    }

    @JsonIgnore
    public boolean hasMainImage() {
        return displayImages != null && displayImages.length > 0;
    }

}
