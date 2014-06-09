package com.guardian.briefing.app.content;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * MAPI does not return image urls to the apps but url templates, from which the app can construct an appropriate url
 * for an appropriate sized image in a given context.
 * <p/>
 * This class represents one of those templates.
 */
public class ImageUrlTemplate {
    //The full list of possible widths - may be useful if we try to be a bit cleverer with sizing
    //private static final int[] widths = new int[]{300, 450, 600, 750, 900, 540, 630, 720, 900, 1200};

    private static final int ICON_SMALL_WIDTH = 60;
    private static final int ICON_MEDIUM_WIDTH = 80;

    private static final int DESIRED_QUALITY = 95;
    public static final int SMALL_WIDTH = 450;
    private static final int MEDIUM_WIDTH = 750;
    private static final int LARGE_WIDTH = 900;
    private static final String WIDTH_PARAMETER = "#{width}";
    private static final String HEIGHT_PARAMETER = "#{height}";
    private static final String QUALITY_PARAMETER = "#{quality}";
    public static final String HYPHEN = "-";
    private static Map<String, String> tokens = new HashMap<String, String>();
    private static final Pattern pattern = Pattern.compile("(width|quality)=\\d+");

    static {
        tokens.put("width", "width=" + WIDTH_PARAMETER);
        tokens.put("quality", "quality=" + QUALITY_PARAMETER);
    }

    private final String template;

    public ImageUrlTemplate(String template) {
        this.template = template;
    }

    /**
     * Image URL for the image of the given width, height and quality
     *
     * @param width   Width of the image
     * @param quality Quality of the image
     * @return The image URL
     */
    public String getUrl(int width, int quality) {
        return template.replace(WIDTH_PARAMETER, String.valueOf(width))
                .replace(HEIGHT_PARAMETER, HYPHEN)
                .replace(QUALITY_PARAMETER, String.valueOf(quality));
    }

    public String getIconSmallSizeUrl() {
        return getUrl(ICON_SMALL_WIDTH, DESIRED_QUALITY);
    }

    public String getIconMediumSizeUrl() {
        return getUrl(ICON_MEDIUM_WIDTH, DESIRED_QUALITY);
    }

    public String getSmallUrl() {
        return getUrl(SMALL_WIDTH, DESIRED_QUALITY);
    }

    public String getMediumUrl() {
        return getUrl(MEDIUM_WIDTH, DESIRED_QUALITY);
    }

    public String getLargeUrl() {
        return getUrl(LARGE_WIDTH, DESIRED_QUALITY);
    }

    public static ImageUrlTemplate parse(String url) {
        Matcher matcher = pattern.matcher(url);

        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, tokens.get(matcher.group(1)));
        }
        matcher.appendTail(sb);
        return new ImageUrlTemplate(sb.toString());
    }
}
