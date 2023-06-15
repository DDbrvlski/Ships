package statki;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.File;
import java.util.ArrayList;
//panel do wczytywania zapisanych gier single player
public class SavedGames extends JPanel implements ActionListener, ComponentListener
{
    private JLabel gameName;
    private JButton loadGame;
    private JButton back;
    private JButton delete;
    private ArrayList<File> savedGamesArrayList = new ArrayList<File>();
    private Style style = new Style();
    private JList savedGamesList = new JList();
    private File fileToLoad;
    private Boolean load = false;
    public SavedGames()
    {
        setLayout(new BorderLayout());
        setBackground(Color.lightGray);

        /* north */
        gameName = new JLabel("Wczytaj grę");
        gameName.setForeground(Color.darkGray);
        gameName.setBorder(BorderFactory.createEmptyBorder(20,0,0,0));
        gameName.setHorizontalAlignment(JLabel.CENTER);
        gameName.setVerticalAlignment(JLabel.CENTER);
        gameName.setFont(new Font("SansSerif",Font.BOLD,60));

        /* center */
        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(Color.lightGray);
        centerPanel.setLayout(new GridLayout(0,2,20,20));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(50,50,50,50));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.lightGray);
        buttonPanel.setLayout(new GridLayout(3,0,20,20));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0,20,0,20));

        loadGame = new JButton("Wczytaj grę");
        style.styleButton2(loadGame);
        loadGame.addActionListener(this);

        delete = new JButton("Usuń zapis");
        style.styleButton2(delete);
        delete.addActionListener(this);

        back = new JButton("Wróć");
        style.styleButton2(back);
        back.addActionListener(this);

        SavedGames.this.addComponentListener(this);

        buttonPanel.add(loadGame);
        buttonPanel.add(delete);
        buttonPanel.add(back);

        savedGamesList.setListData(savedGamesArrayList.toArray());
        savedGamesList.setForeground(Color.darkGray);
        savedGamesList.setFixedCellHeight(30);
        savedGamesList.setFont(new Font("SansSerif",Font.BOLD,20));
        savedGamesList.setBackground(Color.white);
        savedGamesList.setBorder(BorderFactory.createEmptyBorder(5,10,0,10));
        centerPanel.add(savedGamesList);
        centerPanel.add(buttonPanel);

        /* south */
        JLabel authors = new JLabel("Damian Dobrowolski i Klaudia Miłoszewska");
        authors.setForeground(Color.darkGray);
        authors.setBorder(BorderFactory.createEmptyBorder(0,10,10,0));

        add(centerPanel,BorderLayout.CENTER);
        add(gameName,BorderLayout.NORTH);
        add(authors,BorderLayout.SOUTH);
    }
    //lista z dostepnymi zapisami gier
    public void getSavedGames()
    {
        savedGamesArrayList = new ArrayList<>();
        for (int i = 1; i < 4; i++)
        {
            if (new File("SavedGame" + i + ".txt").exists())
            {
                savedGamesArrayList.add(new File("SavedGame" + i + ".txt"));
            }
        }
        savedGamesList.setListData(savedGamesArrayList.toArray());
    }
    public File getLoadFile()
    {
        load = false;
        return fileToLoad;
    }
    public boolean shouldLoad(){
        return load;
    }
    //usuniecie wybranej pozycji zapisu z listy
    private void deleteSave()
    {
        try
        {
            savedGamesArrayList.get(savedGamesList.getSelectedIndex()).delete();
            savedGamesArrayList.remove(savedGamesList.getSelectedIndex());
            getSavedGames();
        }
        catch (IndexOutOfBoundsException e){JOptionPane.showMessageDialog(new JFrame(),
                "Wybierz zapis z listy aby go usunąć!");
        }
    }
    @Override
    public void actionPerformed(ActionEvent e)
    {
        Object generator = e.getSource();
        if(generator == back)
        {
            statki.MainPanel.cl.show(MainPanel.panelContent,"Main");
        }
        else if(generator == loadGame)
        {
            if(!savedGamesList.isSelectionEmpty())
            {
                load = true;
                fileToLoad = savedGamesArrayList.get(savedGamesList.getSelectedIndex());
                statki.MainPanel.cl.show(MainPanel.panelContent,"Game");
            }else
                JOptionPane.showMessageDialog(new JFrame(),"Nie masz żadnych zapisanych gier!");
        }
        else
        {
            deleteSave();
        }
    }
    @Override
    public void componentResized(ComponentEvent e) {}
    @Override
    public void componentMoved(ComponentEvent e) {}
    @Override
    public void componentShown(ComponentEvent e) {
        getSavedGames();
    }
    @Override
    public void componentHidden(ComponentEvent e) {}
}
