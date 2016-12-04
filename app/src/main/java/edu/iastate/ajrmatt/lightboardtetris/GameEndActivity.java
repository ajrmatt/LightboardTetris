package edu.iastate.ajrmatt.lightboardtetris;

import android.app.Activity;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class GameEndActivity extends Activity {

    private int score;

    private HighScoresDataSource dataSource;

    private TextView scoreView;
    private EditText nameInput;
    private Button   saveButton;
    private Button   mainMenuButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_end);

        scoreView = (TextView) findViewById(R.id.score);
        nameInput = (EditText) findViewById(R.id.name);
        saveButton = (Button) findViewById(R.id.save);
        mainMenuButton = (Button) findViewById(R.id.mainMenu);

        score = getIntent().getIntExtra("score", 0);
        scoreView.setText(String.valueOf(score));

        dataSource = HighScoresDataSource.getInstance(this);
        Cursor cursor = dataSource.getCursorToMinHighScore();
        int minHighScore = -1;
        int resultCount = cursor.getCount();
        if (resultCount > 0) minHighScore = cursor.getInt(0);

        if(resultCount == 0 || score > minHighScore)
        {
            nameInput.setVisibility(View.VISIBLE);
            saveButton.setVisibility(View.VISIBLE);
        }
        else
        {
            mainMenuButton.setVisibility(View.VISIBLE);
        }


    }

    public void saveHighScore(View view)
    {

    }

    public void returnToMainMenu(View view)
    {

    }
}
