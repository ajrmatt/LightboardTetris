package edu.iastate.ajrmatt.lightboardtetris;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends Activity
{
    private static final int   COLUMN_COUNT = 12;
    private static final int   ROW_COUNT = 12;
    private static final int[] PLACEMENT_POINT = {COLUMN_COUNT/2, 1};
    private static final int   PLACEMENT_ROTATION = 0;
    public  static final int   BLANK = -1;
    private static final int   VIEW_BLOCK_WIDTH = 70;

    private int[][] grid;
    private int highestFilledBlock = ROW_COUNT - 1;
    private GridLayout gridLayout;
    Tetromino current;
    Tetromino next;

    private int score = 0;

    private Timer fallingTimer;

    private TextView scoreView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        scoreView = (TextView) findViewById(R.id.score);
        scoreView.setText(String.valueOf(score));

        grid = new int[COLUMN_COUNT][ROW_COUNT];
        initGrid();

        gridLayout = (GridLayout) findViewById(R.id.grid);
        // Opposite because Grid Layout is apparently filled top to bottom, left to right
        gridLayout.setColumnCount(COLUMN_COUNT);
        gridLayout.setRowCount(ROW_COUNT);
        initGridLayoutWithViews();
        setGridViewWidths(VIEW_BLOCK_WIDTH);

        startGame();
        updateView();

//        View view = getViewAt(5,5);
//        View view2 = getViewAt(5,6);
//        view.setBackgroundColor(Color.parseColor("#F00000"));
//        view2.setBackgroundColor(Color.parseColor("#00F000"));

    }

    @Override
    protected void onPause()
    {
        super.onPause();
        pauseGame();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        resumeGame();
    }

//    @Override
//    protected void onStop()
//    {
//        super.onStop();
//        finish();
//    }

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

    public void pauseGame()
    {
        fallingTimer.cancel();
    }

    public void resumeGame()
    {
        setFallingTimer();
    }

    public void setFallingTimer()
    {
        if (fallingTimer != null) fallingTimer.cancel();
        fallingTimer = new Timer();
        fallingTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        moveBlockDown(findViewById(R.id.buttonDown));
                    }
                });
            }
        }, 1500, 1500);
    }

    public void placeNextTetromino()
    {
        current = next;
        if(current.placeInGrid(PLACEMENT_POINT[0], PLACEMENT_POINT[1], PLACEMENT_ROTATION)) {
            next = generateRandomTetromino();
        }
        else
        {
            endGame();
        }
    }

    public Tetromino generateRandomTetromino()
    {
        int type = new Random().nextInt(7);
        switch (type)
        {
            case 0  : return new I(grid);
            case 1  : return new J(grid);
            case 2  : return new L(grid);
            case 3  : return new O(grid);
            case 4  : return new S(grid);
            case 5  : return new T(grid);
            case 6  : return new Z(grid);
            default : return null;
        }
    }

    /* TODO Finish */
    public void clearFullRowsAndCollapse()
    {
        // Find full rows (only necessary to check y values of current Tetromino)
        ArrayList<Integer> fullRows = new ArrayList<>();
        for (int l = 0; l < current.getLocation().length; l++)
        {
            boolean rowIsFull = true;
            int rowToCheck = current.getLocation()[l][1];
            for (int i = 0; i < COLUMN_COUNT; i++)
            {
                if (grid[i][rowToCheck] == BLANK)
                {
                    rowIsFull = false;
                    break;
                }
            }
            if (!fullRows.contains(rowToCheck) && rowIsFull) fullRows.add(rowToCheck);
        }
        int numFullRows = fullRows.size();
        if (numFullRows > 0)
        {
            // Sort in descending order
            Collections.sort(fullRows, new Comparator<Integer>()
            {
                @Override
                public int compare(Integer o1, Integer o2)
                {
                    return o2.compareTo(o1);
                }
            });
            // Clear full rows
            for (int r = 0; r < fullRows.size(); r++)
            {
                int rowToClear = fullRows.get(r);
                for (int i = 0; i < COLUMN_COUNT; i++)
                {
                    grid[i][rowToClear] = BLANK;
                }
            }
            // Collapse
            // Starts one row above lowest cleared row
            int clearedRowsBelow = 1;
//            System.out.println("FullRows first index - 1: " + (fullRows.get(0) - 1));
//            System.out.println("highestFilledBlock before clear: " + highestFilledBlock);
            for (int j = fullRows.get(0) - 1; j >= highestFilledBlock; j--)
            {
//                System.out.println(j);
                if (fullRows.size() > clearedRowsBelow && j == fullRows.get(clearedRowsBelow))
                {
//                    System.out.println("j = clearedRowsBelow");
                    clearedRowsBelow++;
                    continue;
                }
                for (int i = 0; i < COLUMN_COUNT; i++)
                {
                    grid[i][j + clearedRowsBelow] = grid[i][j];
                    grid[i][j] = BLANK;
                }
            }
            highestFilledBlock += numFullRows;
//            System.out.println("highestFilledBlock after clear: " + highestFilledBlock);

            switch (numFullRows)
            {
                case 1  : score += 40; break;
                case 2  : score += 100; break;
                case 3  : score += 300; break;
                case 4  : score += 1200; break;
                default : score += 0;
            }
            scoreView.setText(String.valueOf(score));
        }
    }

    public void endGame()
    {
        Intent endGame = new Intent(getApplicationContext(), GameEndActivity.class);
        endGame.putExtra("score", score);
        endGame.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(endGame);
        finish();
    }

    public void updateView()
    {
        for (int j = 0; j < ROW_COUNT; j++)
        {
            for (int i = 0; i < COLUMN_COUNT; i++)
            {
                if (grid[i][j] != BLANK)
                {
                    View view = getViewAt(i, j);
                    view.setBackgroundColor(Color.parseColor(gridColorToViewColor(grid[i][j])));
                }
                else
                {
                    View view = getViewAt(i, j);
                    view.setBackgroundColor(Color.parseColor(gridColorToViewColor(BLANK)));
                }
            }
        }
    }

    public View getViewAt(int x, int y)
    {
        int tag = y * COLUMN_COUNT + x;
        return gridLayout.findViewWithTag(tag);
    }

    private void initGrid()
    {
        for (int j = 0; j < ROW_COUNT; j++)
        {
            for (int i = 0; i < COLUMN_COUNT; i++)
            {
                grid[i][j] = BLANK;
            }
        }
    }

    private void initGridLayoutWithViews()
    {
        for (int j = 0; j < ROW_COUNT; j++)
        {
            for (int i = 0; i < COLUMN_COUNT; i++)
            {
                View view = new View(this);
                int tag = j * COLUMN_COUNT + i;
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
        for (int v = 0; v < gridLayout.getChildCount(); v++)
        {
            View view = gridLayout.getChildAt(v);
            view.getLayoutParams().width  = size;
            view.getLayoutParams().height = size;
        }
    }

    private String gridColorToViewColor(int gridColor)
    {
        switch (gridColor)
        {
            /* TODO change case values to match possible LED matrix/grid values */
            case -1 : return "black";
            case 0  : return "green";
            case 1  : return "red";
            case 2  : return "blue";
            case 3  : return "yellow";
            case 4  : return "purple";
            default : return null;
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
//                System.out.println("blockHeight: " + blockHeight);
                if (blockHeight < highestFilledBlock) highestFilledBlock = blockHeight;
            }
//            System.out.println("highestFilledBlock after set: " + highestFilledBlock);
            clearFullRowsAndCollapse();
            placeNextTetromino();
        }
        updateView();
    }
}