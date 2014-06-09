package com.guardian.briefing.app.content;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class DisplayImage implements Serializable {


    public static final int APPROX_SINGLE_CARD_WIDTH = 900;

    public final String urlTemplate;
    public final int height;
    public final int width;
    public final String orientation;
    public final String caption;
    public final String credit;
    private String smallUrl;
    private String mediumUrl;

    @JsonCreator
    public DisplayImage(@JsonProperty("urlTemplate") String urlTemplate,
                        @JsonProperty("height") int height,
                        @JsonProperty("width") int width,
                        @JsonProperty("orientation") String orientation,
                        @JsonProperty("caption") String caption,
                        @JsonProperty("credit") String credit) {
        this.urlTemplate = urlTemplate;
        this.height = height;
        this.width = width;
        this.orientation = orientation;
        this.caption = caption;
        this.credit = credit;
    }

    @JsonIgnore
    public String getUrl(int width) {
        if (width > APPROX_SINGLE_CARD_WIDTH) {
            //LogHelper.verbose("Using medium url for " + getMediumUrl());
            return getMediumUrl();
        }
        //LogHelper.verbose("Using small url for " + getSmallUrl());
        return getSmallUrl();
    }

    @JsonIgnore
    public String getSmallUrl() {
        if(this.smallUrl == null)
            smallUrl = new ImageUrlTemplate(urlTemplate).getSmallUrl();
        return smallUrl;
    }

    @JsonIgnore
    public String getMediumUrl() {
        if(this.mediumUrl == null)
            mediumUrl = new ImageUrlTemplate(urlTemplate).getMediumUrl();
        return mediumUrl;
    }

    @JsonIgnore
    public String getLargeUrl() {
        return new ImageUrlTemplate(urlTemplate).getLargeUrl();
    }
}
