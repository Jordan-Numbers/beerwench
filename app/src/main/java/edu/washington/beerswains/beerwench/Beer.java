package edu.washington.beerswains.beerwench;

/**
 * Created by Smyth on 6/6/2015.
 */
public class Beer {
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
}
