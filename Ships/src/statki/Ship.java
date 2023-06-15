package statki;
// klasa pomocnicza do ustawiania statków na planszy gracza w klasie PreGame
public class Ship
{
    int size;
    String name;
    public Ship(String name, int size)
    {
        this.name = name;
        this.size = size;
    }
    @Override
    public String toString(){
        return name + " (" + size + ")";
    }
}
