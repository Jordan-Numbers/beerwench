package edu.washington.beerswains.beerwench;

/**
 * Created by Smyth on 6/6/2015.
 */
public class Beer {
    private String name;
    private String abv;
    private String type;
    private String producer;
    private String pictureUrl;
    private String id;

    public Beer(String name, String abv, String type, String producer, String pictureUrl, String id) {
        this.name = name;
        this.abv = abv;
        this.type = type;
        this.producer = producer;
        this.pictureUrl = pictureUrl;
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public String getAbv() {
        return this.abv;
    }

    public String getType() {
        return this.type;
    }

    public String getProducer() {
        return this.producer;
    }
}
