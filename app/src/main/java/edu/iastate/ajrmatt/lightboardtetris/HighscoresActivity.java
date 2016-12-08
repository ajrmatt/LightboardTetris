package edu.iastate.ajrmatt.lightboardtetris;

import android.app.Activity;
import android.app.ListActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListAdapter;

import java.util.ArrayList;

public class HighscoresActivity extends ListActivity
{
    ArrayList<HighScore> highScores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscores);

        HighScoresDataSource dataSource = HighScoresDataSource.getInstance(getApplicationContext());
        dataSource.open();

        highScores = dataSource.getAllHighScoresSorted();

        ListAdapter adapter = new HighScoreListAdapter(this, R.layout.high_score_item, highScores);
        setListAdapter(adapter);
    }
}
