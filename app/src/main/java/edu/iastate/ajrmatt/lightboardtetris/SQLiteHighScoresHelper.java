package edu.iastate.ajrmatt.lightboardtetris;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import edu.iastate.ajrmatt.lightboardtetris.HighScoresContract.*;

/**
 * Created by ajrmatt on 12/4/16.
 */

public class SQLiteHighScoresHelper extends SQLiteOpenHelper
{
    public SQLiteHighScoresHelper(Context context)
    {
        super(context, HighScoresContract.DATABASE_NAME, null, HighScoresContract.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String createHighScoresTable = "CREATE TABLE " + HighScoresEntry.TABLE_HIGH_SCORES + " (" +
                HighScoresEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                HighScoresEntry.COLUMN_NAME_PLAYER_NAME + " TEXT NOT NULL, " +
                HighScoresEntry.COLUMN_NAME_SCORE + " TEXT NOT NULL);";
        db.execSQL(createHighScoresTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(SQLiteHighScoresHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + HighScoresEntry.TABLE_HIGH_SCORES);
        onCreate(db);
    }
}