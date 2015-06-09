package edu.washington.beerswains.beerwench;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;


public class BeerAdd extends ActionBarActivity {
    private ArrayList<ParseObject> storeObjects = new ArrayList<ParseObject>();
    private int selectedPosition;
    private BeerFinder beerFinder;
    private JSONObject foundNewBeer;
    private ParseObject parseStore;
    private double parsePrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_beer_activity);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        PropertyChangeListener listener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent e) {
                String name = e.getPropertyName();
                if (name.equals("found")) {
                    BeerAdd.this.foundNewBeer = (JSONObject) e.getNewValue();
                    makeNewParseBeer();
                }
            }
        };
        this.beerFinder = new BeerFinder();
        PropertyChangeSupport support = new PropertyChangeSupport(beerFinder);
        support.addPropertyChangeListener("found", listener);
        beerFinder.setSupport(support);
        final ArrayList<Store> stores = new ArrayList<Store>();
        LocationManager mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        final ParseGeoPoint start = new ParseGeoPoint(location.getLatitude(), location.getLongitude());
        ParseQuery findStores = ParseQuery.getQuery("Store").whereWithinMiles("map_location", start, 10.0);
        findStores.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    for (ParseObject object : objects) {
                        String name = (String) object.get("name");
                        ParseGeoPoint location = (ParseGeoPoint) object.get("map_location");
                        String address = (String) object.get("address");
                        Store store = new Store(name, address, location);
                        stores.add(store);
                        storeObjects.add(object);
                    }
                    populateListView(stores);
                }
            }
        });
        Button submit = (Button) findViewById(R.id.submitButton);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListView storeList = (ListView) findViewById(R.id.storeListView);
                EditText beerName = (EditText) findViewById(R.id.beerName);
                EditText beerPrice = (EditText) findViewById(R.id.beerPrice);
                try {
                    final String name = beerName.getText().toString();
                    final double price = Double.parseDouble(beerPrice.getText().toString());
                    final int position = selectedPosition;
                    Store store = (Store) storeList.getItemAtPosition(position);
                    if (store == null || name == null || price == 0) {
                        Toast.makeText(BeerAdd.this, "Please Enter Values in all Fields!", Toast.LENGTH_LONG).show();
                        //Log.e("store", store.toString());
                        Log.e("name", name);
                        Log.e("price", "" + price);
                    } else {
                        ParseQuery<ParseObject> verifyBeer = ParseQuery.getQuery("Beer").whereMatches("name", name);
                        verifyBeer.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> objects, ParseException e) {
                                if (e == null) {
                                    if (objects.size() > 0) {
                                        makeBeerPrice(objects.get(0), storeObjects.get(position), price);
                                    } else {
                                        parseStore = storeObjects.get(position);
                                        parsePrice = price;
                                        beerFinder.findBeerByName(name);
                                        //Toast.makeText(BeerAdd.this, "I'm sorry, this beer does not exist in our database", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
                    }
                } catch(Exception e) {
                    Toast.makeText(BeerAdd.this, "Exception", Toast.LENGTH_LONG).show();
                    //Log.e("exception caused by", e.getCause().toString());
                }

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_beer_add, menu);
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

    public void populateListView(ArrayList<Store> stores) {
        ListView storeList = (ListView) findViewById(R.id.storeListView);
        StoreListAdapter adapter = new StoreListAdapter(stores, this);
        storeList.setAdapter(adapter);
        storeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> storeView, View v, int position, long idView) {
                selectedPosition = position;
            }
        });
    }

    public void makeBeerPrice(ParseObject beer, ParseObject store, double price) {
        ParseObject storePrice = new ParseObject("Store_Price");
        storePrice.put("beer", beer);
        storePrice.put("store", store);
        storePrice.put("price", price);
        storePrice.saveInBackground();
        Toast.makeText(BeerAdd.this, "Information submitted successfully", Toast.LENGTH_SHORT).show();
    }

    public void makeNewParseBeer() {
        try {
            JSONArray beerData = foundNewBeer.getJSONArray("data");
            if (beerData.length() > 0) {
                JSONObject topicData = (JSONObject) beerData.get(0);
                String title = topicData.getString("name");
                String description = topicData.getString("description");
                String id = topicData.getString("id");
                JSONObject labels = topicData.getJSONObject("labels");
                String iconUrl = labels.getString("icon");
                String abv = topicData.getString("abv") + "% abv";
                JSONArray producerData = topicData.getJSONArray("breweries");
                JSONObject breweryInfo = (JSONObject) producerData.get(0);
                String bName = breweryInfo.getString("name");
                Beer beer = new Beer(title, abv, bName, iconUrl, id, description);
                ParseObject parseBeer = new ParseObject("Beer");
                parseBeer.put("dbId", beer.getId());
                parseBeer.put("image", beer.getPictureUrl());
                parseBeer.put("abv", beer.getAbv());
                parseBeer.put("description", beer.getDescription());
                parseBeer.put("manufacturer", beer.getProducer());
                parseBeer.put("name", beer.getName());
                parseBeer.saveInBackground();
                Toast.makeText(this, "Congratulations, you found a new beer for us!", Toast.LENGTH_SHORT).show();
                makeBeerPrice(parseBeer, parseStore, parsePrice);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
