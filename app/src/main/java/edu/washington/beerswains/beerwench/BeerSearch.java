package edu.washington.beerswains.beerwench;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.widget.SearchView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
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
import java.util.Arrays;
import java.util.List;


public class BeerSearch extends ActionBarActivity {
    private BeerFinder finder;
    private JSONObject results;
    private ArrayList<Beer> parseBeers = new ArrayList<Beer>();
    private ArrayList<ParseObject>pBeers = new ArrayList<ParseObject>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_beer_activity);
        SearchView beerSearch = (SearchView) findViewById(R.id.searchBox);
        beerSearch.setQueryHint("Enter Beer Name");
        beerSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.trim().length() > 0) {
                    finder.findBeerByName(query);
                    return true;
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
        PropertyChangeListener listener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent e) {
                String name = e.getPropertyName();
                if (name.equals("found")) {
                    BeerSearch.this.results = (JSONObject) e.getNewValue();
                    populateResults();
                }
            }
        };
        this.finder = new BeerFinder();
        PropertyChangeSupport support = new PropertyChangeSupport(finder);
        support.addPropertyChangeListener("found", listener);
        finder.setSupport(support);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_beer_search, menu);
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

    public void setResults(JSONObject results) {
        this.results = results;
    }

    public void populateResults() {
        ArrayList<Beer> userResults = parseResults(this.results);
        TextView resultView = (TextView) findViewById(R.id.beerInfo);
        BeerListAdapter beerList = new BeerListAdapter(userResults, this);
        ListView beerListView = (ListView) findViewById(R.id.beerList);
        beerListView.setAdapter(beerList);
        beerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> beerView, View v, int position, long id) {
                Intent intent = new Intent(BeerSearch.this, BeerMapActivity.class);
                BeerListAdapter adapter = (BeerListAdapter) beerView.getAdapter();
                Beer selectedBeer = adapter.getBeer(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("beer", selectedBeer);
                intent.putExtra("bundle", bundle);
                startActivity(intent);
            }
        });
    }

    public ArrayList<Beer> parseResults(JSONObject data) {
        ArrayList<Beer> beers = new ArrayList<Beer>();
        try {
            JSONArray beerData = data.getJSONArray("data");
            if (beerData.length() > 0 && beerData != null) {
                for (int i = 0; i < beerData.length(); i++) {
                    Log.e("json", beerData.get(i).toString());
                    JSONObject topicData = (JSONObject) beerData.get(i);
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
                    beers.add(beer);
                    parseBeers.add(beer);
                }
            }
            //pushBeersToParse();
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return beers;
    }

    /*
    public void pushBeersToParse() {
        ParseQuery query = ParseQuery.getQuery("Store").whereMatches("name", "QFC");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    for (Beer beer : parseBeers) {
                        ParseObject beerObject = new ParseObject("Beer");
                        beerObject.put("name", beer.getName());
                        beerObject.put("abv", beer.getAbv());
                        beerObject.put("manufacturer", beer.getProducer());
                        beerObject.put("image", beer.getPictureUrl());
                        beerObject.put("description", beer.getDescription());
                        beerObject.put("dbId", beer.getId());
                        beerObject.saveInBackground();
                        pBeers.add(beerObject);
                        //beers.add(beerObject);
                    }
                    for (ParseObject object : objects) {
                        ArrayList<ParseObject> beers = (ArrayList<ParseObject>) object.get("beers");
                        for(ParseObject beerObject : pBeers) {
                            ParseObject storePrice = new ParseObject("Store_Price");
                            storePrice.put("store", object);
                            storePrice.put("beer", beerObject);
                            storePrice.put("price", 7.49);
                            storePrice.saveInBackground();
                        }
                        for (int i = 0; i < pBeers.size(); i++) {
                            beers.add(pBeers.get(i));
                        }
                        object.put("beers", beers);
                        object.saveInBackground();
                    }
                } else {
                    Toast.makeText(BeerSearch.this, "Unable to Load Stores", Toast.LENGTH_LONG).show();
                }
            }
        });
    } */
}
