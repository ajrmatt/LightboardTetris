package edu.iastate.ajrmatt.lightboardtetris;

import java.util.Random;

import static edu.iastate.ajrmatt.lightboardtetris.GameActivity.BLANK;

/**
 * Created by ajrmatt on 11/10/16.
 */

abstract class Tetromino
{

    private int[][] grid;
    private int[] center;
    private int[][] location;
    Rotation[] rotations;
    private int currentRotation;
    private boolean isSet;
    private int color;
    private String viewColor;

    Tetromino(int[][] theGrid)
    {
        grid = theGrid;
        isSet = false;
        color = randomColor();
    }

    boolean placeInGrid(int x, int y, int rotation)
    {

        int[][] desiredLocation = rotations[currentRotation].position(x, y);
        if (positionIsValid(desiredLocation))
        {
            center = new int[] {x, y};
            currentRotation = rotation;
            location = desiredLocation;

            for (int i = 0; i < location.length; i++)
            {
                grid[location[i][0]][location[i][1]] = color;
            }
            return true;
        }
        return false;
    }

    void rotate()
    {
        int nextRotation = (currentRotation == rotations.length - 1) ? 0 : currentRotation + 1;
        int[][] newLocation = rotations[nextRotation].position(center[0], center[1]);

        for (int i = 0; i < newLocation.length; i++)
        {
            grid[location[i][0]][location[i][1]] = BLANK;
        }

        if (positionIsValid(newLocation))
        {
            currentRotation = nextRotation;
            location = newLocation;
        }

        for (int i = 0; i < location.length; i++)
        {
            grid[location[i][0]][location[i][1]] = color;
        }
    }

    boolean moveDown()
    {
        int[][] newLocation = copyLocationOf(location);
        boolean stillMovingDown = false;

        for (int i = 0; i < newLocation.length; i++)
        {
            newLocation[i][1]++;
            grid[location[i][0]][location[i][1]] = BLANK;
        }

        if (positionIsValid(newLocation))
        {
            location = newLocation;
            center[1]++;
            stillMovingDown = true;
        }

        for (int i = 0; i < location.length; i++)
        {
            grid[location[i][0]][location[i][1]] = color;
        }

        return stillMovingDown;
    }

    void moveLeft()
    {
        int[][] newLocation = copyLocationOf(location);

        for (int i = 0; i < newLocation.length; i++)
        {
            newLocation[i][0]--;
            grid[location[i][0]][location[i][1]] = BLANK;
        }

        if (positionIsValid(newLocation))
        {
            location = newLocation;
            center[0]--;
        }

        for (int i = 0; i < location.length; i++)
        {
            grid[location[i][0]][location[i][1]] = color;
        }
    }

    void moveRight()
    {
        int[][] newLocation = copyLocationOf(location);

        for (int i = 0; i < newLocation.length; i++)
        {
            newLocation[i][0]++;
            grid[location[i][0]][location[i][1]] = BLANK;
        }

        if (positionIsValid(newLocation))
        {
            location = newLocation;
            center[0]++;
        }

        for (int i = 0; i < location.length; i++)
        {
            grid[location[i][0]][location[i][1]] = color;
        }
    }

    public boolean containsBlock(int x, int y)
    {
        for (int i = 0; i < location.length; i++)
        {
            if (location[i][0] == x && location[i][1] == y) return true;
        }
        return false;
    }

    private int randomColor()
    {
//        int color =
        return new Random().nextInt(5);
//        switch (color)
//        {
//            case 0 : return 0;
//            case 1 : return 1;
//            case 2 : return 2;
//            case 3 : return 3;
//            case 4 : return 4;
//            default: return -1;
//        }
    }

    private boolean positionIsValid(int[][] location)
    {
        for (int x = 0; x < location.length; x++)
        {
            if (location[x][0] < 0 || location[x][0] >= grid.length ||
                    location[x][1] < 0 || location[x][1] >= grid[0].length)
            {
                return false;
            }
            if (grid[location[x][0]][location[x][1]] > BLANK)
            {
                return false;
            }
        }
        return true;
    }

    private int[][] copyLocationOf(int[][] location)
    {
        int[][] copy = new int[location.length][];
        for(int i = 0; i < location.length; i++)
        {
            copy[i] = location[i].clone();
        }
        return copy;
    }


    /**** GETTERS AND SETTERS ****/

    public boolean isSet() {
        return isSet;
    }

    void setSet(boolean set) {
        isSet = set;
    }

    public int[][] getLocation() {
        return location;
    }

    public void setLocation(int[][] location) {
        this.location = location;
    }

    public int[] getCenter() {
        return center;
    }

    public void setCenter(int[] center) {
        this.center = center;
    }

    public int getCurrentRotation() {
        return currentRotation;
    }

