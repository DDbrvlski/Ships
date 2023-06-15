package statki;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class BoardMultiplayer extends JFrame implements ActionListener
{
    private Socket socket;
    private ObjectOutputStream toClientObject;
    boolean isPlayerTurn = true;

    int n = 11;
    String[] letters = {"A","B","C","D","E","F","G","H","I","J"};
    String[] numbers = {"1","2","3","4","5","6","7","8","9","10"};
    JButton [][] buttonsPlayer = new JButton[n][n];
    JButton [][] buttonsEnemy = new JButton[n][n];
    int[][] shipsPlayer = new int[n][n]; //Przechowuje liczbe 1 w miejscu gdzie znajduje się nasz statek
    int[][] shipsEnemy = new int[n][n]; //Przechowuje liczbe 1 w miejscu gdzie znajduje się statek przeciwnika

    JPanel panelButton = new JPanel();
    JPanel labelsPanel;

    JLabel labelPlayer;
    JLabel labelEnemy;

    JButton back;
    JButton ready;

    JPanel boardsPanel = new JPanel();
    JPanel boardPlayerPanel = new JPanel();
    JPanel boardEnemyPanel = new JPanel();

    Style style = new Style();

    ArrayList<Field> listOfPlayerShips = new ArrayList<>();
    ArrayList<Field> listOfShots = new ArrayList<>();


    public BoardMultiplayer(ArrayList<Field> listOfPlayerShips){
        this.listOfPlayerShips = listOfPlayerShips;
        try{
            this.socket = new Socket("localhost", 2040);
            this.toClientObject = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            closeEverything();
        }
    }
    public void init()
    {

        setSize(800,550);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        boardsPanel.setLayout(new GridLayout(0,2,40,40));
        boardsPanel.setBackground(Color.lightGray);
        boardEnemyPanel.setLayout(new GridLayout(n,n,1,1));
        boardEnemyPanel.setBackground(Color.lightGray);
        boardPlayerPanel.setLayout(new GridLayout(n,n,1,1));
        boardPlayerPanel.setBackground(Color.lightGray);

        labelPlayer = new JLabel("Twoja plansza");
        styleLabel(labelPlayer);
        labelEnemy = new JLabel("Plansza przeciwnika");
        styleLabel(labelEnemy);

        labelsPanel = new JPanel(new GridLayout(0,2,30,40));
        labelsPanel.setBackground(Color.lightGray);
        labelsPanel.setBorder(BorderFactory.createEmptyBorder(10,20,10,0));
        labelsPanel.add(labelPlayer);
        labelsPanel.add(labelEnemy);

        setButtons();
        back = new JButton("Wróć");
        style.styleButton(back);
        back.addActionListener(this);
        ready = new JButton("Gotowy");
        style.styleButton(ready);
        ready.addActionListener(this);

        //ADDING ALL PANELS
        panelButton.add(back);
        panelButton.add(ready);
        panelButton.setBackground(Color.lightGray);

        boardsPanel.add(boardPlayerPanel);
        boardsPanel.add(boardEnemyPanel);
        boardsPanel.setBorder(BorderFactory.createEmptyBorder(0,20,70,20));
        add(labelsPanel,BorderLayout.NORTH);
        add(boardsPanel,BorderLayout.CENTER);
        add(panelButton,BorderLayout.SOUTH);

        new game(this,socket, listOfPlayerShips, listOfShots, buttonsPlayer);
    }

    public void sendShot(Field field){
        try{
            if (socket.isConnected()){
                if (isPlayerTurn){
                    Message shot = new Message("shot", field);
                    toClientObject.writeObject(shot);
                    toClientObject.flush();
                }
            }
        } catch (Exception e) {
            closeEverything();
        }
    }

    public void sendPositions(ArrayList<Field> positions){
        try{
            if (socket.isConnected()){
                toClientObject.writeObject(new Message("positions", listOfPlayerShips));
                toClientObject.flush();
                ready.setEnabled(false);
            }
        } catch (Exception e) {
            closeEverything();
        }
    }
    public void changeBool(){
        if (isPlayerTurn) isPlayerTurn = false;
        else isPlayerTurn = true;
    }
    public void updateEnemyBoard(Field field){
        style.styleButtonEnemyShipHit(buttonsEnemy[field.getI()][field.getJ()]);
    }
    public void isWin(boolean win){
        if (win) JOptionPane.showMessageDialog(null,"WYGRALES!");
        else JOptionPane.showMessageDialog(null,"PRZEGRALES!");
    }
    public void styleLabel(JLabel label){
        label.setForeground(Color.white);
        label.setFont(new Font("SansSerif",Font.BOLD,15));
    }
    public void setButtons(){
        //Tworzy buttony dla gracza
        for (int i = 0; i < buttonsPlayer.length; i++)
        {
            for (int j = 0; j < buttonsPlayer.length; j++)
            {
                buttonsPlayer[i][j] = new JButton();
            }
        }
        //Tworzy buttony dla przeciwnika
        for (int i = 0; i < buttonsEnemy.length; i++)
        {
            for (int j = 0; j < buttonsEnemy.length; j++)
            {
                buttonsEnemy[i][j] = new JButton();
            }
        }
        //Dodaje buttony do planszy gracza
        for (int i = 0; i < n; i++)
        {
            for (int j = 0; j < n; j++)
            {
                boardPlayerPanel.add(buttonsPlayer[i][j]);
                style.styleButtonBoard(buttonsPlayer[i][j]);
            }
        }
        //Dodaje buttony do planszy przeciwnika
        for (int i = 0; i < n; i++)
        {
            for (int j = 0; j < n; j++)
            {
                boardEnemyPanel.add(buttonsEnemy[i][j]);
                style.styleButtonBoard(buttonsEnemy[i][j]);
                buttonsEnemy[i][j].addActionListener(this);

            }
        }
        //Ustawia litery i liczby na górnym i lewym pasku planszy
        for(int j = 1; j < n; j++)
        {
            buttonsPlayer[0][j].setText(letters[j-1]);
            style.styleButtonBorders(buttonsPlayer[0][j]);

            buttonsPlayer[j][0].setText(numbers[j-1]);
            style.styleButtonBorders(buttonsPlayer[j][0]);

            buttonsEnemy[0][j].setText(letters[j-1]);
            style.styleButtonBorders(buttonsEnemy[0][j]);

            buttonsEnemy[j][0].setText(numbers[j-1]);
            style.styleButtonBorders(buttonsEnemy[j][0]);

        }
        //Dodaje Action Listenera do buttonów przeciwnika i ustawia statki na planszy gracza
        for (int i = 1; i < n; i++)
        {
            for (int j = 1; j < n; j++)
            {
                if (shipsPlayer[i][j] != 0){
                    style.styleButtonPlayerShip(buttonsPlayer[i][j]);
                    buttonsPlayer[i][j].setText(String.valueOf(shipsPlayer[i][j]));
                }
                int finalI = i;
                int finalJ = j;
                buttonsEnemy[i][j].addActionListener(ev -> buttonClicked(finalI, finalJ)); //dodanie do kazdego buttona event handlera
            }
        }
        buttonsPlayer[0][0].setEnabled(false);
        buttonsEnemy[0][0].setEnabled(false);
    }
    private void updateBoard(){
        for (Field field: listOfPlayerShips){
            style.styleButtonPlayerShip(buttonsPlayer[field.getI()][field.getJ()]);
        }
    }
    private boolean containsField(Field f){
        for (Field field: listOfPlayerShips){
            if (field.equalsField(f)) return true;
        }
        return false;
    }
    private void resetBoard(){
        shipsPlayer = new int[n][n];
        shipsEnemy = new int[n][n];
        for (int i = 1; i < n; i++)
        {
            for (int j = 1; j < n; j++)
            {
                style.styleButtonBoard(buttonsEnemy[i][j]);
                buttonsEnemy[i][j].setText("");
            }
        }
    }
    //Wcisniecie buttona na planszy
    public void buttonClicked(int i, int j){
        if (isPlayerTurn){
            sendShot(new Field(i,j));
            style.styleButtonEnemyBoardHit(buttonsEnemy[i][j]);
        }
    }
    private void closeEverything(){
        try{
            if (socket != null) this.socket.close();
            if (toClientObject != null) toClientObject.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void actionPerformed(ActionEvent e){
        Object generator = e.getSource();
        if(generator == back)
        {
            System.exit(0);
        }
        else if (generator == ready) {
            sendPositions(listOfPlayerShips);
            updateBoard();
        }
    }
}
class game {
    ObjectInputStream fromClientObject;
    ArrayList<Field> listOfPlayerShips;
    ArrayList<Field> listOfShots;
    JButton[][] buttonsPlayer;
    Style style = new Style();
    boolean isPT;
    BoardMultiplayer boardDlaMulti;
    Field tempField = null;

    public game(BoardMultiplayer boardDlaMulti, Socket socket, ArrayList<Field> listOfPlayerShips, ArrayList<Field> listOfShots, JButton[][] buttonsPlayer){
        this.listOfPlayerShips = listOfPlayerShips;
        this.buttonsPlayer = buttonsPlayer;
        this.listOfShots = listOfShots;
        this.boardDlaMulti = boardDlaMulti;
        try {
            this.fromClientObject = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        listenForMessages();
    }
    private boolean containsField(Field f){
        for (Field field: listOfPlayerShips){
            if (field.equalsField(f)) return true;
        }
        return false;
    }
    private void updateBoard(){
        Field f = listOfShots.get(listOfShots.size()-1);
        if (containsField(f)){
            style.styleButtonEnemyShipHit(buttonsPlayer[f.getI()][f.getJ()]);
        }
        else {
            style.styleButtonEnemyBoardHit(buttonsPlayer[f.getI()][f.getJ()]);
        }
    }
    private void closeEverything(){
        try{
            if (fromClientObject != null) fromClientObject.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void listenForMessages(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message = null;
                while (true){
                    try {
                        message = (Message) fromClientObject.readObject();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    if (message.getMessage().contains("isPlayerTurn")){
                        boardDlaMulti.isPlayerTurn = message.isPlayerTurn();
                        System.out.println(boardDlaMulti.isPlayerTurn);


                    }
                    if (message.getMessage().contains("getShot")){
                        listOfShots.add(message.getField());
                        updateBoard();
                    }
                    if (message.getMessage().contains("hit")){
                        tempField = message.getField();
                        boardDlaMulti.updateEnemyBoard(tempField);


                    }
                    if (message.getMessage().contains("win")){
                        boardDlaMulti.isWin(message.isWin());
                        break;

                    }

                }
                closeEverything();
            }
        }).start();
    }
}
