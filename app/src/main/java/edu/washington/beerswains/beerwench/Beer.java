package edu.washington.beerswains.beerwench;

import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.Serializable;

/**
 * Created by Smyth on 6/6/2015.
 */

public class Beer  extends ParseObject implements Serializable {

    private String name;
    private String abv;
    private String producer;
    private String pictureUrl;
    private String id;
    private String description;

    public Beer(String name, String abv, String producer, String pictureUrl, String id, String description) {
        this.name = name;
        this.abv = abv;
        this.producer = producer;
        this.pictureUrl = pictureUrl;
        this.id = id;
        this.description = description;
    }

    public String getName() {
        return this.name;
    }

    public String getAbv() {
        return this.abv;
    }

    public String getProducer() {
        return this.producer;
    }

    public static ParseQuery<Beer> getQuery() {
        return ParseQuery.getQuery(Beer.class);
    }

    public String getPictureUrl() {
        return this.pictureUrl;
    }

    public String getId() {
        return this.id;
    }

    public String getDescription() {
        return this.description;
    }

    public void saveBeer() {
        this.pinInBackground(BeerApplication);
    }
}