    public void setCurrentRotation(int currentRotation) {
        this.currentRotation = currentRotation;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getViewColor() {
        return viewColor;
    }

    public void setViewColor(String viewColor) {
        this.viewColor = viewColor;
    }


}

class O extends Tetromino
{
    O(int[][] theGrid)
    {
        super(theGrid);
        rotations = new Rotation[] {
                new Rotation() {
                    /*
                     *  00[][]
                     *  00{}[]
                     *  000000
                     */
                    @Override
                    public int[][] position(int x, int y) {
                        return new int[][]{
                                {x, y},
                                {x, y - 1},
                                {x + 1, y - 1},
                                {x + 1, y},
                        };
                    }
                }
        };
    }
}

class I extends Tetromino
{

    I(int[][] theGrid)
    {
        super(theGrid);
        rotations = new Rotation[] {
                new Rotation() {
                    /*
                     *  00000000
                     *  00000000
                     *  []{}[][]
                     *  00000000
                     */
                    @Override
                    public int[][] position(int x, int y) {
                        return new int[][] {
                                {x, y},
                                {x + 1, y},
                                {x + 2, y},
                                {x - 1, y},
                        };
                    }
                },
                new Rotation() {
                    /*
                     *  00[]0000
                     *  00[]0000
                     *  00{}0000
                     *  00[]0000
                     */
                    @Override
                    public int[][] position(int x, int y) {
                        return new int[][] {
                                {x, y},
                                {x, y - 1},
                                {x, y - 2},
                                {x, y + 1},
                        };
                    }
                },
                new Rotation() {
                    /*
                     *  00000000
                     *  [][][][]
                     *  00CC0000
                     *  00000000
                     */
                    @Override
                    public int[][] position(int x, int y) {
                        return new int[][] {
                                {x, y - 1},
                                {x + 1, y - 1},
                                {x + 2, y - 1},
                                {x - 1, y - 1},
                        };
                    }
                },
                new Rotation() {
                    /*
                     *  0000[]00
                     *  0000[]00
                     *  00CC[]00
                     *  0000[]00
                     */
                    @Override
                    public int[][] position(int x, int y) {
                        return new int[][] {
                                {x + 1, y - 2},
                                {x + 1, y - 1},
                                {x + 1, y},
                                {x + 1, y + 1},
                        };
                    }
                },
        };
    }
}

class T extends Tetromino
{

    T(int[][] theGrid)
    {
        super(theGrid);
        rotations = new Rotation[] {
                new Rotation() {
                    /*
                     *  00[]00
                     *  []{}[]
                     *  000000
                     */
                    @Override
                    public int[][] position(int x, int y) {
                        return new int[][] {
                                {x, y},
                                {x, y - 1},
                                {x + 1, y},
                                {x - 1, y},
                        };
                    }
                },
                new Rotation() {
                    /*
                     *  00[]00
                     *  00{}[]
                     *  00[]00
                     */
                    @Override
                    public int[][] position(int x, int y) {
                        return new int[][] {
                                {x, y},
                                {x, y - 1},
                                {x + 1, y},
                                {x, y + 1},
                        };
                    }
                },
                new Rotation() {
                    /*
                     *  000000
                     *  []{}[]
                     *  00[]00
                     */
                    @Override
                    public int[][] position(int x, int y) {
                        return new int[][] {
                                {x, y},
                                {x + 1, y},
                                {x, y + 1},
                                {x - 1, y},
                        };
                    }
                },
                new Rotation() {
                    /*
                     *  00[]00
                     *  []{}00
                     *  00[]00
                     */
                    @Override
                    public int[][] position(int x, int y) {
                        return new int[][] {
                                {x, y},
                                {x, y - 1},
                                {x, y + 1},
                                {x - 1, y},
                        };
                    }
                },
        };
    }
}

class J extends Tetromino
{

    J(int[][] theGrid)
    {
        super(theGrid);
        rotations = new Rotation[]{
                new Rotation() {
                    /*
                     *  []0000
                     *  []{}[]
                     *  000000
                     */
                    @Override
                    public int[][] position(int x, int y) {
                        return new int[][]{
                                {x, y},
                                {x + 1, y},
                                {x - 1, y},
                                {x - 1, y - 1},
                        };
                    }
                },
                new Rotation() {
                    /*
                     *  00[][]
                     *  00{}00
                     *  00[]00
                     */
                    @Override
                    public int[][] position(int x, int y) {
                        return new int[][]{
                                {x, y},
                                {x, y - 1},
                                {x + 1, y - 1},
                                {x, y + 1},
                        };
                    }
                },
                new Rotation() {
                    /*
                     *  000000
                     *  []{}[]
                     *  0000[]
                     */
                    @Override
                    public int[][] position(int x, int y) {
                        return new int[][]{
                                {x, y},
                                {x + 1, y},
                                {x + 1, y + 1},
                                {x - 1, y},
                        };
                    }
                },
                new Rotation() {
                    /*
                     *  00[]00
                     *  00{}00
                     *  [][]00
                     */
                    @Override
                    public int[][] position(int x, int y) {
                        return new int[][]{
                                {x, y},
                                {x, y - 1},
                                {x, y + 1},
                                {x - 1, y + 1},
                        };
                    }
                },
        };
    }
}

class L extends Tetromino
{

