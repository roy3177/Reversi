/**
 * @author Roy Meoded
 * @author Noa Agassi
 * This disc represents bomb disc in the Reversi game.
 * This class,like the others, inherits from the abstract class Disc.
 * When this disc is flipping->it flipps all the discs from all his directions.(8 directions).
 * This disc is limited to 3 discs per player.
 * This method alsoo fulfill the method getType, and returns a special unique "💣".
 *
 */



public class BombDisc extends Disc{

    public BombDisc(Player owner) {
        super(owner);
    }

    @Override
    public String getType() {
        return "💣";
    }

    @Override
    public boolean isFlippable() {
        return false;
    }
}