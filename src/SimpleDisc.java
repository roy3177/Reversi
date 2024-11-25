/**
 * @author Roy Meoded
 * @ author Noa Agassi
 * This class represents a regular disc in the Reversi game.
 * This class inherits from the abstract clas Disc.
 * It uses a Disc constructor-using super(owner) to define the player holding that disc.
 * Its also implements the abstract function getType(), that defined in the disc and
 * return aaaa unique character "⬤" to indicate that is a regular disc.
 */
public class SimpleDisc extends Disc {

    public SimpleDisc(Player owner) {
        super(owner);  // Pass the owner to the superclass constructor
    }

    @Override
    public String getType() {
        return "⬤";  // Type for SimpleDisc
    }

    @Override
    public boolean isFlippable() {
        return true;  // Simple discs are flippable
    }
}