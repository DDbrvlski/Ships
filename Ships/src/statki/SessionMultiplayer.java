package statki;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class SessionMultiplayer extends Thread
{
    public static ArrayList<SessionMultiplayer> clients = new ArrayList<>();
    private Socket socket;
    private ObjectInputStream fromClientObject;
    private ObjectOutputStream toClientObject;
    private ArrayList<ArrayList<Field>> shots = null;
    private ArrayList<Field> ships = null;
    private boolean playerTurn;
    private int playerNumber;
    private static int numberOfPlayers = 0;
    private int sizeOfArray;
    public SessionMultiplayer(Socket socket)
    {
        try{
            this.socket = socket;
            fromClientObject = new ObjectInputStream(socket.getInputStream());
            toClientObject = new ObjectOutputStream(socket.getOutputStream());
            clients.add(this);
            numberOfPlayers++;
            playerNumber = numberOfPlayers;

        } catch (IOException e) {
            closeEverything(socket, fromClientObject, toClientObject);
        }
        //Ustawia ture dla gracza ktory pierwszy wszedl do sesji
        if (playerNumber == 1) playerTurn = true;
        else playerTurn = false;
    }
    //Sprawdza czy lista "ships" zawiera obiekt podany w parametrze
    private boolean containsField(Field f)
    {
        for (Field field: ships){
            if (field.equalsField(f)) return true;
        }
        return false;
    }
    @Override
    public void run()
    {
        Message message = null;
        while (socket.isConnected())
        {
            try{
                try {
                    message = (Message) fromClientObject.readObject();
                } catch (IOException e) {
                    System.out.println("Błąd podczas odczycie");
                }
                //Przyjmuje wiadomosc o pozycjach i przypisuje statki gracza do listy "ships" w sesji
                if (message.getMessage().contains("positions")){
                    ships = (ArrayList<Field>) message.getListOfShips();
                    sizeOfArray = ships.size();
                    Message isPlayerTurn = new Message("isPlayerTurn", playerTurn);
                    toClientObject.writeObject(isPlayerTurn);
                    toClientObject.flush();
                }
                //Przyjmuje wiadomosc o wystrzale i przekazuje do drugiego gracza
                if (message.getMessage().contains("shot") && ships != null)
                {
                    Field f = message.getField();
                    for (SessionMultiplayer client: clients){
                        if (playerNumber != client.playerNumber){
                            client.toClientObject.writeObject(new Message("getShot", f));
                            client.toClientObject.flush();

                            //Sprawdza czy statek przeciwnika zostal trafiony
                            if (client.containsField(f)){
                                toClientObject.writeObject(new Message("hit", f));
                                toClientObject.flush();
                                client.sizeOfArray--;

                                //Zwraca informacje o wygranej
                                if (client.sizeOfArray == 0){
                                    toClientObject.writeObject(new Message("win", true, 0));
                                    client.toClientObject.writeObject(new Message("win", false, 0));
                                }
                            }
                            else {
                                //Zmienia tury graczy
                                playerTurn = false;
                                client.playerTurn = true;
                                Message isPlayerTurn = new Message("isPlayerTurn", playerTurn);
                                Message isClientPlayerTurn = new Message("isPlayerTurn", client.playerTurn);

                                //Zwraca graczom informacje o turze
                                toClientObject.writeObject(isPlayerTurn);
                                toClientObject.flush();
                                client.toClientObject.writeObject(isClientPlayerTurn);
                                client.toClientObject.flush();
                            }
                        }
                    }
                }
            } catch (IOException e) {
                closeEverything(socket, fromClientObject, toClientObject);
                break;
            } catch (ClassNotFoundException e) {}
        }
    }
    //Zamyka wszystkie strumienie/socket
    private void closeEverything(Socket socket, ObjectInputStream in, ObjectOutputStream out)
    {
        clients.remove(this);
        try{
            if (socket != null) socket.close();
            if (in != null) in.close();
            if (out != null) out.close();
        } catch (IOException e) {
            System.out.println("Błąd podczas zamykania gniazda");
        }
    }
}
