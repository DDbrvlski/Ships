package statki;

import java.io.Serializable;
import java.util.List;

//Klasa stworzona do przekazywania miedzy serwerem a klientem
public class Message implements Serializable {
    private String message = null;
    private Field field = null;
    private List<Field> listOfShips = null;
    private boolean isPlayerTurn;
    private boolean win;
    private int temp;
    public Message(String message, Field field) {
        this.message = message;
        this.field = field;
    }
    public Message(String message, List<Field> listOfShips) {
        this.message = message;
        this.listOfShips = listOfShips;
    }
    public Message(String message, boolean isPlayerTurn) {
        this.message = message;
        this.isPlayerTurn = isPlayerTurn;
    }
    public Message(String message, boolean win, int temp) {
        this.message = message;
        this.win = win;
        this.temp = temp;
    }
    public String getMessage() {
        return message;
    }
    public Field getField() {
        return field;
    }
    public List<Field> getListOfShips() {
        return listOfShips;
    }
    public boolean isPlayerTurn() {
        return isPlayerTurn;
    }
    public boolean isWin() {
        return win;
    }
}
