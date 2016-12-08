package edu.iastate.ajrmatt.lightboardtetris;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import edu.iastate.ajrmatt.lightboardtetris.HighScoresContract.*;

/**
 * Created by ajrmatt on 12/4/16.
 */

public class HighScoresDataSource
{

    private static HighScoresDataSource dsInstance = null;

    private SQLiteDatabase database;
    private SQLiteHighScoresHelper dbHelper;

    private String[] allColumns = { HighScoresEntry._ID,
            HighScoresEntry.COLUMN_NAME_PLAYER_NAME,
            HighScoresEntry.COLUMN_NAME_SCORE };

    public static HighScoresDataSource getInstance(Context context) {
        if (dsInstance == null)
        {
            dsInstance = new HighScoresDataSource(context.getApplicationContext());
        }
        return dsInstance;
    }

    private HighScoresDataSource(Context context) {
        dbHelper = new SQLiteHighScoresHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public HighScore createHighScore(String name, int score)
    {

        // Put keys (row columns) and values (parameters) into ContentValues object
        ContentValues contentValues = new ContentValues();
        contentValues.put(HighScoresEntry.COLUMN_NAME_PLAYER_NAME, name);
        contentValues.put(HighScoresEntry.COLUMN_NAME_SCORE, score);

        // Insert ContentValues into row in events table and obtain row ID
        // HINT: database.insert(...) returns the id of the row you insert
        long id = -1;
        id = database.insert(HighScoresEntry.TABLE_HIGH_SCORES, null, contentValues);

        // Query database for event row just added using the getEvent(...) method
        // NOTE: You need to write a query to get an event by id at the to-do marker
        //		 in the getEvent(...) method
        HighScore newHighScore = getHighScore(id);

        return newHighScore;
    }

    public HighScore getHighScore(long id) {

        Cursor cursor = database.rawQuery("SELECT * FROM " + HighScoresEntry.TABLE_HIGH_SCORES + " WHERE _id = ?", new String[] {String.valueOf(id)});

		/*database.query(SQLiteHelper.TABLE_EVENTS,
				null, "? = ?", new String[] { SQLiteHelper.COLUMN_ID, String.valueOf(id) },
				null, null, null, null);*/

        if(cursor!=null && cursor.moveToFirst())
        {cursor.moveToFirst();}
        HighScore toReturn = cursorToEvent(cursor);
        cursor.close();
        return toReturn;
    }

    public Cursor getCursorToMinHighScore()
    {
        Cursor cursor = database.query(HighScoresEntry.TABLE_HIGH_SCORES, new String[] { "MIN(" + HighScoresEntry.COLUMN_NAME_SCORE + ")"}, null, null, null, null, null);
        cursor.moveToFirst();
        return cursor;
    }

    public void deleteHighScore(HighScore highScore)
    {
        long id = highScore.getId();
        String id_string = "" + id;
//        database.delete(HighScoresEntry.TABLE_HIGH_SCORES, "? = ?", new String[] { HighScoresEntry._ID, id_string });
        database.execSQL("DELETE FROM " + HighScoresEntry.TABLE_HIGH_SCORES + " WHERE " + HighScoresEntry._ID + " = " + id_string);
    }

    public void clearHighScores()
    {
        database.execSQL("DELETE FROM " + HighScoresEntry.TABLE_HIGH_SCORES);
//        database.execSQL("TRUNCATE TABLE " + HighScoresEntry.TABLE_HIGH_SCORES + ";");
    }

    public ArrayList<HighScore> getAllHighScoresSorted() {
        ArrayList<HighScore> highScores = new ArrayList<HighScore>();

        // Query of all HighScore
        Cursor cursor = database.query(HighScoresEntry.TABLE_HIGH_SCORES, allColumns, null,
                null, null, null, null);

        cursor.moveToFirst();

        // Create HighScore objects for each item in list
        while (!cursor.isAfterLast()) {
            HighScore highScore = cursorToEvent(cursor);
            highScores.add(highScore);
            cursor.moveToNext();
        }

        cursor.close();

        // Sort in descending order
        Collections.sort(highScores, new Comparator<HighScore>()
        {
            @Override
            public int compare(HighScore o1, HighScore o2)
            {
                return Integer.compare(o2.getScore(), o1.getScore());
            }
        });
        return highScores;
    }

    /*
     * Helper method to convert row data into HighScore
     */
    private HighScore cursorToEvent(Cursor cursor) {
        HighScore highScore = new HighScore();

        highScore.setId(Long.parseLong(cursor.getString(cursor.getColumnIndex(HighScoresEntry._ID))));
        highScore.setPlayerName(cursor.getString(cursor.getColumnIndex(HighScoresEntry.COLUMN_NAME_PLAYER_NAME)));
        highScore.setScore(cursor.getInt(cursor.getColumnIndex(HighScoresEntry.COLUMN_NAME_SCORE)));

        return highScore;
    }

}