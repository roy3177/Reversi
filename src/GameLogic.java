/**
 @author Roy Meoded
 @author Noa Agassi

 The class GameLogic introduces the logic of the game board of the Reversi game.
 it manages the board,the moves,the turns of the players,the types of the special discs
 (like BombDisc and UnflippableDisc),and the end of the game....
 */

import java.util.*;

public class GameLogic implements PlayableLogic {


    /**
     * Our fields:
     */
    private Disc[][] board;
    private Player firstPlayer;
    private Player secondPlayer;
    private final int _BOARD_SIZE = 8;
    private List<Move> moveList; //list that include all the moves that was made
    private boolean isFirst = true;
    private Stack<List<Position>> flippedLastH;

    /**
     * Constructor:
     */
    public GameLogic() {
        moveList = new ArrayList<>();
        board = new Disc[_BOARD_SIZE][_BOARD_SIZE];
        flippedLastH = new Stack<>();
    }

    /*
     * This method checking if  the move is valid.
     * Manages the special discs(BombDisc and UnflippableDisc)
     * Locates disc and updates the discs that got flipped.
     * Flips the turn between the players.
     * ++ Using the internal methods: isMoveValid , placeDisc
     */
    @Override
    public boolean locate_disc(Position a, Disc disc) {

        //If the move is illegal->the method not continue:
        if (!isMoveValid(a)) {
            return false;
        }
        //Checking which player's turn+give us the number of his bomb discs,and the number of the unflippable discs:
        Player currentPlayer = isFirstPlayerTurn() ? firstPlayer : secondPlayer;
        int numOfBomb = currentPlayer.getNumber_of_bombs();
        int numberOfUnflippedable = currentPlayer.getNumber_of_unflippedable();

        if (disc instanceof BombDisc) {
            //If the player does not have bomb discs-> the move is illegal:
            if (numOfBomb == 0) {

                return false;
            }
            //If he has bomb discs->reduce one of them:
            currentPlayer.reduce_bomb();
        }

        if (disc instanceof UnflippableDisc) {
            //If the player does not have unflippable discs-> the move is illegal:
            if (numberOfUnflippedable == 0) {

                return false;
            }
            //If he has unflippable discs->reduce one of them:
            currentPlayer.reduce_unflippedable();
        }

        placeDisc(a, disc);
        getStatus(currentPlayer,a,disc);


        /**
         * Calling to the internal method flipsOnThisTurn,that returns a list of
         * the positions that need to be flipped for the particular move.
         * If the list is not empty->every disc in the list will flip by the
         * internal method flipOne.
         * The list is being pushed to the stack flippedLastH, to save what been
         * flipped in the current turn->For future cancelling activities.
         */
        List<Position> flipsOnThisTurn = getFlippedDiscs(a, currentPlayer);

        if (!flipsOnThisTurn.isEmpty()) {
            for (Position p : flipsOnThisTurn) { // do flips
                flipOne(p);

                //print
                String player = "";
                if (currentPlayer == firstPlayer) { player = "Player 1";}
                else{
                    player = "Player 2";
                }
                System.out.println(player+" flipped the "+getDiscAtPosition(p).getType()+" in ("+p.row() +","+ p.col()+")");
            }
            System.out.println();

            flippedLastH.push(flipsOnThisTurn); // save flips
        }
        moveList.add(new Move(a, disc));

        //Move the turn to the second player:
        isFirst = !isFirst;

        //Return true if all the move's levels has been succeeded.
        return true;
    }

    private void getStatus(Player currentPlayer, Position p, Disc d) {
        String player = "";
        if (currentPlayer == firstPlayer) {
            player = "Player 1";
        }
        else{
            player = "Player 2";
        }
        System.out.println(player +" placed a "+ getDiscAtPosition(p).getType()+ " in ("+p.row()+", "+p.col() +")");

    }



    /**
     * This internal method flips particular disc on the board.
     * It changes the disc's owner.
     *
     */
    private void flipOne(Position p) {

        //Checking if the position is inside the board's limits:
        if (isInBounds(p.row(), p.col())) {
            Disc d = board[p.row()][p.col()];
            if (d != null && d.getOwner() != null)
//                System.out.println(" Disc belongs to " + (d.getOwner().isPlayerOne ? "player 1" : "player 2"));
                //If there is a disc in this position,and its belong to particular player->change the disc's owner to the second player:
                d.setOwner(d.getOwner().isPlayerOne ? getSecondPlayer() : getFirstPlayer());
//            System.out.println(" Disc now belongs to " + (d.getOwner().isPlayerOne ? "player 1" : "player 2"));
        }
    }

    /**
     *
     * This method returns the disc of particular position
     *
     */
    @Override
    public Disc getDiscAtPosition(Position position) {
        int row = position.row();
        int col = position.col();
        return board[row][col];
    }

