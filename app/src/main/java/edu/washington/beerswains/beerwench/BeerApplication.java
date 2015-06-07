package edu.washington.beerswains.beerwench;

import android.app.Application;
import android.content.Context;
import android.os.Environment;

import com.parse.Parse;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
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
    }
}
