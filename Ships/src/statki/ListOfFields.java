package statki;

import java.io.Serializable;
import java.util.ArrayList;

// Lista pol
public class ListOfFields
{
    private ArrayList<Field> listOfFields = new ArrayList<Field>();
    ListOfFields()
    {
        fillList();
    }
    public void fillList()
    {
        for (int i = 1; i < 11; i++)
        {
            for (int j = 1; j < 11; j++)
            {
                listOfFields.add(new Field(i,j));
            }
        }
    }
    //Zwraca wartosc true/false w zaleznosci czy podany w parametrze obiekt znajduje sie w liscie "listOfFields"
    public boolean contains(Field f){
        for (Field field: listOfFields){
            if (field.equalsField(f)){
                return true;
            }
        }
        return false;
    }
    //Zwraca obiekt o podanym w parametrze indexie
    public Field getField(int index){
        return listOfFields.get(index);
    }
    //Usuwa obiekt podany w parametrze z listy "listOfFields"
    public void usunPoleZListy(Field f){
        listOfFields.remove(f);
    }
    //Zwraca ilosc elementow w liscie "listOfFields"
    public int iloscElementowWLiscie(){
        return  listOfFields.size();
    }
}
// Klasa zawierajaca dane o polu
class Field implements Serializable
{
    private int i;
    private int j;
    public Field(int i, int j){
        this.i = i;
        this.j = j;
    }
    public int getI() {
        return i;
    }
    public int getJ() {
        return j;
    }
    @Override
    public String toString() {
        return "Field{" +
                "i=" + i +
                ", j=" + j +
                '}';
    }
    public boolean equalsField(Field f) {
        return i == f.i && j == f.j;
    }
}