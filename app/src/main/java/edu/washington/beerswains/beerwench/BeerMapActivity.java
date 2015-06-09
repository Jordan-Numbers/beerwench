package edu.washington.beerswains.beerwench;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;


public class BeerMapActivity extends ActionBarActivity {
    private LocationManager mLocationManager;
    private GoogleMap map;
    private Beer beer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beer_map);
        Intent sentData = getIntent();
        Bundle sentBundle = sentData.getBundleExtra("bundle");
        this.beer = (Beer) sentBundle.getSerializable("beer");
       MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap map) {
                loadMap(map);
            }
        });

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_beer_map, menu);
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

    public void loadMap(GoogleMap map) {
        this.map = map;
        map.setMyLocationEnabled(true);
        Location location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null) {
            LatLng coordinates = new LatLng(location.getLatitude(), location.getLongitude());
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 13));
        }
        loadStores();
    }

    public void loadStores() {
        Location location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        final ParseGeoPoint start = new ParseGeoPoint(location.getLatitude(), location.getLongitude());
        ParseQuery firstQuery = ParseQuery.getQuery("Beer").whereMatches("name", this.beer.getName());
        firstQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() > 0) {
                        for (ParseObject object : objects) {
                            Log.e("object Id", object.getObjectId());
                            ParseQuery innerQuery = ParseQuery.getQuery("Beer").whereEqualTo("objectId", object.getObjectId());
                            ParseQuery priceQuery = ParseQuery.getQuery("Store_Price").include("beer").include("store").whereMatchesQuery("beer", innerQuery);
                            priceQuery.findInBackground(new FindCallback<ParseObject>() {
                                @Override
                                public void done(List<ParseObject> objects1, ParseException e) {
                                    if (e == null) {
                                        for (ParseObject object : objects1) {
                                            Log.e("object Id", object.getObjectId());
                                            ParseObject storeData = (ParseObject) object.get("store");
                                            ParseGeoPoint coordinates = (ParseGeoPoint) storeData.get("map_location");
                                            String name = (String) storeData.get("name");
                                            String address = (String) storeData.get("address");
                                            double price = (double) object.get("price");
                                            if (coordinates.distanceInMilesTo(start) <= 10) {
                                                map.addMarker(new MarkerOptions().position(new LatLng(coordinates.getLatitude(), coordinates.getLongitude())).title(name).snippet(address + " - $" + price));
                                            }
                                        }
                                    }
                                }
                            });
                        }
                    } else {
                        Toast.makeText(BeerMapActivity.this, "This beer is not available in your area", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
}
