package project.patchworks.utils;

import java.net.InetAddress;

/**
 * Created by u1421499 on 23/01/18.
 */

public class Game {

    private final InetAddress address; //local IP to connect to
    private final String name, state; //game display information
    private final int players; //current players in the game
    private final boolean pin; //game requires pin to join

    public Game(InetAddress address, String name, String state, int players, boolean pin) {
        this.address = address;
        this.name = name;
        this.state = state;
        this.players = players;
        this.pin = pin;
    }

    public InetAddress getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public String getState() {
        return state;
    }

    public int getPlayers() {
        return players;
    }

    public boolean getPinProtected() {
        return pin;
    }
}
