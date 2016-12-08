package edu.iastate.ajrmatt.lightboardtetris;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class GameEndActivity extends Activity {

    private int score;
    private int resultCount;
    private HighScore minHighScore;

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
        System.out.println("Score: " + score);
        scoreView.setText(String.valueOf(score));

        dataSource = HighScoresDataSource.getInstance(getApplicationContext());
        dataSource.open();

//        Cursor cursor = dataSource.getCursorToMinHighScore();
//        int minHighScore = -1;
//        int resultCount = cursor.getCount();
//        if (resultCount > 0) minHighScore = cursor.getInt(0);

        ArrayList<HighScore> highScores = dataSource.getAllHighScoresSorted();
        minHighScore = null;
        resultCount = highScores.size();
        if (resultCount > 0) minHighScore = highScores.get(resultCount-1);

        if(resultCount < 10 || score > minHighScore.getScore())
        {
            nameInput.setVisibility(View.VISIBLE);
            saveButton.setVisibility(View.VISIBLE);
        }
        else
        {
            mainMenuButton.setVisibility(View.VISIBLE);
        }

//        if(resultCount == 0) System.out.println("No results.");
//        if(score > minHighScore.getScore()) System.out.println("Score greater than min.");

//        for(HighScore score : highScores)
//        {
//            System.out.println(score.getScore());
//        }
    }

    public void saveHighScore(View view)
    {
        String name = nameInput.getText().toString();
        if(name.length() > 0)
        {
            if (resultCount >= 10)
            {
                dataSource.deleteHighScore(minHighScore);
            }
            dataSource.createHighScore(name, score);

            for(HighScore score : dataSource.getAllHighScoresSorted())
            {
                System.out.println(score.getPlayerName() + ": " + score.getScore());
            }
            goToActivityOfClass(HighscoresActivity.class);
        }
        else
        {
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_SHORT).show();
        }
    }

    public void returnToMainMenu(View view)
    {
        goToActivityOfClass(MainMenuActivity.class);
    }

    private void goToActivityOfClass(Class theClass)
    {
        Intent intent = new Intent(getApplicationContext(), theClass);
        startActivity(intent);
        finish();
    }
}
