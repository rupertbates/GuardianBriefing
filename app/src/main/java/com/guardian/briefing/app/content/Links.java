package com.guardian.briefing.app.content;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class Links implements Serializable {
    public final String uri;
    public final String shortUrl;
    public final String relatedUri;

    @JsonCreator
    public Links(@JsonProperty("uri") String uri,
                 @JsonProperty("shortUrl") String shortUrl,
                 @JsonProperty("relatedUri") String relatedUri) {
        this.uri = uri;
        this.shortUrl = shortUrl;
        this.relatedUri = relatedUri;
    }
}
