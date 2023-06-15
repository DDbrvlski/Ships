package statki;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static final int serverPort = 2040;
    ServerSocket serverSocket;
    private int maxPlayers = 2;
    private int numPlayers = 0;
    public Server()
    {
        try
        {
            this.serverSocket = new ServerSocket(serverPort);
        } catch (IOException e) {
            System.exit(1);
        }
    }
    //uruchom serwer
    public void startServer()
    {
        Socket socket = null;
        try{
            while(true)
            {
                socket = serverSocket.accept();
                System.out.println("Nowy gracz dolaczyl!");
                numPlayers++;
                new SessionMultiplayer(socket).start();
                if (numPlayers == 3) break;
            }
        } catch (IOException e) {
            System.out.println("Problem podczas włączania serwera");
        } finally {
            closeServerSocket();
        }
    }
    //zamknij socket serwera
    public void closeServerSocket()
    {
        try
        {
            if (serverSocket != null)
            {
                serverSocket.close();
            }
        } catch (IOException e) {
            System.out.println("Błąd podczas zamykania gniazda serwera");
        }
    }
    public static void main(String[] args)
    {
        Server server = new Server();
        server.startServer();
    }
}
