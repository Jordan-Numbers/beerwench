package edu.washington.beerswains.beerwench;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Smyth on 6/8/2015.
 */
public class StoreListAdapter extends ArrayAdapter<Store> {
    private List<Store> storeList;
    private Context context;

    public StoreListAdapter(List<Store> storeList, Context context) {
        super(context, android.R.layout.simple_list_item_1, storeList);
        this.storeList = storeList;
        this.context = context;
    }

    public int getCount() {
        if (storeList != null)
            return storeList.size();
        return 0;
    }

    public Store getStore(int position) {
        if(storeList != null)
            return storeList.get(position);
        return null;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if(v == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.beer_list_layout, null);
        }

        Store store = storeList.get(position);
        TextView storeName = (TextView) v.findViewById(R.id.name);
        storeName.setText(store.getName());

        TextView storeAddress = (TextView) v.findViewById(R.id.price);
        storeAddress.setText(store.getAddress());

        return v;
    }

    public List<Store> getStoreList() {
        return this.storeList;
    }

    public void setItemList(List<Store> storeList) {
        this.storeList = storeList;
    }
}
