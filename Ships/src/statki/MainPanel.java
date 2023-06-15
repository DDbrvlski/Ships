package statki;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.Socket;
//glowne okno aplikacji
public class MainPanel
{
    JFrame frame = new JFrame("Statki");
    public static JPanel panelContent = new JPanel();
    SavedGames savedGames = new SavedGames();
    MainMenuPanel mainMenuPanel = new MainMenuPanel(savedGames);
    GameChoicePanel gameChoicePanel = new GameChoicePanel();
    PreGame preGame = new PreGame(gameChoicePanel);
    Board board = new Board(preGame, savedGames, mainMenuPanel);
    public static CardLayout cl = new CardLayout();
    public MainPanel()
    {
        //cardlayout obslugujacy panele wyswietlane w tym samym oknie
        panelContent.setLayout(cl);
        panelContent.setBackground(Color.lightGray);

        panelContent.add(mainMenuPanel,"Main");
        panelContent.add(gameChoicePanel,"GameChoice");
        panelContent.add(savedGames,"SavedGames");
        panelContent.add(preGame,"PreGame");
        panelContent.add(board,"Game");

        //panel domyslny przy otwarciu okna
        cl.show(panelContent,"Main");
        frame.add(panelContent);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setSize(800,550);
    }
}
