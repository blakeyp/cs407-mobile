package patchworks.utils;

import java.net.InetAddress;

/**
 * Created by u1421499 on 23/01/18.
 */

public class Level {

    private final String name, author; //game display information
    private final float stars; //current players in the game

    public Level(String name, String author, float stars) {
        this.name = name;
        this.author = author;
        this.stars = stars;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public float getStars() { return stars; }

}
