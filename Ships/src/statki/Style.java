package statki;
import javax.swing.*;
import java.awt.*;
//funkcje stylizujace odpowiednie labele oraz buttony
public class Style {
    Style(){}
    public void styleButton(JButton button)
    {
        button.setFont(new Font("SansSerif",Font.BOLD,20));
        button.setBackground(Color.darkGray);
        button.setForeground(Color.white);
        button.setBorder(BorderFactory.createLineBorder(Color.lightGray,1));
        button.setPreferredSize(new Dimension(100,40));
    }
    public void styleLabel(JLabel label)
    {
        label.setForeground(Color.white);
        label.setFont(new Font("SansSerif",Font.BOLD,15));
    }
    public void styleButtonBoard(JButton button)
    {
        button.setBackground(Color.white);
        button.setBorderPainted(false);
        button.setEnabled(true);
        button.setMargin(new Insets(0, 0, 0, 0));
    }
    public void styleButtonBorders(JButton button)
    {
        button.setForeground(Color.gray);
        button.setFont(new Font("SansSerif",Font.BOLD,20));
        button.setEnabled(false);
    }
    public void styleButtonPlayerShip(JButton button){
        button.setBackground(new Color(163, 227, 255));
    }
    public void styleButtonEnemyBoardHit(JButton button)
    {
        button.setBackground(Color.red);
        button.setEnabled(false);
    }
    public void styleButtonEnemyShipHit(JButton button)
    {
        styleButtonEnemyBoardHit(button);
        button.setText("X");
        button.setFont(new Font("SansSerif",Font.BOLD,20));
    }
    public void styleInfoLabel(JLabel label)
    {
        label.setForeground(Color.darkGray);
        label.setFont(new Font("SansSerif",Font.TYPE1_FONT,20));
        label.setHorizontalAlignment(JLabel.CENTER);
    }
    public void styleButton2(JButton button)
    {
        button.setFont(new Font("SansSerif",Font.BOLD,20));
        button.setBackground(Color.darkGray);
        button.setForeground(Color.white);
        button.setBorder(BorderFactory.createLineBorder(Color.lightGray,1));
    }
}

