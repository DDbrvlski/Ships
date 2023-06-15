package statki;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
//panel wyboru trybu gry
public class GameChoicePanel extends JPanel implements ActionListener
{
    JButton computerGame;
    JButton onlineGame;
    JButton back;
    JLabel gameMode;
    Style style = new Style();
    boolean isSinglePlayer;
    public GameChoicePanel()
    {
        setLayout(new BorderLayout());
        setBackground(Color.lightGray);
        JPanel panel = new JPanel();
        panel.setBackground(Color.lightGray);
        panel.setLayout(new GridLayout(3,0,20,20));
        panel.setBorder(BorderFactory.createEmptyBorder(25,280,50,280));

        gameMode = new JLabel("Tryb gry");
        gameMode.setForeground(Color.darkGray);
        gameMode.setBorder(BorderFactory.createEmptyBorder(50,0,0,0));
        gameMode.setHorizontalAlignment(JLabel.CENTER);
        gameMode.setVerticalAlignment(JLabel.CENTER);
        gameMode.setFont(new Font("SansSerif",Font.BOLD,60));

        computerGame = new JButton("Gra z komputerem");
        style.styleButton2(computerGame);
        computerGame.addActionListener(this);

        onlineGame = new JButton("Gra z przeciwnikiem");
        style.styleButton2(onlineGame);
        onlineGame.addActionListener(this);

        back = new JButton("Wróć");
        style.styleButton2(back);
        back.addActionListener(this);

        JLabel authors = new JLabel("Damian Dobrowolski i Klaudia Miłoszewska");
        authors.setForeground(Color.darkGray);
        authors.setBorder(BorderFactory.createEmptyBorder(0,10,10,0));

        panel.add(computerGame);
        panel.add(onlineGame);
        panel.add(back);

        add(panel, BorderLayout.CENTER);
        add(gameMode, BorderLayout.NORTH);
        add(authors,BorderLayout.SOUTH);
    }
    @Override
    public void actionPerformed(ActionEvent e)
    {
        Object generator = e.getSource();
        if(generator == back)
        {
            statki.MainPanel.cl.show(MainPanel.panelContent,"Main");
        }
        else if(generator == computerGame)
        {
            isSinglePlayer = true;
            statki.MainPanel.cl.show(MainPanel.panelContent,"PreGame");
        }
        else if(generator == onlineGame)
        {
            isSinglePlayer = false;
            statki.MainPanel.cl.show(MainPanel.panelContent,"PreGame");
        }
    }
}
