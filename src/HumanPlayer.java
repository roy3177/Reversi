public class HumanPlayer extends Player{
    private  int wins;

    public HumanPlayer(boolean isPlayerOne){
        super(isPlayerOne);
        this.wins=0;
    }

    @Override
    public boolean isHuman(){
        return true;
    }
    @Override
    public void addWin(){
        wins++;
    }
    @Override
    public int getWins(){
        return wins;
    }

}