    /**
     *This method returns the size of the board.
     */
    @Override
    public int getBoardSize() {
        return _BOARD_SIZE;
    }

    /**
     * This method returns a list of valid moves for the current player.
     * ++ Using the internal methods : isMoveValid
     */
    @Override
    public List<Position> ValidMoves() {
        List<Position> validMoves = new ArrayList<>();
        for (int row = 0; row < _BOARD_SIZE; row++) {
            for (int col = 0; col < _BOARD_SIZE; col++) {
                Position pos = new Position(row, col);
                //If the move is valid->add that to the list:
                if (isMoveValid(pos)) {
                    validMoves.add(pos);
                }
            }
        }
        return validMoves;
    }

    /**
     *This method returns how many discs will flip for a valid move of the
     * current player.
     * ++ Using the internal method getFlippedDiscs.
     */
    @Override
    public int countFlips(Position a) {
        return getFlippedDiscs(a, isFirstPlayerTurn() ? getFirstPlayer() : getSecondPlayer()).size();
    }

    /*
     *
     * This internal method identifies the discs that get flipped for a particular move.
     * This method also treats the special discs.
     * ++ Using the internal method flipsInDirection.
     * ++Using the method getDiscAtPosition.
     */
    private List<Position> getFlippedDiscs(Position pos, Player currentPlayer) {
        //Directions of the neighbors in the coordinates:
        int[] neiX = {-1, -1, -1, 0, 1, 1, 1, 0};
        int[] neiY = {-1, 0, 1, 1, 1, 0, -1, -1};

        //The list of all the positions of the discs that get flipped:
        List<Position> flipsOnThisTurn = new ArrayList<>();

        // get discs flipped on lines
        for (int i = 0; i < neiX.length; i++)
            flipInDirection(pos, neiX[i], neiY[i], currentPlayer, flipsOnThisTurn);//test all the position to flip

        for (int j = 0; j < flipsOnThisTurn.size(); j++) {
            Position p = flipsOnThisTurn.get(j);

            if (getDiscAtPosition(p) instanceof BombDisc) {
                // add neighbours to end of list
                for (int i = 0; i < neiX.length; i++) {

                    if (isInBounds(p.row() + neiX[i], p.col() + neiY[i])) {
                        Position neighbour = new Position(p.row() + neiX[i], p.col() + neiY[i]);
                        Disc d = getDiscAtPosition(neighbour);
                        if (d != null && !currentPlayer.equals(d.getOwner()) && !flipsOnThisTurn.contains(neighbour))
                            flipsOnThisTurn.addLast(neighbour);
                    }
                }
            }
        }
        return flipsOnThisTurn;
    }

    /**
     *
     *This internal method is looking for discs that got flipped in particular direction.
     */
    private void flipInDirection(Position pos, int dx, int dy, Player currentPlayer, List<Position> flippedDiscsInThisMove) {
        int x = pos.row() + dx, y = pos.col() + dy;
        List<Position> discsToFlip = new ArrayList<>();

        //Add the discs that need to be flipped:
        while (isInBounds(x, y) && board[x][y] != null && board[x][y].getOwner() != currentPlayer) {
            discsToFlip.add(new Position(x, y));
            x += dx;
            y += dy;
        }

        //Checks if its possible to flip the discs if there is a disc to the current player in the edge:
        if (isInBounds(x, y) && board[x][y] != null && board[x][y].getOwner() == currentPlayer) {
            for (Position p : discsToFlip)
                if (!(board[p.row()][p.col()] instanceof UnflippableDisc))
                    flippedDiscsInThisMove.add(p); // שמירת המהלך להיסטוריה
        }
    }

    /**
     *
     * This method returns if it is the first player's turn.
     */
    @Override
    public boolean isFirstPlayerTurn() {
        return isFirst;
    }

    /**
     *
     * This method returns the first player.
     */
    @Override
    public Player getFirstPlayer() {
        return firstPlayer;
    }

    /**
     * This method returns the second player.
     */
    @Override
    public Player getSecondPlayer() {
        return secondPlayer;
    }

    /**
     *
     *This method initializes the players
     */
    @Override
    public void setPlayers(Player player1, Player player2) {
        firstPlayer = player1;
        secondPlayer = player2;
    }

    /**
     *
     *This method checks if the games is finished.
     *We know that if the board is full of discs, or there is no valid moves.
     * If the game is finished-the method declares the winner!
     * ++ Using the internal method declareWinner
     * ++Using the method ValidMoves.
     */
    @Override
    public boolean isGameFinished() {
        //If there is no valid moves to do->the game if over:
        if (ValidMoves().isEmpty()) {
            declareWinner();
            return true;
        } else {
            for (int row = 0; row < _BOARD_SIZE; row++) {
                for (int col = 0; col < _BOARD_SIZE; col++) {
                    if (board[row][col] == null) {
                        return false;
                    }
                }
            }

        }
        declareWinner();
        return true;
    }

