package edu.washington.beerswains.beerwench;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;
import android.widget.ArrayAdapter;


/**
 * Created by Trent Rosane on 6/7/15.
 */
public class BeerListAdapter extends ArrayAdapter<Beer> {
    private List<Beer> beerList;
    private Context context;

    public BeerListAdapter(List<Beer> beerList, Context context) {
        super(context, android.R.layout.simple_list_item_1, beerList);
        this.beerList = beerList;
        this.context = context;
    }

    public int getCount() {
        if (beerList != null)
            return beerList.size();
        return 0;
    }

    public Beer getBeer(int position) {
        if(beerList != null)
            return beerList.get(position);
        return null;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if(v == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.beer_list_layout, null);
        }

        Beer beer = beerList.get(position);
        TextView beerName = (TextView) v.findViewById(R.id.name);
        beerName.setText(beer.getName());

        TextView beerPrice = (TextView) v.findViewById(R.id.price);
        beerPrice.setText(beer.getPrice());

        return v;
    }

    public List<Beer> getBeerList() {
        return beerList;
    }

    public void setItemList(List<Beer> beerList) {
        this.beerList = beerList;
    }


}
