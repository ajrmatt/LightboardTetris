package edu.iastate.ajrmatt.lightboardtetris;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by ajrmatt on 12/6/16.
 */

public class HighScoreListAdapter extends ArrayAdapter<HighScore>
{
    private Context context;
    private int layoutResourceId;
    private List<HighScore> data = null;

    public HighScoreListAdapter(Context context, int layoutResourceId, List<HighScore> data)
    {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;
        TextView nameView = new TextView(context);
        TextView scoreView = new TextView(context);


        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        row = inflater.inflate(layoutResourceId, parent, false);

        nameView = (TextView) row.findViewById(R.id.playerName);
        scoreView = (TextView) row.findViewById(R.id.score);

        HighScore highScore = data.get(position);

        nameView.setText(highScore.getPlayerName());
        scoreView.setText(String.valueOf(highScore.getScore()));

        return row;
    }
}
