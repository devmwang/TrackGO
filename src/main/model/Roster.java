package model;

import java.util.ArrayList;

// Represents a roster of players to be tracked by the application
public class Roster {
    private String name;
    private ArrayList<Player> players;
    private int gamesPlayed;
    private int roundsPlayed;
    private int wins;
    private int ties;

    public Roster(String name) {
        this.name = name;
        this.players = new ArrayList<Player>();
        this.gamesPlayed = 0;
        this.roundsPlayed = 0;
        this.wins = 0;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void removePlayer(Player player) {
        players.remove(player);
    }



    public String getName() {
        return name;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public int getRoundsPlayed() {
        return roundsPlayed;
    }

    public int getWins() {
        return wins;
    }

    public int getTies() {
        return ties;
    }

    public int getLosses() {
        return gamesPlayed - wins - ties;
    }
}