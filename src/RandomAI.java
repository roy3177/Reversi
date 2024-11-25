/**
 @author Roy Meoded
 @author Noa Agassi
This class represents an AI player that acts in a completely random way.
In each turn,the player randomly selects a legal move from a list of all
possible moves.
 */

import java.util.List;
import java.util.Random;

public class RandomAI extends AIPlayer {

    //Constructor-that indicate us if the player is p1 or p2:
    public RandomAI(boolean isPlayerOne) {
        super(isPlayerOne);
    }

    /*
    This method is responsible for selecting a random move from the list of
    legal moves in our game.
     */
    @Override
    public Move makeMove(PlayableLogic gameStatus) {
        //The list of the valid positions-with help of the ValidMoves:
        List<Position> ValidMoves = gameStatus.ValidMoves();
        if (ValidMoves.isEmpty()) {
            return null;
        }
        //Selects a random position from the list:
        Position chosenPos = ValidMoves.get(new Random().nextInt(ValidMoves.size()));
        //Checking who is the current player :
        Player currentPlayer = gameStatus.isFirstPlayerTurn() ? gameStatus.getFirstPlayer() : gameStatus.getSecondPlayer();

        //Creates a new disc that introduce the current player:
        Disc newDisc = new SimpleDisc(currentPlayer);

        //Return the move with the disc and position that we create:
        return new Move(chosenPos, newDisc);
    }
}


