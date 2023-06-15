package statki;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.File;
import javax.swing.*;
//panel z menu glownym aplikacji
public class MainMenuPanel extends JPanel implements ActionListener, ComponentListener
{
    JLabel gameName;
    JButton play;
    JButton resume;
    JButton close;
    JButton savedGames;
    SavedGames sg;
    Style style = new Style();
    boolean continueGame = false;
    public MainMenuPanel(SavedGames sg)
    {
        this.sg = sg;
        addComponentListener(this);
        setLayout(new BorderLayout());
        setBackground(Color.lightGray);
        JPanel panel = new JPanel();
        panel.setBackground(Color.lightGray);
        panel.setLayout(new GridLayout(4,0,20,20));
        panel.setBorder(BorderFactory.createEmptyBorder(25,280,50,280));

        gameName = new JLabel("STATKI");
        gameName.setForeground(Color.darkGray);
        gameName.setBorder(BorderFactory.createEmptyBorder(50,0,0,0));
        gameName.setHorizontalAlignment(JLabel.CENTER);
        gameName.setVerticalAlignment(JLabel.CENTER);
        gameName.setFont(new Font("SansSerif",Font.BOLD,60));

        play = new JButton("Nowa gra");
        style.styleButton2(play);
        play.addActionListener(this);

        resume = new JButton("Kontynuuj grę");
        style.styleButton2(resume);
        resume.addActionListener(this);

        savedGames = new JButton("Wczytaj grę");
        style.styleButton2(savedGames);
        savedGames.addActionListener(this);

        close = new JButton("Wyjdź");
        style.styleButton2(close);
        close.addActionListener(this);

        panel.add(play);
        panel.add(resume);
        panel.add(savedGames);
        panel.add(close);

        add(panel,BorderLayout.CENTER);
        add(gameName,BorderLayout.NORTH);
        JLabel authors = new JLabel("Damian Dobrowolski i Klaudia Miłoszewska");
        authors.setForeground(Color.darkGray);
        authors.setBorder(BorderFactory.createEmptyBorder(0,10,10,0));
        add(authors,BorderLayout.SOUTH);

        isCurrentGameExists();
    }
    //funkcja sprawdzająca czy użytkownik ma ostatnią rozegraną grę z komputerem którą może kontynuować
    private void isCurrentGameExists()
    {
        if (!new File("currentGame.txt").exists())
        {
            resume.setEnabled(false);
        }
        else resume.setEnabled(true);
    }
    @Override
    public void actionPerformed(ActionEvent e)
    {
        Object generator = e.getSource();
        if(generator == close)
        {
            System.exit(0);
        }
        else if(generator == resume)
        {
            statki.MainPanel.cl.show(MainPanel.panelContent,"Game");
            continueGame = true;
        }
        else if(generator == play)
        {
            statki.MainPanel.cl.show(MainPanel.panelContent,"GameChoice");
            continueGame = false;
        }
        else if (generator == savedGames)
        {
            statki.MainPanel.cl.show(MainPanel.panelContent,"SavedGames");
        }
    }
    @Override
    public void componentResized(ComponentEvent e) {}
    @Override
    public void componentMoved(ComponentEvent e) {}
    @Override
    public void componentShown(ComponentEvent e) {
        isCurrentGameExists();
    }
    @Override
    public void componentHidden(ComponentEvent e) {}
}
