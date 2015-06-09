package edu.washington.beerswains.beerwench;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage_activity);
        //BeerFinder finder = new BeerFinder();
        Button findBeer = (Button) findViewById(R.id.findBeerButton);
        findBeer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BeerSearch.class);
                startActivity(intent);
            }
        });
        Button addBeer = (Button) findViewById(R.id.addBeerButton);
        addBeer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BeerAdd.class);
                startActivity(intent);
            }
        });
        /*
        LatLng location = getLocationFromAddress("4224 University Way NE Seattle, WA 98105");
        if (location != null) {
            ParseObject store = new ParseObject("Store");
            ParseGeoPoint locale = new ParseGeoPoint(location.latitude, location.longitude);
            store.put("map_location", locale);
            store.put("address", "4224 University Way NE Seattle, WA 98105");
            store.put("beers", new ArrayList<Beer>());
            store.put("name", "7/11");
            store.saveInBackground();
        }
        //ParseObject testObject = new ParseObject("TestObject");
        //testObject.put("foo", "bar");
        //testObject.saveInBackground(); */
    }

    public LatLng getLocationFromAddress(String address) {
        List<Address> addresses;
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocationName(address, 1);
            return new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude());
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
