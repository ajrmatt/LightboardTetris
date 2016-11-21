package edu.iastate.ajrmatt.lightboardtetris;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends Activity
{
    private static final int COLUMN_COUNT = 14;
    private static final int ROW_COUNT = 18;
    private static final int[] PLACEMENT_POINT = {7, 1};
    private static final int PLACEMENT_ROTATION = 0;
    public static final int BLANK = -1;

    private int[][] grid;
    private int highestFilledBlock = 0;
    private GridLayout gridLayout;
    Tetromino current;
    Tetromino next;

    private Timer fallingTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        grid = new int[COLUMN_COUNT][ROW_COUNT];
        initGrid();

        gridLayout = (GridLayout) findViewById(R.id.grid);
        // Opposite because Grid Layout is apparently filled top to bottom, left to right
        gridLayout.setColumnCount(COLUMN_COUNT);
        gridLayout.setRowCount(ROW_COUNT);
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
//        current = generateRandomTetromino();
//        current.placeInGrid(4, 4, 0);
//        new J(grid).placeInGrid(4, 7, 0);
//        boolean running = true;
//        while(running)
//        {
            next = generateRandomTetromino();
            placeNextTetromino();
            updateView();

//        }
    }

    public void placeNextTetromino()
    {
        current = next;
        current.placeInGrid(PLACEMENT_POINT[0], PLACEMENT_POINT[1], PLACEMENT_ROTATION);
        next = generateRandomTetromino();
        if (fallingTimer != null) fallingTimer.cancel();
        fallingTimer = new Timer();
        fallingTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run()
            {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run()
                    {
                        moveBlockDown(findViewById(R.id.buttonDown));
                    }
                });
            }
        }, 1500, 1500);
    }

    public Tetromino generateRandomTetromino()
    {
        int type = new Random().nextInt(7);
        switch (type)
        {
            case 0 : return new I(grid);
            case 1 : return new J(grid);
            case 2 : return new L(grid);
            case 3 : return new O(grid);
            case 4 : return new S(grid);
            case 5 : return new T(grid);
            case 6 : return new Z(grid);
            default: return null;
        }
    }

    /* TODO Finish */
    public void clearFullRowsAndCollapse()
    {
        // Find full rows
        ArrayList<Integer> fullRows = new ArrayList<>();
        for (int i = 0; i < current.getLocation().length; i++)
        {
            boolean rowIsFull = true;
            int rowToCheck = current.getLocation()[i][1];
            for (int j = 0; j < COLUMN_COUNT; j++)
            {
                if (grid[j][rowToCheck] == BLANK)
                {
                    rowIsFull = false;
                    break;
                }
            }
            if (!fullRows.contains(rowToCheck) && rowIsFull) fullRows.add(rowToCheck);
        }
        // Clear full rows
        for (int i = fullRows.size() - 1; i >= 0; i--)
        {
            int rowToClear = fullRows.get(i);
            for (int j = 0; j < COLUMN_COUNT; j++)
            {
                grid[i][j] = BLANK;
            }
        }
        // Collapse
            // Starts one row above lowest cleared row
        for (int i = fullRows.indexOf(fullRows.size()); i <= highestFilledBlock; i--)
        {

        }
    }

    public void updateView()
    {
        for (int i = 0; i < COLUMN_COUNT; i++)
        {
            for (int j = 0; j < ROW_COUNT; j++)
            {
                if (grid[i][j] != BLANK)
                {
                    View view = getViewAt(i, j);
                    view.setBackgroundColor(Color.parseColor(gridColorToViewColor(grid[i][j])));
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
        int tag = y * COLUMN_COUNT + x;
        View view = gridLayout.findViewWithTag(tag);
        return view;
    }

    private void initGrid()
    {
        for (int x = 0; x < COLUMN_COUNT; x++)
        {
            for (int y = 0; y < ROW_COUNT; y++)
            {
                grid[x][y] = BLANK;
            }
        }
    }

    private void initGridLayoutWithViews()
    {
        for (int y = 0; y < ROW_COUNT; y++)
        {
            for (int x = 0; x < COLUMN_COUNT; x++)
            {
                View view = new View(this);
                int tag = y * COLUMN_COUNT + x;
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
            view.getLayoutParams().width  = size;
            view.getLayoutParams().height = size;
        }
    }

    private String gridColorToViewColor(int gridColor)
    {
        switch (gridColor)
        {
            /* TODO change case values to match possible LED matrix/grid values */
            case 0 : return "green";
            case 1 : return "red";
            case 2 : return "blue";
            case 3 : return "yellow";
            case 4 : return "purple";
            default: return null;
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
        if (!current.moveDown())
        {
            current.setSet(true);
            for (int i = 0; i < current.getLocation().length; i++)
            {
                int blockHeight = current.getLocation()[i][1];
                if (blockHeight > highestFilledBlock) highestFilledBlock = blockHeight;
            }
            placeNextTetromino();
        }
        updateView();
    }
}