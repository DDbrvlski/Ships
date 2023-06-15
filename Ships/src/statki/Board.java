package statki;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class Board extends JPanel implements ActionListener, ComponentListener
{
    int n = 11;
    String[] letters = {"A","B","C","D","E","F","G","H","I","J"};
    String[] numbers = {"1","2","3","4","5","6","7","8","9","10"};
    JButton [][] buttonsPlayer = new JButton[n][n];
    JButton [][] buttonsEnemy = new JButton[n][n];
    int[][] shipsPlayer = new int[n][n]; //Przechowuje liczbe 1 w miejscu gdzie znajduje się nasz statek
    int[][] shipsEnemy = new int[n][n]; //Przechowuje liczbe 1 w miejscu gdzie znajduje się statek przeciwnika
    int iloscStatkowGracza;
    int iloscStatkowKomputera;
    JPanel panelButton = new JPanel();
    JPanel labelsPanel;
    JLabel labelPlayer;
    JLabel labelEnemy;
    JLabel infoLabel;
    JButton back;
    JButton save;
    JPanel infoPanel;
    JPanel boardsPanel = new JPanel();
    JPanel boardPlayerPanel = new JPanel();
    JPanel boardEnemyPanel = new JPanel();
    ListOfFields listaPol = new ListOfFields();
    Style style = new Style();
    PreGame preGame;
    SavedGames savedGames;
    MainMenuPanel mainMenuPanel;
    Boolean gameStarted = false;
    String[] directions = {
            "W poziomie",
            "W pionie"
    };
    ArrayList<Integer> shipSizes = new ArrayList<Integer>();
    boolean playerTurn = true;
    Field f1 = null;
    Field f2 = null;
    boolean game = false;
    boolean backToF1 = false;
    ArrayList<Field> direction = new ArrayList<Field>();
    SessionAgainstComputer sesja;
    public Board(PreGame preGame, SavedGames savedGames ,MainMenuPanel mainMenuPanel)
    {
        this.preGame = preGame;
        this.savedGames = savedGames;
        this.mainMenuPanel = mainMenuPanel;
        listaPol.fillList();

        setVisible(true);
        setLayout(new BorderLayout());
        boardsPanel.setLayout(new GridLayout(0,2,40,40));
        boardsPanel.setBackground(Color.lightGray);
        boardEnemyPanel.setLayout(new GridLayout(n,n,1,1));
        boardEnemyPanel.setBackground(Color.lightGray);
        boardPlayerPanel.setLayout(new GridLayout(n,n,1,1));
        boardPlayerPanel.setBackground(Color.lightGray);
        addComponentListener(this);

        labelPlayer = new JLabel("Twoja plansza");
        style.styleLabel(labelPlayer);
        labelEnemy = new JLabel("Plansza przeciwnika");
        style.styleLabel(labelEnemy);

        labelsPanel = new JPanel(new GridLayout(0,2,30,40));
        labelsPanel.setBackground(Color.lightGray);
        labelsPanel.setBorder(BorderFactory.createEmptyBorder(10,20,10,0));
        labelsPanel.add(labelPlayer);
        labelsPanel.add(labelEnemy);

        setButtons();
        back = new JButton("Wróć");
        style.styleButton(back);
        back.addActionListener(this);
        save = new JButton("Zapisz");
        style.styleButton(save);
        save.addActionListener(this);

        infoPanel = new JPanel();
        infoLabel = new JLabel("Twoja tura...");
        infoPanel.setBackground(Color.lightGray);
        style.styleInfoLabel(infoLabel);
        infoPanel.add(infoLabel);

        //ADDING ALL PANELS
        panelButton.add(back);
        panelButton.add(save);
        panelButton.setBackground(Color.lightGray);

        JPanel southPanel = new JPanel(new GridLayout(2,0));
        southPanel.setBackground(Color.lightGray);
        southPanel.add(infoPanel);
        southPanel.add(panelButton);
        boardsPanel.add(boardPlayerPanel);
        boardsPanel.add(boardEnemyPanel);
        boardsPanel.setBorder(BorderFactory.createEmptyBorder(0,20,20,20));
        add(labelsPanel,BorderLayout.NORTH);
        add(boardsPanel,BorderLayout.CENTER);
        add(southPanel,BorderLayout.SOUTH);

        shipsPlayer = preGame.getShipsPlayer();
    }
    //Ustawia plansze gracza i komputera
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

                //dodanie do kazdego buttona actionlistenera poprzez zastosowanie wyrazenia lambda
                buttonsEnemy[i][j].addActionListener(ev -> buttonClicked(finalI, finalJ));
            }
        }
        buttonsPlayer[0][0].setEnabled(false);
        buttonsEnemy[0][0].setEnabled(false);
    }
    //Aktualizuje plansze
    private void updateBoard(){
        listaPol.fillList();
        for (int i = 1; i < n; i++)
        {
            for (int j = 1; j < n; j++)
            {
                if (shipsPlayer[i][j] > 0 && shipsPlayer[i][j] < 7){
                    style.styleButtonPlayerShip(buttonsPlayer[i][j]);
                    buttonsPlayer[i][j].setText(String.valueOf(shipsPlayer[i][j]));
                }
                else if(shipsPlayer[i][j] > 6){
                    listaPol.usunPoleZListy(new Field(i,j));
                    playerBoardHit(i,j);
                    if (shipsPlayer[i][j] == 7) {
                        f1 = new Field(i,j);
                        backToF1 = true;
                    }
                }
                else if (shipsEnemy[i][j] > 7){

                    if (shipsEnemy[i][j] == 8){
                        style.styleButtonEnemyBoardHit(buttonsEnemy[i][j]);
                    }
                    else {
                        style.styleButtonEnemyShipHit(buttonsEnemy[i][j]);
                    }
                }
                else{
                    style.styleButtonBoard(buttonsPlayer[i][j]);
                    style.styleButtonBoard(buttonsEnemy[i][j]);
                    buttonsPlayer[i][j].setText("");
                    buttonsEnemy[i][j].setText("");
                }
            }
        }
    }
    //Zapisuje gre w pliku aby moc kontynuowac rozgrywke po wyjsciu z gry
    private void saveCurrentGame(){
        try(FileWriter out = new FileWriter("currentGame.txt")) {
            for (int i = 1; i < 11; i++){
                for (int j = 1; j < 11; j++){
                    if (f1 != null && shipsPlayer[i][j] == shipsPlayer[f1.getI()][f1.getJ()]){
                        out.write("7" + ";");
                        f1 = null;
                    }
                    else{
                        out.write(shipsPlayer[i][j]+";");
                    }
                }
                out.write("\n");
            }
            for (int i = 1; i < 11; i++){
                for (int j = 1; j < 11; j++){
                    out.write(shipsEnemy[i][j]+";");
                }
                out.write("\n");
            }
        }
        catch (IOException ex) {
            System.out.println("Błąd podczas zapisywaniu gry");
        }
    }
    //Zapisuje gre w pliku mozliwym do wczytania z menu
    private void saveCurrentGame(String name){
        for (int i = 1; i < 4; i++){
            if (!new File("SavedGame" + i + ".txt").exists()){
                name = "SavedGame" + i + ".txt";
                break;
            }
        }
        if (name == "save"){
            JOptionPane.showMessageDialog(null, "Wszystkie dostępne zapisy są zajęte.");
        }
        else {
            try (FileWriter out = new FileWriter(name)) {
                for (int i = 1; i < 11; i++) {
                    for (int j = 1; j < 11; j++) {
                        if (f1 != null && shipsPlayer[i][j] == shipsPlayer[f1.getI()][f1.getJ()]){
                            out.write("7" + ";");
                            f1 = null;
                        }
                        else{
                            out.write(shipsPlayer[i][j]+";");
                        }
                    }
                    out.write("\n");
                }
                for (int i = 1; i < 11; i++) {
                    for (int j = 1; j < 11; j++) {
                        out.write(shipsEnemy[i][j] + ";");
                    }
                    out.write("\n");
                }
            } catch (IOException ex) {
                System.out.println("Błąd podczas zapisywaniu rozgrywki");
            }
        }
    }
    //Wczytuje zapisana gre
    private void loadGame(File file){
        if (file.getPath()=="setBoard.txt"){
            try(BufferedReader br = new BufferedReader(new FileReader(file));){
                String linia;
                int j = 1;
                while ((linia = br.readLine()) != null) {
                    String[] dane = linia.split(";");
                    for (int i = 0; i < dane.length; i++) {
                        shipsPlayer[j][i+1] = Integer.valueOf(dane[i]);
                    }
                    j++;
                }
            } catch (Exception e) {
                System.out.println("Błąd podczas załadowaniu zapisu");
            }
        }
        else{
            try(BufferedReader br = new BufferedReader(new FileReader(file));){
                String linia;
                int j1 = 1;
                int j2 = 1;
                int z = 1;
                while ((linia = br.readLine()) != null) {
                    String[] dane = linia.split(";");
                    if (z < 11){
                        for (int i = 0; i < dane.length; i++) {
                            shipsPlayer[z][i+1] = Integer.valueOf(dane[i]);
                        }
                        z++;
                    }
                    else{
                        for (int i = 0; i < dane.length; i++) {
                            shipsEnemy[j2][i+1] = Integer.valueOf(dane[i]);
                        }
                        j2++;
                    }
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        updateBoard();
    }
    //Resetuje plansze
    private void resetBoard(){
        shipsPlayer = new int[n][n];
        shipsEnemy = new int[n][n];
        f1 = null;
        f2 = null;
        shipSizes = new ArrayList<>();
        shipSizes.add(1);
        shipSizes.add(1);
        shipSizes.add(2);
        shipSizes.add(2);
        shipSizes.add(3);
        shipSizes.add(3);
        shipSizes.add(4);
        iloscStatkowKomputera = 16;
        for (int i = 1; i < n; i++)
        {
            for (int j = 1; j < n; j++)
            {
                style.styleButtonBoard(buttonsEnemy[i][j]);
                buttonsEnemy[i][j].setText("");
                style.styleButtonBoard(buttonsPlayer[i][j]);
                buttonsPlayer[i][j].setText("");
            }
        }
    }
    //Wypelnia statkami pole komputera
    private void fillEnemyBoard(){
        Random drawPos = new Random();
        Random drawDir = new Random();
        int i, j, z;
        int shipPos, shipSize;
        gameStarted = true;
        iloscStatkowKomputera = 16;
        while (!shipSizes.isEmpty()){
            i = 1+ drawPos.nextInt(10);
            j = 1+ drawPos.nextInt(10);
            z = 1- drawDir.nextInt(2);
            if(isAvailable(i,j, directions[z])){
                shipSize = shipSizes.get(0);
                if (directions[z] == "W poziomie") {
                    shipPos = j;
                    for (int x = 0;x < shipSize; x++){
                        shipsEnemy[i][shipPos++] = shipSize;
                    }
                }
                else{
                    shipPos = i;
                    for (int x = 0;x < shipSize; x++){
                        shipsEnemy[shipPos++][j] = shipSize;
                    }
                }
                shipSizes.remove(0);
            }
        }
    }
    //Sprawdza czy mozna ustawic statek w podanej pozycji
    private boolean isAvailable(int i, int j, String direction) {
        int shipSize = shipSizes.get(0);
        if (direction == "W poziomie") {
            int shipPos = j;
            for (int x = 1; x < shipSize; x++) {
                shipPos++;
                if (shipPos > 10 || shipsPlayer[i][shipPos] > 0) {
                    return false;
                }
            }
            return true;
        } else {
            int shipPos = i;
            for (int x = 1; x < shipSize; x++) {
                shipPos++;
                if (shipPos > 10 || shipsPlayer[shipPos][j] > 0) {
                    return false;
                }
            }
            return true;
        }
    }
    //Wcisniecie buttona na planszy
    public void buttonClicked(int i, int j){
        if(playerTurn){
            if (shipsEnemy[i][j] > 0 && shipsEnemy[i][j] != 8){
                style.styleButtonEnemyShipHit(buttonsEnemy[i][j]);
                shipsEnemy[i][j] = 9;
                iloscStatkowKomputera--;
            }
            else{
                style.styleButtonEnemyBoardHit(buttonsEnemy[i][j]);
                shipsEnemy[i][j] = 8;
                infoLabel.setText("Tura przeciwnika...");
                playerTurn = false;
            }
        }
    }
    //STRZAL BOTA
    public void computerHit(){
        //Sprawdza czy ma zaczynac strzal od trafionego wczesniej pola
        if (backToF1){
            scanFields(f1);
        }
        else {
            Random los = new Random();
            int index = los.nextInt(listaPol.iloscElementowWLiscie());
            if (f1 != null) shipsPlayer[f1.getI()][f1.getJ()] = 9;
            f1 = listaPol.getField(index);
            if (shipsPlayer[f1.getI()][f1.getJ()] < 8){
                if (shipsPlayer[f1.getI()][f1.getJ()] > 0){
                    shipsPlayer[f1.getI()][f1.getJ()] = 9;
                    iloscStatkowGracza--;
                    playerBoardHit(f1.getI(), f1.getJ());
                    listaPol.usunPoleZListy(f1);
                    scanFields(f1);
                }
                else{
                    shipsPlayer[f1.getI()][f1.getJ()] = 8;
                    playerBoardHit(f1.getI(), f1.getJ());
                    listaPol.usunPoleZListy(f1);
                    f1 = null;
                    infoLabel.setText("Twoja tura...");
                    playerTurn = true;
                }
            }
        }
    }
    //Skan pol wokol trafionego statku na polu gracza
    private void scanFields(Field f){
        fillFieldList(f);
        ArrayList<Field> directionToRemove = new ArrayList<>();
        for (Field field: direction) {
            Field temp = new Field(f.getI() + field.getI(), f.getJ() + field.getJ());
            if (!isAvailable(temp)){
                directionToRemove.add(field);
            }
            else if(shipsPlayer[temp.getI()][temp.getJ()] == 8 || shipsPlayer[temp.getI()][temp.getJ()] == 9){
                directionToRemove.add(field);
            }
            else if(!listaPol.contains(temp)){
                directionToRemove.add(field);
            }
        }
        //Kasuje kierunki w ktore nie mozna strzelic
        direction.removeAll(directionToRemove);
        if (!direction.isEmpty()){
            Random los = new Random();
            //Losuje kierunek w ktorym komputer ma strzelic
            int kierunek = los.nextInt(direction.size());
            f2 = new Field(f.getI() + direction.get(kierunek).getI(),f.getJ() + direction.get(kierunek).getJ());
            while(!containsField(f2)){
                los = new Random();
                kierunek = los.nextInt(direction.size());
                f2 = new Field(f.getI() + direction.get(kierunek).getI(),f.getJ() + direction.get(kierunek).getJ());
            }
            //Jezeli kolejny strzal trafil
            if (isShipHit(shipsPlayer[f2.getI()][f2.getJ()])){
                backToF1 = false;
                System.out.println("hit");
                System.out.println("f2 = i: " + f2.getI() + " j: " + f2.getJ());
                shipsPlayer[f2.getI()][f2.getJ()] = 9;
                iloscStatkowGracza--;
                playerBoardHit(f2.getI(), f2.getJ());
                listaPol.usunPoleZListy(f2);
                scanFurther(f2, direction.get(kierunek));
            }
            //Jezeli kolejny strzal nie trafil
            else{
                shipsPlayer[f2.getI()][f2.getJ()] = 8;
                playerBoardHit(f2.getI(), f2.getJ());
                listaPol.usunPoleZListy(f2);
                backToF1 = true;
                f2 = f;
                infoLabel.setText("Twoja tura...");
                playerTurn = true;
            }
        }
        else{
            f1 = null;
            f2 = null;
            backToF1 = false;
            computerHit();
        }
    }
    //Skanuje dalej w kierunku kolejnego trafionego statku
    private void scanFurther(Field f, Field way){
        f2 = new Field(f.getI() + way.getI(),f.getJ() + way.getJ());
        if (!isAvailable(f2)){
            if (f1 != null) backToF1 = true;
            infoLabel.setText("Twoja tura...");
            playerTurn = true;
        }
        else if (isShipHit(shipsPlayer[f2.getI()][f2.getJ()])){
            backToF1 = false;
            listaPol.usunPoleZListy(f2);
            shipsPlayer[f2.getI()][f2.getJ()] = 9;
            iloscStatkowGracza--;
            playerBoardHit(f2.getI(), f2.getJ());
            scanFurther(f2, way);
        }
        else{
            listaPol.usunPoleZListy(f2);
            shipsPlayer[f2.getI()][f2.getJ()] = 8;
            playerBoardHit(f2.getI(), f2.getJ());
            if (f1 != null) backToF1 = true;
            infoLabel.setText("Twoja tura...");
            playerTurn = true;
        }
    }
    //Dodaje do listy 4 mozliwe kierunki do strzalu
    private void fillFieldList(Field f){
        direction.add(new Field(-1, 0));
        direction.add(new Field(1, 0));
        direction.add(new Field(0, -1));
        direction.add(new Field(0, 1));
    }
    //Sprawdza czy Field nie wychodzi poza tablice (plansza gracza)
    private boolean isAvailable(Field f){
        if(f.getI() >= 11 || f.getI() <= 0 || f.getJ() >= 11 || f.getJ() <= 0){
            return false;
        }
        else{
            return true;
        }
    }
    //Sprawdza czy strzal byl trafiony
    private boolean isShipHit(int s){
        if (s < 8 && s > 0){
            return true;
        }
        else{
            return false;
        }
    }
    //Sprawdza czy podany Field wystepuje w liscie listaPol
    private boolean containsField(Field f){
        if (listaPol.contains(f)){
            return true;
        }
        else{
            return false;
        }
    }
    //Sprawdza kto wygral
    public void winner(){
        if (iloscStatkowGracza <= 0){
            JOptionPane.showMessageDialog(null, "Przegrales :(");
            sesja.interrupt();
        }
        else if (iloscStatkowKomputera <= 0){
            JOptionPane.showMessageDialog(null,"WYGRALES!!!");
            sesja.interrupt();
        }
    }
    //Ustawia style dla buttonow po wystrzale
    private void playerBoardHit(int i, int j){
        if (shipsPlayer[i][j] == 9){
            style.styleButtonEnemyShipHit(buttonsPlayer[i][j]);
        }
        else if(shipsPlayer[i][j] == 8){
            style.styleButtonEnemyBoardHit(buttonsPlayer[i][j]);
        }
        else if(shipsPlayer[i][j] == 7){
            style.styleButtonEnemyShipHit(buttonsPlayer[i][j]);
            f1 = new Field(i,j);
        }
    }
    @Override
    public void actionPerformed(ActionEvent e){
        Object generator = e.getSource();
        if(generator == back) {
            game = false;
            sesja.interrupt();
            gameStarted = false;
            saveCurrentGame();
            statki.MainPanel.cl.show(MainPanel.panelContent,"Main");
        }
        else if(generator == save){
            saveCurrentGame("save");
        }
    }
    @Override
    public void componentResized(ComponentEvent e) {}
    @Override
    public void componentMoved(ComponentEvent e) {}
    @Override
    public void componentShown(ComponentEvent e) {
        resetBoard();
        preGame.resetBoard();
        game = true;
        for (int i: shipSizes) {
            iloscStatkowGracza += i;
        }
        fillEnemyBoard();
        sesja = new SessionAgainstComputer(this);
        sesja.start();
        gameStarted = true;
        if (savedGames.shouldLoad()){
            resetBoard();
            loadGame(savedGames.getLoadFile());
        }
        else if(mainMenuPanel.continueGame){
            resetBoard();
            loadGame(new File("currentGame.txt"));
        }
        else{
            loadGame(new File("setBoard.txt"));
        }
        updateBoard();
    }
    @Override
    public void componentHidden(ComponentEvent e) {
        if (gameStarted){
            sesja.interrupt();
            game = false;
            saveCurrentGame();
            gameStarted = false;
        }
        resetBoard();
    }
}
