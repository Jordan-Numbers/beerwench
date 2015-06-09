package edu.washington.beerswains.beerwench;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;

import java.util.List;
import java.util.Map;

/**
 * Created by Smyth on 6/6/2015.
 */
public class BeerApplication extends Application {
    private Map<String, List<Beer>> userBeers;

    @Override
    public void onCreate() {
        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "1x5WB9hiFmJPBBy23a0hWhFhgLHUzD605yLIaWxi", "qjopJX3wOEpbZNhdq303AWSIp9sMfFo1yTgarieb");
        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        ParseACL.setDefaultACL(defaultACL, true);
    }
}
