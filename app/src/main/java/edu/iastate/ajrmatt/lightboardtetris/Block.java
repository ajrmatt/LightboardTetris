package edu.iastate.ajrmatt.lightboardtetris;

/**
 * Created by ajrmatt on 11/10/16.
 */

public class Block {

    private int x;
    private int y;

    private Tetromino parent;



    public Block(Tetromino theParent)
    {
        x = 0;
        y = 0;
        parent = theParent;
    }


}
