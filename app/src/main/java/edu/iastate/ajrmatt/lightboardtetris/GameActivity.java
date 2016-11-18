package edu.iastate.ajrmatt.lightboardtetris;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;

public class GameActivity extends Activity {

    private static final int columnCount = 16;
    private static final int rowCount = 18;

    private int[][] grid;
    private GridLayout gridLayout;
    Tetromino current;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        grid = new int[columnCount][rowCount];
        initGrid();

        gridLayout = (GridLayout) findViewById(R.id.grid);
        // Opposite because Grid Layout is apparently filled top to bottom, left to right
        gridLayout.setColumnCount(columnCount);
        gridLayout.setRowCount(rowCount);
        initGridLayoutWithViews();
        setGridViewWidths(60);

        startGame();
        updateView();

//        View view = getViewAt(5,5);
//        View view2 = getViewAt(5,6);
//        view.setBackgroundColor(Color.parseColor("#F00000"));
//        view2.setBackgroundColor(Color.parseColor("#00F000"));

    }

    public void startGame()
    {
        current = new L(grid, 4, 4, 0);
        new J(grid, 4, 7, 0);

    }

    public void updateView()
    {
        for (int i = 0; i < grid.length; i++)
        {
            for (int j = 0; j < grid[i].length; j++)
            {
                if (grid[i][j] != -1)
                {
                    View view = getViewAt(i, j);
                    view.setBackgroundColor(Color.parseColor("green"));
                }
                else
                {
                    View view = getViewAt(i, j);
                    view.setBackgroundColor(Color.parseColor("black"));
                }
            }
        }
    }

    public View getViewAt(int x, int y)
    {
        int tag = y * columnCount + x;
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
        for (int y = 0; y < rowCount; y++)
        {
            for (int x = 0; x < columnCount; x++)
            {
                View view = new View(this);
                int tag = y * columnCount + x;
                System.out.println(tag);
                view.setTag(tag);
//                view.getLayoutParams().width = 10;
//                view.getLayoutParams().height = 10;
                gridLayout.addView(view);
//                if (x == 15 || y == 16) view.setBackgroundColor(Color.parseColor("red"));
            }
        }
    }

    private void setGridViewWidths(int size)
    {
        for (int i = 0; i < gridLayout.getChildCount(); i++)
        {
            View view = gridLayout.getChildAt(i);
            view.getLayoutParams().width = size;
            view.getLayoutParams().height = size;
        }
    }

    public void rotateBlock(View view)
    {
        current.rotate();
        updateView();
    }

    public void moveBlockLeft(View view)
    {
        current.moveLeft();
        updateView();
    }

    public void moveBlockRight(View view)
    {
        current.moveRight();
        updateView();
    }

    public void moveBlockDown(View view)
    {
        current.moveDown();
        updateView();
    }

}