package edu.washington.beerswains.beerwench;

/**
 * Created by Smyth on 6/6/2015.
 */
public class Beer {
    private String name;
    private String abv;
    private String type;
    private String producer;

    public Beer(String name, String abv, String type, String producer) {
        this.name = name;
        this.abv = abv;
        this.type = type;
        this.producer = producer;
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
