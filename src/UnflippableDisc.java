/**
 * @author Roy Meoded
 * @author Noa Agassi
 * This class represents a special disc in the game, which cannot be turned over after
 * it is found on the board.
 * The class, like the SimpleDisc, inherits from the abstract class Disc,and fulfill
 * the method getType,and return a unique "⭕".
 * This disc is limited to 2 discs per player-->this ensures that players cannot
 * overuse the advantage of this type of disc.
 */

public class UnflippableDisc extends Disc {

    public UnflippableDisc(Player owner) {
        super(owner);
    }

    @Override
    public String getType() {
        return "⭕";
    }

    @Override
    public boolean isFlippable() {
        return false;
    }


}