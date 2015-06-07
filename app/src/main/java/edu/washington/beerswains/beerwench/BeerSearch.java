package edu.washington.beerswains.beerwench;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Arrays;


public class BeerSearch extends ActionBarActivity {
    private BeerFinder finder;
    private JSONObject results;


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
        //Toast.makeText(BeerSearch.this, "" + support.hasListeners("found"), Toast.LENGTH_SHORT).show();

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
        if (userResults.size() > 0) {
            for (int i = 0; i < userResults.size(); i++) {
                resultView.setText(userResults.get(i).getName());
            }
        } else {
            resultView.setText("No Results Found");
        }
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
                    String abv = topicData.getString("abv") + "%";
                    Beer beer = new Beer(title, abv, "test", iconUrl, id, description);
                    beers.add(beer);
                }
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return beers;
    }
}
