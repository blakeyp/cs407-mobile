package patchworks.utils;

import android.graphics.drawable.Drawable;

import java.net.InetAddress;

/**
 * Created by u1421499 on 23/01/18.
 */

public class Level {

    private final String name, author; //game display information
    private final float stars; //current players in the game
    private final int thumbnail; //drawable index for thumbanil

    public Level(String name, String author, float stars, int thumbnail) {
        this.name = name;
        this.author = author;
        this.stars = stars;
        this.thumbnail = thumbnail;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public float getStars() { return stars; }

    public int getThumbnail() { return thumbnail; }

}
