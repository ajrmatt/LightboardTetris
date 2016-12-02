package edu.iastate.ajrmatt.lightboardtetris;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class GameEndActivity extends Activity {

    private int score;

    private TextView scoreView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_end);

        scoreView = (TextView) findViewById(R.id.score);
        score = getIntent().getIntExtra("score", 0);
        scoreView.setText(String.valueOf(score));
    }
}
