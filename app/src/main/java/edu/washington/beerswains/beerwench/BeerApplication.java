package edu.washington.beerswains.beerwench;

import android.app.Application;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by Smyth on 6/6/2015.
 */
public class BeerApplication extends Application {
    private List<Beer> userBeers;

    @Override
    public void onCreate() {
        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        //register & init
        ParseObject.registerSubclass(Beer.class);
        Parse.initialize(this, "1x5WB9hiFmJPBBy23a0hWhFhgLHUzD605yLIaWxi", "qjopJX3wOEpbZNhdq303AWSIp9sMfFo1yTgarieb");

        //setup anonymous users
        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        ParseACL.setDefaultACL(defaultACL, true);

        ParseQuery<Beer> query = ParseQuery.getQuery(Beer.class);
        query.findInBackground(new FindCallback<Beer>() {
            @Override
            public void done(List<Beer> results, ParseException e) {
                for (Beer aBeer : results) {
                    userBeers.add(aBeer);
                }
            }
        });
    }
}
