package model;

import java.util.ArrayList;
import java.util.LinkedHashMap;

// Represents a match to be tracked by the application
public class Match {
    private final int matchId;
    private final ArrayList<Player> players;
    private final int roundsWon;
    private final int roundsLost;
    private final String map;

    // EFFECTS: Constructs a match with the provided roster and data values
    public Match(int matchId, Roster roster, int roundsWon, int roundsLost, String map) {
        this.matchId = matchId;
        this.players = new ArrayList<>();
        this.players.addAll(roster.getPlayers());
        this.roundsWon = roundsWon;
        this.roundsLost = roundsLost;
        this.map = map;
    }

    public int getMatchId() {
        return matchId;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    // EFFECTS: Returns the total number of rounds played in this match (wonRounds + lostRounds)
    public int getTotalRounds() {
        return roundsWon + roundsLost;
    }

    public int getRoundsWon() {
        return roundsWon;
    }

    public int getRoundsLost() {
        return roundsLost;
    }

    public String getMap() {
        return map;
    }
}
