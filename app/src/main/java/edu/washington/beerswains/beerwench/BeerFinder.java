package edu.washington.beerswains.beerwench;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.net.URLEncoder;

/**
 * Created by Smyth on 6/6/2015.
 */
public class BeerFinder {
    private String key = "06954be80e620927f66e363e3c3c9bf4";
    private JSONObject found;
    private PropertyChangeSupport support;


    public void findBeerByName(String name) {
        try {
            String url = "https://api.brewerydb.com/v2/beers?name=" + URLEncoder.encode(name, "UTF-8") + "&key=" + key + "&withBreweries=Y&format=json";
            JSONParser parser = new JSONParser(this);
            parser.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void findBeerMaker(String id) {
        try {
            String url = "https://api.brewerydb.com/v2/beer/" + URLEncoder.encode(id, "UTF-8") + "/breweries/?key=" + key + "&format=json";
            JSONParser parser = new JSONParser(this);
            parser.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void findBreweryByName(String name) {
        try {
            String url = "https://api.brewerydb.com/v2/breweries?name=" + URLEncoder.encode(name, "UTF-8") + "&key=" + key + "&format=json";
            JSONParser parser = new JSONParser(this);
            parser.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void findBreweryById(String id) {
        try {
            String url = "https://api.brewerydb.com/v2/breweries?id=" + URLEncoder.encode(id, "UTF-8") + "&key=" + key + "&format=json";
            JSONParser parser = new JSONParser(this);
            parser.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setFound(JSONObject found) {
        JSONObject oldFound = this.found;
        this.found = found;
        Log.e("found", found.toString());
        support.firePropertyChange("found", oldFound, found);
    }

    public void setSupport(PropertyChangeSupport support) {
        this.support = support;
    }
}
