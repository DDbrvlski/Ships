package statki;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class PreGame extends JPanel implements ActionListener, ComponentListener
{
    private int n = 11;
    public boolean isSinglePlayer;

    //Statki
    private String[] letters = {"A","B","C","D","E","F","G","H","I","J"};
    private String[] numbers = {"1","2","3","4","5","6","7","8","9","10"};
    private JButton[][] buttonsPlayer = new JButton[n][n];
    private int[][] shipsPlayer = new int[n][n];
    private ArrayList<Ship> ships = new ArrayList<Ship>();
    private JList shipList = new JList();
    private ArrayList<Field> shipsPlayerList;

    //JPanel
    private JPanel panelButton = new JPanel(); // Panel z buttonami play, reset, back
    private JPanel labelsPanel = new JPanel(); // Panel z labelami
    private JPanel rotatePanel = new JPanel(); //panel z buttonem do pozycji statkow
    private JPanel placementPanel = new JPanel(); // Panel z buttonami rotate i placement
    private JPanel boardsPanel = new JPanel(); // Panel dla całego okna
    private JPanel boardPlayerPanel = new JPanel(); // Panel dla lewej części okna (panel z planszą gracza)
    private JPanel availableShipsPanel = new JPanel(); // Panel dla prawej części okna (panel z dostępnymi statkami)
    private JPanel availableShipsList = new JPanel(); // Panel z listą dostępnych statków

    //JLabel
    private JLabel labelPlayerBoard;
    private JLabel labelAvailableShips;

    //Button
    private JButton back;
    private JButton play;
    private JButton reset;
    private JButton rotate; // Button służący do zmiany orientacji stawianego statku
    private JButton placement; // Button służący za pole tekstowe

    private Style style = new Style();
    private GameChoicePanel gameChoicePanel;

    public PreGame(GameChoicePanel gameChoicePanel)
    {
        this.gameChoicePanel = gameChoicePanel;
        // Ustawienia okna
        setVisible(true);
        setLayout(new BorderLayout());
        boardsPanel.setLayout(new GridLayout(0,2,40,40));
        boardsPanel.setBackground(Color.lightGray);
        boardsPanel.setBorder(BorderFactory.createEmptyBorder(0,20,70,20));
        boardsPanel.add(boardPlayerPanel);
        boardsPanel.add(availableShipsPanel);
        addComponentListener(this);

        // Buttony
        back = new JButton("Wróć");
        reset = new JButton("Resetuj");
        play = new JButton("Graj");
        rotate = new JButton("Obróć");
        placement = new JButton("W poziomie");

        back.addActionListener(this);
        reset.addActionListener(this);
        play.addActionListener(this);
        rotate.addActionListener(this);

        // style dla buttonów
        style.styleButton(back);
        style.styleButton(reset);
        style.styleButton(play);
        style.styleButton(rotate);
        style.styleButton(placement);
        placement.setEnabled(false);
        placement.setPreferredSize(new Dimension(150,40));
        placementPanel.add(placement);
        rotatePanel.add(rotate);

        // Góra okna
        labelsPanel = new JPanel(new GridLayout(0,2,30,40));
        labelsPanel.setBackground(Color.lightGray);
        labelsPanel.setBorder(BorderFactory.createEmptyBorder(10,20,10,0));

        labelPlayerBoard = new JLabel("Twoja plansza");
        labelAvailableShips = new JLabel("Dostępne statki");
        labelsPanel.add(labelPlayerBoard);
        labelsPanel.add(labelAvailableShips);

          // style dla labeli
        style.styleLabel(labelPlayerBoard);
        style.styleLabel(labelAvailableShips);


        // Prawa część okna
        shipList.setForeground(Color.darkGray);
        shipList.setFixedCellHeight(25);
        shipList.setFont(new Font("SansSerif",Font.BOLD,20));
        availableShipsList.add(shipList);
        availableShipsList.setBorder(BorderFactory.createEmptyBorder(20,0,10,0));
        availableShipsList.setBackground(Color.white);
        placementPanel.setBackground(Color.white);
        rotatePanel.setBackground(Color.white);
        availableShipsPanel.setLayout(new BorderLayout());
        availableShipsPanel.add(placementPanel,BorderLayout.NORTH);
        availableShipsPanel.add(availableShipsList,BorderLayout.CENTER);
        availableShipsPanel.add(rotatePanel,BorderLayout.SOUTH);

        // Lewa część okna
        boardPlayerPanel.setLayout(new GridLayout(n,n,1,1));
        boardPlayerPanel.setBackground(Color.lightGray);

        // Dolna część okna
        panelButton.add(back);
        panelButton.add(reset);
        panelButton.add(play);
        panelButton.setBackground(Color.lightGray);

        add(boardsPanel,BorderLayout.CENTER);
        add(labelsPanel,BorderLayout.NORTH);
        add(panelButton,BorderLayout.SOUTH);

        fillShipList();
        setButtons();
    }

    // Stworzenie planszy z buttonami
    private void setButtons()
    {
        for (int i = 0; i < buttonsPlayer.length; i++)
        {
            for (int j = 0; j < buttonsPlayer.length; j++)
            {
                buttonsPlayer[i][j] = new JButton();
            }
        }
        //dodanie buttonow do planszy gracza
        for (int i = 0; i < n; i++)
        {
            for (int j = 0; j < n; j++)
            {
                boardPlayerPanel.add(buttonsPlayer[i][j]);
                style.styleButtonBoard(buttonsPlayer[i][j]);
                buttonsPlayer[i][j].addActionListener(this);
            }
        }
        for(int j = 1; j < n; j++)
        {
            buttonsPlayer[0][j].setText(letters[j-1]);
            style.styleButtonBorders(buttonsPlayer[0][j]);

            buttonsPlayer[j][0].setText(numbers[j-1]);
            style.styleButtonBorders(buttonsPlayer[j][0]);
        }
        buttonsPlayer[0][0].setEnabled(false);
        //action listener do kazdego buttona
        for (int i = 1; i < n; i++)
        {
            for (int j = 1; j < n; j++)
            {
                int finalI = i;
                int finalJ = j;
                buttonsPlayer[i][j].addActionListener(ev -> buttonClicked(finalI, finalJ)); //dodanie do kazdego buttona event handlera
            }
        }
    }
    // Stworzenie EventHandlera
    private void buttonClicked(int i, int j)
    {
        if (!shipList.isSelectionEmpty())
        {
            int shipSize = getShipSize();
            int shipPos;
            if (isAvailable(i,j)){
                if (placement.getText() == "W poziomie") {
                    shipPos = j;
                    for (int x = 0;x < shipSize; x++){
                        shipsPlayerList.add(new Field(i,shipPos));
                        shipsPlayer[i][shipPos++] = shipSize;
                        style.styleButtonPlayerShip(buttonsPlayer[i][j]);
                        buttonsPlayer[i][j++].setText(String.valueOf(shipSize));
                    }
                    deleteShip();
                }
                else
                {
                    shipPos = i;
                    for (int x = 0;x < shipSize; x++){
                        shipsPlayerList.add(new Field(shipPos,j));
                        shipsPlayer[shipPos++][j] = shipSize;
                        style.styleButtonPlayerShip(buttonsPlayer[i][j]);
                        buttonsPlayer[i++][j].setText(String.valueOf(shipSize));
                    }
                    deleteShip();
                }
            }
        }
    }
    // Sprawdza czy można postawić statek
    private boolean isAvailable(int i, int j)
    {
        String direction = placement.getText();
        int shipSize = getShipSize();

        if (direction == "W poziomie"){
            int shipPos = j;
            for (int x = 1;x < shipSize; x++){
                shipPos++;
                if (shipPos > 10 || shipsPlayer[i][shipPos] > 0){
                    JOptionPane.showMessageDialog(null, "Nie można ustawić tutaj statku");
                    return false;
                }
            }
            return true;
        }
        else{
            int shipPos = i;
            for (int x = 1;x < shipSize; x++){
                shipPos++;
                if (shipPos > 10 || shipsPlayer[shipPos][j] > 0){
                    JOptionPane.showMessageDialog(null, "Nie można ustawić tutaj statku");
                    return false;
                }
            }
            return true;
        }
    }
    // Wypelnia listę dostępnymi statkami
    private void fillShipList()
    {
        ships = new ArrayList<Ship>();
        ships.add(new Ship("Jednomasztowiec",1));
        ships.add(new Ship("Jednomasztowiec",1));
        ships.add(new Ship("Dwumasztowiec",2));
        ships.add(new Ship("Dwumasztowiec",2));
        ships.add(new Ship("Trójmasztowiec",3));
        ships.add(new Ship("Trójmasztowiec",3));
        ships.add(new Ship("Czteromasztowiec",4));
        shipList.setListData(ships.toArray());
    }
    // Zwraca index zaznaczonego statku
    private int getShipIndex() { return shipList.getSelectedIndex(); }
    // Zwraca wielkość wybranego statku
    private int getShipSize() { return ships.get(getShipIndex()).size; }
    // Kasuje wybrany statek z listy
    private void deleteShip() { ships.remove(getShipIndex()); shipList.setListData(ships.toArray()); }
    // Resetuje ustawienie statków do fazy początkowej
    public void resetBoard()
    {
        fillShipList();
        shipsPlayerList = new ArrayList<>();
        for (int i = 1; i < 11; i++){
            for (int j = 1; j < 11; j++){
                buttonsPlayer[i][j].setText("");
                buttonsPlayer[i][j].setBackground(Color.white);
            }
        }
        shipsPlayer = new int[11][11];
    }
    //zapis pozycji statkow gracza
    private void saveCurrentShipPositions()
    {
        try(FileWriter out = new FileWriter("setBoard.txt")) {
            for (int i = 1; i < 11; i++){
                for (int j = 1; j < 11; j++){
                    out.write(shipsPlayer[i][j]+";");
                }
                out.write("\n");
            }
        }
        catch (IOException ex) { System.out.println(ex); }
    }
    //zwroc statki gracza
    public int[][] getShipsPlayer(){
        return shipsPlayer;
    }
    public ArrayList<Field> getShipsPlayerList(){
        return shipsPlayerList;
    }
    @Override
    public void actionPerformed(ActionEvent e)
    {
        Object generator = e.getSource();
        if(generator == back)
        {
            statki.MainPanel.cl.show(MainPanel.panelContent,"Main");
        }
        if (generator == play)
        {
            if (isSinglePlayer)
            {
                statki.MainPanel.cl.show(MainPanel.panelContent,"Game");
                saveCurrentShipPositions();
            }
            else
            {
                setVisible(false);
                for (Field ship: shipsPlayerList)
                {
                    System.out.println(ship);
                }
                BoardMultiplayer boardDlaMulti = new BoardMultiplayer(shipsPlayerList);
                boardDlaMulti.init();
            }
        }
        if (generator == rotate)
        {
            if (placement.getText() == "W pionie")
            {
                placement.setText("W poziomie");
            }
            else
            {
                placement.setText("W pionie");
            }
        }
        if (generator == reset)
        {
            resetBoard();
        }
    }
    @Override
    public void componentResized(ComponentEvent e) {}
    @Override
    public void componentMoved(ComponentEvent e) {}
    //czy klasa pregame dla trybu online czy offline
    @Override
    public void componentShown(ComponentEvent e)
    {
        this.isSinglePlayer = gameChoicePanel.isSinglePlayer;
        resetBoard();
    }
    @Override
    public void componentHidden(ComponentEvent e) {}
}
