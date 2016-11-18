package edu.iastate.ajrmatt.lightboardtetris;

/**
 * Created by ajrmatt on 11/10/16.
 */

public abstract class Tetromino
{

    protected int[][] grid;
    protected int[] center;
    protected int[][] location;
    protected Rotation[] rotations;
    protected int currentRotation;
    protected int color;

    public Tetromino(int[][] theGrid, int theX, int theY)
    {
        grid = theGrid;
        center = new int[] {theX, theY};
    }

    public void rotate()
    {
        int[][] newLocation = location;

        int nextRotation = (currentRotation == rotations.length - 1) ? 0 : currentRotation + 1;
        newLocation = rotations[nextRotation].position(center[0], center[1]);

        for (int i = 0; i < newLocation.length; i++)
        {
            grid[location[i][0]][location[i][1]] = -1;
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

    public boolean moveDown()
    {
        int[][] newLocation = copyLocationOf(location);
        boolean stillMovingDown = false;

        for (int i = 0; i < newLocation.length; i++)
        {
            newLocation[i][1]++;
            grid[location[i][0]][location[i][1]] = -1;
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

    public void moveLeft()
    {
        int[][] newLocation = copyLocationOf(location);

        for (int i = 0; i < newLocation.length; i++)
        {
            newLocation[i][0]--;
            grid[location[i][0]][location[i][1]] = -1;
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

    public void moveRight()
    {
        int[][] newLocation = copyLocationOf(location);

        for (int i = 0; i < newLocation.length; i++)
        {
            newLocation[i][0]++;
            grid[location[i][0]][location[i][1]] = -1;
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

    private boolean positionIsValid(int[][] location)
    {
        for (int x = 0; x < location.length; x++)
        {
            if (location[x][0] < 0 || location[x][0] >= grid.length ||
                    location[x][1] < 0 || location[x][1] >= grid[0].length)
            {
                return false;
            }
            if (grid[location[x][0]][location[x][1]] > -1)
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
}

class O extends Tetromino
{
    public O(int[][] theGrid, int theX, int theY, int theRotation)
    {
        super(theGrid, theX, theY);
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
        location = rotations[theRotation].position(theX, theY);
        currentRotation = theRotation;

        for (int i = 0; i < location.length; i++)
        {
            grid[location[i][0]][location[i][1]] = color;
        }

    }
}

class I extends Tetromino
{

    public I(int[][] theGrid, int theX, int theY, int theRotation)
    {
        super(theGrid, theX, theY);
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
        currentRotation = theRotation;
        location = rotations[currentRotation].position(theX, theY);

        for (int i = 0; i < location.length; i++)
        {
            grid[location[i][0]][location[i][1]] = color;
        }
    }
}

class T extends Tetromino
{

    public T(int[][] theGrid, int theX, int theY, int theRotation)
    {
        super(theGrid, theX, theY);
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
        location = rotations[theRotation].position(theX, theY);
        currentRotation = theRotation;

        for (int i = 0; i < location.length; i++)
        {
            grid[location[i][0]][location[i][1]] = color;
        }
    }
}

class J extends Tetromino
{

    public J(int[][] theGrid, int theX, int theY, int theRotation)
    {
        super(theGrid, theX, theY);
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
        location = rotations[theRotation].position(theX, theY);
        currentRotation = theRotation;

        for (int i = 0; i < location.length; i++)
        {
            grid[location[i][0]][location[i][1]] = color;
        }
    }
}

class L extends Tetromino
{

    public L(int[][] theGrid, int theX, int theY, int theRotation)
    {
        super(theGrid, theX, theY);
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
        location = rotations[theRotation].position(theX, theY);
        currentRotation = theRotation;

        for (int i = 0; i < location.length; i++)
        {
            grid[location[i][0]][location[i][1]] = color;
        }
    }
}

class S extends Tetromino
{

    public S(int[][] theGrid, int theX, int theY, int theRotation)
    {
        super(theGrid, theX, theY);
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
        location = rotations[theRotation].position(theX, theY);
        currentRotation = theRotation;

        for (int i = 0; i < location.length; i++)
        {
            grid[location[i][0]][location[i][1]] = color;
        }
    }
}

class Z extends Tetromino
{

    public Z(int[][] theGrid, int theX, int theY, int theRotation)
    {
        super(theGrid, theX, theY);
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
        location = rotations[theRotation].position(theX, theY);
        currentRotation = theRotation;

        for (int i = 0; i < location.length; i++)
        {
            grid[location[i][0]][location[i][1]] = color;
        }
    }
}