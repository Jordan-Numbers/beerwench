package edu.washington.beerswains.beerwench;

import android.app.FragmentManager;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
/**
 * Created by wildemblem on 6/6/15.
 */
public class Fragments extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_activity);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new homepageFragment())
                    .commit();
        }
    }

    public static class homepageFragment extends Fragment {
        public homepageFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.homepage_activity, container, false);
            Button myBeerBtn = (Button) rootView.findViewById(R.id.myBeerButton);
            Button findBeerBtn = (Button) rootView.findViewById(R.id.findBeerButton);
            Button addBeerBtn = (Button) rootView.findViewById(R.id.addBeerButton);

            myBeerBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment fragment = new myBeerFragment();
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, fragment)
                            .commit();
                }
            });

            findBeerBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment fragment = new findBeerFragment();
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, fragment)
                            .commit();
                }
            });

            addBeerBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment fragment = new addBeerFragment();
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, fragment)
                            .commit();
                }
            });


            return rootView;
        }
    }

    public static class myBeerFragment extends Fragment {
        public myBeerFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.my_beer_activity, container, false);

            EditText searchText = (EditText) rootView.findViewById(R.id.search_src_text);
            Button searchBtn = (Button) rootView.findViewById(R.id.search_go_btn);

            return rootView;
        }
    }

    public static class findBeerFragment extends Fragment {
        public findBeerFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.find_beer_activity, container, false);

            EditText beerNameField = (EditText) rootView.findViewById(R.id.beerName);
            EditText beerPriceField = (EditText) rootView.findViewById(R.id.beerPrice);
            Button submitBtn = (Button) rootView.findViewById(R.id.submitButton);

            String beerName = beerNameField.getText().toString();
            int beerPrice = Integer.parseInt(beerPriceField.getText().toString());



            return rootView;
        }

    }

    public static class addBeerFragment extends Fragment {
        public addBeerFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.add_beer_activity, container, false);

            EditText searchText = (EditText) rootView.findViewById(R.id.search_src_text);
            Button searchBtn = (Button) rootView.findViewById(R.id.search_go_btn);

            return rootView;
        }

    }
}
