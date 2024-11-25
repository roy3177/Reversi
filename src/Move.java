/**
 @author Roy Meoded
 @author Noa Agassi
 This class is designed to represent a move int the Reversi game.
Each move includes a position on the board and a disc in the same position.
The class allows us to follow every move that made during the game,including
the possibility to check if two moves are equal.
 */

import java.util.List;
import java.util.Stack;


public class Move {
    private Position position;
    private Disc disc;


    // Constructor:
    public Move(Position position, Disc disc) {
        this.position = position;
        this.disc = disc;  // Initialize disc here

    }

    // Getter for position
    public Position position() {
        return position;
    }


    // Getter for disc
    public Disc disc() {
        return disc;
    }

    // Comparing between to objects of move type, to check if they are equals
    @Override
    public boolean equals(Object other) {
        if (other instanceof Move) { //Checks if the other object is a move type.
            Move otherMove = (Move) other;
            return position.equals(otherMove.position()) && disc.equals(otherMove.disc());
        }
        return false;
    }
}