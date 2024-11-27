/**
 @author Roy Meoded
 @author Noa Agassi
 This class represents a greedy AI player for our game.
 The greedy player will always choose the move that will lead to maximum flipping
 of the opponent's discs.
 If there are several moves that flip the same amount of discs:
 the player will choose the rightmost position,and if there are several:
 the lowest position.
 */

import java.util.List;

public class GreedyAI extends AIPlayer{

    //Constructor-that indicate us if the player is p1 or p2:
    public GreedyAI (boolean isPlayerOne) {
        super(isPlayerOne);
    }

    /*
    This method is responsible for selecting a greedy move from the list of
    legal moves in our game.
     */
    @Override
    public Move makeMove(PlayableLogic gameStatus) {

        //The list of the valid positions-with help of the ValidMoves:
        List<Position> ValidPosition = gameStatus.ValidMoves();
        if (ValidPosition.isEmpty()) {
            return null;
        }

//        //Indicate who is the current player: p1 or p2+create a new disc to the current player:
//        Player currentPlayer = gameStatus.isFirstPlayerTurn() ? gameStatus.getFirstPlayer() : gameStatus.getSecondPlayer();
//        Disc newDisc = new SimpleDisc(currentPlayer);

        //Find the best move:

        Position bestPos = null;
        int maxF = -1;
        boolean haveBombDisc = false;

        /*
        Goes through all legal moves and computes how many  opponent's discs
        can be flipped for each position.
        Using the countFlips method.
        If the number of the flips of the current position is greater than the
        previous value->update the maximum flips(maxF) and the best position(bestPos).
         */
        for (Position pos : ValidPosition) {
            int flips = gameStatus.countFlips(pos);

            if (flips > maxF) {
                maxF = flips;
                bestPos = pos;
            }
            if (gameStatus.countFlips(pos) > 3 && getNumber_of_bombs() > 0) {
                haveBombDisc = true;
            }
        }

        Disc newDisc;
        if (haveBombDisc){
            newDisc = new BombDisc(this);
        } else if (number_of_unflippedable>0) {
            newDisc = new UnflippableDisc(this);
        }
        else {
            newDisc = new SimpleDisc(this);
        }


        return new Move(bestPos,newDisc);
    }

}