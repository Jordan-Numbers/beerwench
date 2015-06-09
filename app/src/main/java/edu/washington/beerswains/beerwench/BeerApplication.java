package edu.washington.beerswains.beerwench;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Smyth on 6/6/2015.
 */
public class BeerApplication extends Application {
    private List<Beer> userBeers;
    private List<ParseObject> parseBeerList;
    private ParseObject localParseData;

    @Override
    public void onCreate() {
        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "1x5WB9hiFmJPBBy23a0hWhFhgLHUzD605yLIaWxi", "qjopJX3wOEpbZNhdq303AWSIp9sMfFo1yTgarieb");
        userBeers = new ArrayList<Beer>();
        ParseQuery query = ParseQuery.getQuery("BeerList").fromLocalDatastore().include("beers").include("Beer");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() > 0) {
                        ParseObject object = objects.get(0);
                        localParseData = object;
                        parseBeerList = (List<ParseObject>) object.get("beers");
                        for (ParseObject beer : parseBeerList) {
                            String name = (String) beer.get("name");
                            String id = (String) beer.get("dbId");
                            String description = (String) beer.get("description");
                            String image = (String) beer.get("image");
                            String producer = (String) beer.get("manufacturer");
                            String abv = (String) beer.get("abv");
                            userBeers.add(new Beer(name, abv, producer, image, id, description));
                        }
                    } else {
                        parseBeerList = new ArrayList<ParseObject>();
                        localParseData = ParseObject.create("BeerList");
                    }
                }
            }
        });
    }

    public void addToList(Beer beer) {
        userBeers.add(beer);
        final Beer lBeer = beer;
        ParseQuery query = ParseQuery.getQuery("Beer").whereMatches("name", beer.getName());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (objects.size() == 0) {
                    ParseObject object = ParseObject.create("Beer");
                    object.put("name", lBeer.getName());
                    object.put("abv", lBeer.getAbv());
                    object.put("dbId", lBeer.getId());
                    object.put("description", lBeer.getDescription());
                    object.put("image", lBeer.getPictureUrl());
                    object.put("manufacturer", lBeer.getProducer());
                    object.saveInBackground();
                    parseBeerList.add(object);
                } else {
                    ParseObject object = objects.get(0);
                    parseBeerList.add(object);
                }
                localParseData.put("beers", parseBeerList);
                /*
                localParseData.pinInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException ex) {
                        if (ex == null) {
                            Toast.makeText(getApplicationContext(), "Beer Successfully Added to Your Beers", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Unable to save beer", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                */
                try {
                    localParseData.pin();
                    Toast.makeText(getApplicationContext(), "Beer Successfully Added to Your Beers", Toast.LENGTH_SHORT).show();
                } catch (ParseException e1) {
                    Toast.makeText(getApplicationContext(), "Unable to save beer", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void removeFromList(Beer selectedBeer, int location) {
        userBeers.remove(location);
        parseBeerList.remove(location);
        localParseData.put("beers", parseBeerList);
        localParseData.pinInBackground(new SaveCallback() {
            @Override
            public void done(ParseException ex) {
                if (ex == null) {
                    Toast.makeText(getApplicationContext(), "Beer Successfully Removed From Your Beers", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Unable to remove beer", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public List<Beer> getUserBeers() {
        return this.userBeers;
    }
}
