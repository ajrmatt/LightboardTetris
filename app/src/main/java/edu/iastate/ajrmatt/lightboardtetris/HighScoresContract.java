package edu.iastate.ajrmatt.lightboardtetris;

import android.provider.BaseColumns;

/**
 * Created by ajrmatt on 12/4/16.
 */

public final class HighScoresContract
{
    public static final String DATABASE_NAME = "highscores.db";
    public static final int DATABASE_VERSION = 1;

    private HighScoresContract() {}

    public static class HighScoresEntry implements BaseColumns
    {
        public static final String TABLE_HIGH_SCORES = "highScores";
        public static final String COLUMN_NAME_PLAYER_NAME = "playerName";
        public static final String COLUMN_NAME_SCORE = "SCORE";
    }
}
