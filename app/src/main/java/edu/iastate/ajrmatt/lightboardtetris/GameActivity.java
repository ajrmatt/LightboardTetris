package edu.iastate.ajrmatt.lightboardtetris;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.hardware.usb.UsbAccessory;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends Activity
{

    // TAG is used to debug in Android logcat console
    private static final String TAG = "ArduinoAccessory";

    private static final String ACTION_USB_PERMISSION = "com.google.android.DemoKit.action.USB_PERMISSION";

    private UsbManager mUsbManager;
    private PendingIntent mPermissionIntent;
    private boolean mPermissionRequestPending;
    private ToggleButton buttonLED;

    UsbAccessory mAccessory;
    ParcelFileDescriptor mFileDescriptor;
    FileInputStream mInputStream;
    FileOutputStream mOutputStream;

    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbAccessory accessory = (UsbAccessory) intent.getParcelableExtra(UsbManager.EXTRA_ACCESSORY);
                    if (intent.getBooleanExtra(
                            UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        openAccessory(accessory);
                    } else {
                        Log.d(TAG, "permission denied for accessory "
                                + accessory);
                    }
                    mPermissionRequestPending = false;
                }
            } else if (UsbManager.ACTION_USB_ACCESSORY_DETACHED.equals(action)) {
                UsbAccessory accessory = (UsbAccessory) intent.getParcelableExtra(UsbManager.EXTRA_ACCESSORY);
                if (accessory != null && accessory.equals(mAccessory)) {
                    closeAccessory();
                }
            }
        }
    };






    private static final int   COLUMN_COUNT = 12;
    private static final int   ROW_COUNT = 12;
    private static final int[] PLACEMENT_POINT = {COLUMN_COUNT/2, 1};
    private static final int   PLACEMENT_ROTATION = 0;
    public  static final int   BLANK = -1;
    private static final int   VIEW_BLOCK_WIDTH = 70;
    private static final int   FALL_TIME = 1000;

    private int[][] grid;
    private byte[] matrix;
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

        mUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        filter.addAction(UsbManager.ACTION_USB_ACCESSORY_DETACHED);
        registerReceiver(mUsbReceiver, filter);

        setContentView(R.layout.activity_game);
        scoreView = (TextView) findViewById(R.id.score);
        scoreView.setText(String.valueOf(score));

        grid = new int[COLUMN_COUNT][ROW_COUNT];
        initGrid();

        matrix = new byte[32*32*2+2];

        gridLayout = (GridLayout) findViewById(R.id.grid);
        gridLayout.setColumnCount(COLUMN_COUNT);
        gridLayout.setRowCount(ROW_COUNT);
        initGridLayoutWithViews();
        setGridViewWidths(VIEW_BLOCK_WIDTH);

        startGame();

//        updateMatrix();





//        HighScoresDataSource dataSource = HighScoresDataSource.getInstance(getApplicationContext());
//        dataSource.open();
//
//        dataSource.clearHighScores();
//        dataSource.createHighScore("Adam", 240);
//        dataSource.createHighScore("Adam", 60);
//        dataSource.createHighScore("Jesse", 140);
//        dataSource.createHighScore("Jesse", 180);
//        dataSource.createHighScore("A", 0);
//        dataSource.createHighScore("B", 0);
//        dataSource.createHighScore("C", 0);
//        dataSource.createHighScore("D", 0);
//        dataSource.createHighScore("E", 0);
//        dataSource.createHighScore("F", 0);

//        dataSource.close();

    }

    @Override
    protected void onPause()
    {
        super.onPause();
        pauseGame();

        closeAccessory();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        resumeGame();

        if (mInputStream != null && mOutputStream != null) {
            return;
        }

        UsbAccessory[] accessories = mUsbManager.getAccessoryList();
        UsbAccessory accessory = (accessories == null ? null : accessories[0]);
        if (accessory != null) {
            if (mUsbManager.hasPermission(accessory)) {
                openAccessory(accessory);
            } else {
                synchronized (mUsbReceiver) {
                    if (!mPermissionRequestPending) {
                        mUsbManager.requestPermission(accessory,mPermissionIntent);
                        mPermissionRequestPending = true;
                    }
                }
            }
        } else {
            Log.d(TAG, "mAccessory is null");
        }
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(mUsbReceiver);
        super.onDestroy();
    }

//    @Override
//    protected void onStop()
//    {
//        super.onStop();
//        finish();
//    }

    public void startGame()
    {
        next = generateRandomTetromino();
        placeNextTetromino();
        updateMatrix();
//        updateView();
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
        }, FALL_TIME, FALL_TIME);
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

    public void updateMatrix()
    {
        matrix[0] = COLUMN_COUNT;
        matrix[1] = ROW_COUNT;
        for (int j = 0; j < ROW_COUNT; j++)
        {
            for (int i = 0; i < COLUMN_COUNT; i++)
            {
                int color = RGBtoMatrixColor(gridColorToRGB(grid[i][j]));
                byte upper = (byte) (color >> 8);
                byte lower = (byte) (color);
                matrix[j*COLUMN_COUNT*2+i*2+2] = upper;
                matrix[j*COLUMN_COUNT*2+i*2+3] = lower;
//                System.out.printf("%x", upper);
//                System.out.println("");
//                System.out.printf("%x", lower);
//                System.out.println("");
            }
        }

        if(mOutputStream != null)
        {
            try {
                mOutputStream.write(matrix);
            } catch (IOException e) {
                Log.e(TAG, "write failed", e);
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
//                System.out.println(tag);
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

    private byte[] gridColorToRGB(int gridColor)
    {
        switch (gridColor)
        {
            /* TODO change case values to match possible LED matrix/grid values */
            case -1 : return new byte[] {0,0,0};
            case 0  : return new byte[] {0,6,0};
            case 1  : return new byte[] {6,0,0};
            case 2  : return new byte[] {0,0,6};
            case 3  : return new byte[] {3,3,0};
            case 4  : return new byte[] {2,0,4};
            default : return new byte[] {0,0,0};
        }
    }

    private int RGBtoMatrixColor(byte[] rgb)
    {
        return ((rgb[0] & 0x7) << 13) | ((rgb[0] & 0x6) << 10) |
                ((rgb[1] & 0x7) <<  8) | ((rgb[1] & 0x7) <<  5) |
                ((rgb[2] & 0x7) <<  2) | ((rgb[2] & 0x6) >>  1);
    }

    private void openAccessory(UsbAccessory accessory) {
        mFileDescriptor = mUsbManager.openAccessory(accessory);
        if (mFileDescriptor != null) {
            mAccessory = accessory;
            FileDescriptor fd = mFileDescriptor.getFileDescriptor();
            mInputStream = new FileInputStream(fd);
            mOutputStream = new FileOutputStream(fd);
            Log.d(TAG, "accessory opened");
        } else {
            Log.d(TAG, "accessory open fail");
        }
    }


    private void closeAccessory() {
        try {
            if (mFileDescriptor != null) {
                mFileDescriptor.close();
            }
        } catch (IOException e) {
        } finally {
            mFileDescriptor = null;
            mAccessory = null;
        }
    }

    public void rotateBlock(View view)
    {
        current.rotate();
        updateMatrix();
//        updateView();
    }

    public void moveBlockLeft(View view)
    {
        current.moveLeft();
        updateMatrix();
//        updateView();
    }

    public void moveBlockRight(View view)
    {
        current.moveRight();
        updateMatrix();
//        updateView();
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
        updateMatrix();
//        updateView();
    }
}