    L(int[][] theGrid)
    {
        super(theGrid);
        rotations = new Rotation[]{
                new Rotation() {
                    /*
                     *  0000[]
                     *  []{}[]
                     *  000000
                     */
                    @Override
                    public int[][] position(int x, int y) {
                        return new int[][]{
                                {x, y},
                                {x + 1, y - 1},
                                {x + 1, y},
                                {x - 1, y},
                        };
                    }
                },
                new Rotation() {
                    /*
                     *  00[]00
                     *  00{}00
                     *  00[][]
                     */
                    @Override
                    public int[][] position(int x, int y) {
                        return new int[][]{
                                {x, y},
                                {x, y - 1},
                                {x + 1, y + 1},
                                {x, y + 1},
                        };
                    }
                },
                new Rotation() {
                    /*
                     *  000000
                     *  []{}[]
                     *  []0000
                     */
                    @Override
                    public int[][] position(int x, int y) {
                        return new int[][]{
                                {x, y},
                                {x + 1, y},
                                {x - 1, y + 1},
                                {x - 1, y},
                        };
                    }
                },
                new Rotation() {
                    /*
                     *  [][]00
                     *  00{}00
                     *  00[]00
                     */
                    @Override
                    public int[][] position(int x, int y) {
                        return new int[][]{
                                {x, y},
                                {x, y - 1},
                                {x, y + 1},
                                {x - 1, y - 1},
                        };
                    }
                },
        };
    }
}

class S extends Tetromino
{

    S(int[][] theGrid)
    {
        super(theGrid);
        rotations = new Rotation[]{
                new Rotation() {
                    /*
                     *  00[][]
                     *  []{}00
                     *  000000
                     */
                    @Override
                    public int[][] position(int x, int y) {
                        return new int[][]{
                                {x, y},
                                {x, y - 1},
                                {x + 1, y - 1},
                                {x - 1, y},
                        };
                    }
                },
                new Rotation() {
                    /*
                     *  00[]00
                     *  00{}[]
                     *  0000[]
                     */
                    @Override
                    public int[][] position(int x, int y) {
                        return new int[][]{
                                {x, y},
                                {x, y - 1},
                                {x + 1, y},
                                {x + 1, y + 1},
                        };
                    }
                },
                new Rotation() {
                    /*
                     *  000000
                     *  00{}[]
                     *  [][]00
                     */
                    @Override
                    public int[][] position(int x, int y) {
                        return new int[][]{
                                {x, y},
                                {x + 1, y},
                                {x, y + 1},
                                {x - 1, y + 1},
                        };
                    }
                },
                new Rotation() {
                    /*
                     *  []0000
                     *  []{}00
                     *  00[]00
                     */
                    @Override
                    public int[][] position(int x, int y) {
                        return new int[][]{
                                {x, y},
                                {x, y + 1},
                                {x - 1, y},
                                {x - 1, y - 1},
                        };
                    }
                },
        };
    }
}

class Z extends Tetromino
{

    Z(int[][] theGrid)
    {
        super(theGrid);
        rotations = new Rotation[]{
                new Rotation() {
                    /*
                     *  [][]00
                     *  00{}[]
                     *  000000
                     */
                    @Override
                    public int[][] position(int x, int y) {
                        return new int[][]{
                                {x, y},
                                {x, y - 1},
                                {x + 1, y},
                                {x - 1, y - 1},
                        };
                    }
                },
                new Rotation() {
                    /*
                     *  0000[]
                     *  00{}[]
                     *  00[]00
                     */
                    @Override
                    public int[][] position(int x, int y) {
                        return new int[][]{
                                {x, y},
                                {x + 1, y - 1},
                                {x + 1, y},
                                {x, y + 1},
                        };
                    }
                },
                new Rotation() {
                    /*
                     *  000000
                     *  []{}00
                     *  00[][]
                     */
                    @Override
                    public int[][] position(int x, int y) {
                        return new int[][]{
                                {x, y},
                                {x + 1, y + 1},
                                {x, y + 1},
                                {x - 1, y},
                        };
                    }
                },
                new Rotation() {
                    /*
                     *  00[]00
                     *  []{}00
                     *  []0000
                     */
                    @Override
                    public int[][] position(int x, int y) {
                        return new int[][]{
                                {x, y},
                                {x, y - 1},
                                {x - 1, y + 1},
                                {x - 1, y},
                        };
                    }
                },
        };
    }
}