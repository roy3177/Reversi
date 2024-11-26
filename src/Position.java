/**
 * @Roy Meoded
 * @Noa Agassi
 * The position class represents a position on the game board,using 2 attributes:
 * row and col, to store the row and column of the position.
 */

import java.util.ArrayList;
import java.util.List;

public class Position {

    public static final int BOARD_SIZE = 8;

    private int row;
    private int col;


    //A constructor that accepts a row and a column and builds a new position
    public Position(int row, int col) {
        if(row < 0 || row > BOARD_SIZE || col < 0 || col > BOARD_SIZE){
            throw new IllegalArgumentException("The position is not in the board area");
        }
        this.row = row;
        this.col = col;
    }

    //copy constructor
    public Position(Position p){
        this.row = p.row;
        this.col = p.col;
    }

    // return the row of a position
    public int row() {
        return row;
    }

    //return thr column of the position
    public int col() {
        return col;
    }

    public int getRow(){
        return this.row;
    }
    public int getCol(){
        return col;
    }


    //Checks if two positions are the same
    public boolean equals(Object o){
        if (o instanceof Position b){
            return row()==b.row && col()==b.col;
        }
        return false;
    }


    public static String toString(int row,int col){
        return "("+ row +" ,"+ col+ ")";
    }


}