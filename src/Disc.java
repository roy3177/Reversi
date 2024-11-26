/**
 * @author Roy Meoded
 * @author Noa Agassi
 * The Disc interface defines the characteristics of a game in a chess-like game.
 * Implementing classes should provide information about the player who owns the Disc.
 */
public abstract class Disc {
    protected Player owner;

    //A constructor that accepts a type,player owner and position
    //and builds a new disc
    public Disc( Player owner){
        this.owner = owner;
    }

    public abstract String getType();

    public Player getOwner(){
        return owner;
    }

    public void setOwner(Player o){
        this.owner = o;
    }

    public abstract boolean isFlippable();

}