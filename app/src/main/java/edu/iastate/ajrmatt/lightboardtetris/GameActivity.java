package edu.iastate.ajrmatt.lightboardtetris;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;

public class GameActivity extends Activity {

    private static final int columnCount = 8;
    private static final int rowCount = 8;

    private int[][] grid;
    private GridLayout gridLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        grid = new int[columnCount][rowCount];
        initGrid();

        gridLayout = (GridLayout) findViewById(R.id.grid);
        gridLayout.setColumnCount(columnCount);
        gridLayout.setRowCount(rowCount);
        initGridLayoutWithViews();
        setGridViewWidths(90);


//        View view = getViewAt(5,5);
//        View view2 = getViewAt(5,6);
//        view.setBackgroundColor(Color.parseColor("#F00000"));
//        view2.setBackgroundColor(Color.parseColor("#00F000"));

    }

    public void startGame()
    {

    }

    public View getViewAt(int x, int y)
    {
        int tag = x * columnCount + y;
        View view = gridLayout.findViewWithTag(tag);
        return view;
    }

    private void initGrid()
    {
        for (int x = 0; x < columnCount; x++)
        {
            for (int y = 0; y < rowCount; y++)
            {
                grid[x][y] = -1;
            }
        }
    }

    private void initGridLayoutWithViews()
    {
        for (int x = 0; x < columnCount; x++)
        {
            for (int y = 0; y < rowCount; y++)
            {
                View view = new View(this);
                int tag = y * columnCount + x;
                view.setTag(tag);
//                view.getLayoutParams().width = 10;
//                view.getLayoutParams().height = 10;
                gridLayout.addView(view);
            }
        }
    }

    private void setGridViewWidths(int width)
    {
        for (int i = 0; i < gridLayout.getChildCount(); i++)
        {
            View view = gridLayout.getChildAt(i);
            view.getLayoutParams().width = width;
            view.getLayoutParams().height = width;
        }
    }

}
