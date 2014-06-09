package com.guardian.briefing.app;

import android.util.Log;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guardian.briefing.app.content.ApiResponse;
import com.guardian.briefing.app.content.Card;
import com.guardian.briefing.app.content.Item;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.internal.http.Request;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class ContentHelper {
    public final static String url = "http://mobile-apps.guardianapis.com/uk/groups/collections/uk-alpha/news/regular-stories";

    public ArrayList<Item> getItems() {
        OkHttpClient client = new OkHttpClient();

        try {
            HttpURLConnection connection = client.open(new URL(url));
            ApiResponse response = new ObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES).readValue(connection.getInputStream(), ApiResponse.class);
            return getItems(response);
        } catch (MalformedURLException e) {
            Log.e(MainActivity.LOG_TAG, "Error downloading content", e);
        } catch (IOException e) {
            Log.e(MainActivity.LOG_TAG, "Error downloading content", e);
        }

        return null;
    }

    private ArrayList<Item> getItems(ApiResponse response) {
        ArrayList<Item> items = new ArrayList<Item>(response.cards.length);
        for (Card card : response.cards) {
            items.add(card.item);
        }
        return items;
    }
}
