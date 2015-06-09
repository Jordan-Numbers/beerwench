package edu.washington.beerswains.beerwench;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;


public class MyBeerList extends ActionBarActivity {
    private List<Beer> userBeers;
    private BeerApplication myApp;
    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_beer_activity);
        list = (ListView) findViewById(R.id.userBeerList);
        myApp = (BeerApplication) getApplication();
        userBeers = myApp.getUserBeers();
        if (userBeers.size() == 0) {
            TextView header = (TextView) findViewById(R.id.beerListHeader);
            header.setText("You have no beers! Go search for some and add them!");
        } else {
            BeerListAdapter adapter = new BeerListAdapter(userBeers, this);
            list.setAdapter(adapter);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> beerView, View v, int position, long id) {
                    BeerListAdapter adapter = (BeerListAdapter) beerView.getAdapter();
                    Beer selectedBeer = adapter.getBeer(position);
                    makeSelectedAlert(selectedBeer, position);
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_beer_list, menu);
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

    public void makeSelectedAlert(Beer beer, int location) {
        final Beer selectedBeer = beer;
        final int position = location;
        new AlertDialog.Builder(MyBeerList.this).setTitle(beer.getName())
                .setNeutralButton("View On Map", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(MyBeerList.this, BeerMapActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("beer", selectedBeer);
                        intent.putExtra("bundle", bundle);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Delete from My Beers", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                myApp.removeFromList(selectedBeer, position);
                                BeerListAdapter adapter = new BeerListAdapter(userBeers, MyBeerList.this);
                                list.setAdapter(adapter);
                            }
                        }
                ).show();
    }
}
