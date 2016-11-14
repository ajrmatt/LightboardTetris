package edu.iastate.ajrmatt.lightboardtetris;

/**
 * Created by ajrmatt on 11/10/16.
 */

public class Tetromino
{
    public enum TetriminoType
    {
        O, I, T, J, L, S, Z
    }

    private TetriminoType type;

    public Tetromino(TetriminoType theType)
    {
        type = theType;
    }
}
