package edu.iastate.ajrmatt.lightboardtetris;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainMenuActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

    }

    public void onHighscoresClick(View view) {
        Intent intent = new Intent(this, HighscoresActivity.class);
        startActivity(intent);

    }

    public void onStartGameClick(View view)  {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }
}