    /**
     * This internal method counts the discs of each player.
     * At the end-the method declares the winner, or there is a draw.
     */
    private void declareWinner() {
        //Our counters for the players:
        int firstPlayerCount = 0;
        int secondPlayerCount = 0;

        for (int row = 0; row < _BOARD_SIZE; row++) {
            for (int col = 0; col < _BOARD_SIZE; col++) {
                Disc disc = board[row][col];
                if (disc != null) {
                    if (disc.getOwner() == firstPlayer) {
                        firstPlayerCount++;
                    } else if (disc.getOwner() == secondPlayer) {
                        secondPlayerCount++;
                    }
                }
            }
        }
        //The one that the count is bigger->he is the winner:
        if (firstPlayerCount > secondPlayerCount) {
            firstPlayer.addWin();
            System.out.println("Player 1 wins with  " + firstPlayerCount + " discs! " + "Player 2 had " + secondPlayerCount);
        } else if (secondPlayerCount > firstPlayerCount) {
            secondPlayer.addWin();
            System.out.println("Player 2 wins with  " + secondPlayerCount + " discs! " + "Player 1 had " + firstPlayerCount);
        } else {
            System.out.println("It's a tie ! both players have " + firstPlayerCount + "discs!");
        }
    }

    /**
     * This internal method initializes the board to the initial situation of the game
     */
    private void intializeBoard() {
        if (firstPlayer == null || secondPlayer == null) {
            throw new IllegalStateException("Players must be set before intialize board");
        }
        //Make the start game:
        int mid = _BOARD_SIZE / 2;
        board[mid - 1][mid - 1] = new SimpleDisc(firstPlayer);
        board[mid][mid] = new SimpleDisc(firstPlayer);
        board[mid - 1][mid] = new SimpleDisc(secondPlayer);
        board[mid][mid - 1] = new SimpleDisc(secondPlayer);

    }

    /**
     * This method resets the game to the initial situation.
     * ++Using the internal method intializeBoard
     */
    @Override
    public void reset() {
        //Rebooting the board:
        for (int i = 0; i < _BOARD_SIZE; i++) {
            for (int j = 0; j < _BOARD_SIZE; j++) {
                board[i][j] = null;
            }
        }


        //Initializing the board to the starting game:
        intializeBoard();

        moveList.clear();


        //Reset the types of the discs for each player:
        firstPlayer.reset_bombs_and_unflippedable();
        secondPlayer.reset_bombs_and_unflippedable();


        flippedLastH.clear();

    }

    /**
     * This method cancels the last move ,includes restoring the previous state of
     * the discs.
     *
     */
    @Override
    public void undoLastMove() {
        // remove the last move
        if (!moveList.isEmpty()) {
            Move lastMove = moveList.removeLast();


            //Deletes the disc that in the position of the last move:
            board[lastMove.position().row()][lastMove.position().col()] = null;
            System.out.println("Undoing last move: \n"+
                    "    Undo: removing "+ lastMove.disc().getType() + " from ("+lastMove.position().row()+ ", "+ lastMove.position().col()+")");

            //Retrieve the list of the positions of the discs that got flipped in the last move:
            List<Position> lastF = flippedLastH.pop();
            for (Position p : lastF) {
                //Flip the disc to the previous status:
                flipOne(p);
                System.out.println("    Undo: flipping back "+ getDiscAtPosition(p).getType()+ " in ("+ p.row()+ ", "+ p.col()+")");
            }
            System.out.println();
            //If the last move include type of bomb disc:
            if (lastMove.disc() instanceof BombDisc) {
                //Return the bomb disc to their player
                lastMove.disc().getOwner().increas_bomb();


            }
            //If the last move include type of unflippable disc:
            if (lastMove.disc() instanceof UnflippableDisc) {
                //Return the unflippable disc to their player
                lastMove.disc().getOwner().increas_unflippedable();

            }
            //Swithcing the turn's player:
            isFirst = !isFirst;

        }
        else{
            System.out.println(" No moves to undo ");
        }


    }

    /**
     * This internal method checks if the move is valid
     */
    private boolean isMoveValid(Position pos) {
        return isInBounds(pos.row(), pos.col()) && board[pos.row()][pos.col()] == null && countFlips(pos) > 0;
    }

    /**
     *
     * This internal method checks if the position is inside the board's borders.
     */
    private boolean isInBounds(int row, int col) {
        return row >= 0 && row < _BOARD_SIZE && col >= 0 && col < _BOARD_SIZE;

    }

    /**
     *This internal method places a disc in particular position.
     */
    private void placeDisc(Position p, Disc disc) {
        if (disc == null || disc.getOwner() == null) {
            throw new IllegalArgumentException("dddddd");
        }
        board[p.row()][p.col()] = disc;
    }
}