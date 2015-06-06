package edu.washington.beerswains.beerwench;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.net.URLEncoder;

/**
 * Created by Smyth on 6/6/2015.
 */
public class BeerFinder {
    private String key = "06954be80e620927f66e363e3c3c9bf4";

    public void findBeerByName(String name) {
        try {
            String url = "https://api.brewerydb.com/v2/beers?name=" + URLEncoder.encode(name, "UTF-8") + "&key=" + key + "&format=json";
            JSONParser parser = new JSONParser();
            parser.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void findBeerMaker(String id) {
        try {
            String url = "https://api.brewerydb.com/v2/beer/" + URLEncoder.encode(id, "UTF-8") + "/breweries/?key=" + key + "&format=json";
            JSONParser parser = new JSONParser();
            parser.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void findBreweryByName(String name) {
        try {
            String url = "https://api.brewerydb.com/v2/breweries?name=" + URLEncoder.encode(name, "UTF-8") + "&key=" + key + "&format=json";
            JSONParser parser = new JSONParser();
            parser.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void findBreweryById(String id) {
        try {
            String url = "https://api.brewerydb.com/v2/breweries?id=" + URLEncoder.encode(id, "UTF-8") + "&key=" + key + "&format=json";
            JSONParser parser = new JSONParser();
            parser.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
