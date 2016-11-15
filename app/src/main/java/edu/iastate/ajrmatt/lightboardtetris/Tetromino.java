package edu.iastate.ajrmatt.lightboardtetris;

/**
 * Created by ajrmatt on 11/10/16.
 */

public abstract class Tetromino
{

    protected int[][] location;
    protected Rotation[] rotations;
    protected int currentRotation;

    public Tetromino()
    {
        rotations = new Rotation[4];
    }

    abstract public void rotate();

}

class O extends Tetromino
{
    public O(int theX, int theY)
    {
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

    public void rotate()
    {

    }
}

class I extends Tetromino
{

    public I(int theX, int theY, int theRotation)
    {
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
        location = rotations[theRotation].position(theX, theY);
    }

    public void rotate()
    {

    }
}

class T extends Tetromino
{

    public T(int theX, int theY, int theRotation)
    {
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
    }

    public void rotate()
    {

    }
}

class J extends Tetromino
{

    public J(int theX, int theY, int theRotation)
    {
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
    }
    public void rotate()
    {

    }
}

class L extends Tetromino
{

    public L(int theX, int theY, int theRotation)
    {
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
    }
    public void rotate()
    {

    }
}

class S extends Tetromino
{

    public S(int theX, int theY, int theRotation)
    {
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
    }
    public void rotate()
    {

    }
}

class Z extends Tetromino
{

    public Z(int theX, int theY, int theRotation)
    {
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
    }
    public void rotate()
    {

    }
}