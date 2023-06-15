package statki;
public class SessionAgainstComputer extends Thread
{
    Board b;
    public SessionAgainstComputer(Board b)
    {
        this.b = b;
    }
    @Override
    public void run(){
        while(b.game){
            b.winner();
            //sprawdza czy watek moze odpalic metody odpowiedzialne za ruchy komputera
            if (!b.playerTurn)
            {
                b.computerHit();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {}
            }
            if (!b.game){
                interrupt();
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {}
        }
    }
}
