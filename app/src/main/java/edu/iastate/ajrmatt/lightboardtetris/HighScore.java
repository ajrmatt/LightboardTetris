package edu.iastate.ajrmatt.lightboardtetris;

/**
 * Created by ajrmatt on 12/4/16.
 */

public class HighScore
{

    private long id;
    private String playerName;
    private int score;

    public HighScore(long theId, String thePlayerName, int theScore)
    {
        id = theId;
        playerName = thePlayerName;
        score = theScore;
    }

    public HighScore() {}


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
