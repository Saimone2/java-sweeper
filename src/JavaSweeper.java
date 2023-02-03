import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import sweeper.*;
import sweeper.Box;


public class JavaSweeper extends JFrame {

    private Game game;
    private JPanel panel;
    private JLabel label;
    private final int IMAGE_SIZE = 50;

    public static void main(String[] args) {
        new JavaSweeper();
    }

    private JavaSweeper() {
        game = new Game(Complexity.EASY);
        game.start();
        setImages();
        initLabel();
        initPanel();
        initFrame();
    }

//    String[] items = {
//            "Easy",
//            "Normal",
//            "Hard"
//    };

//    private void initComplexityBox() {
//        Font font = new Font("Arial", Font.BOLD, 16);
//        JComboBox comboBox = new JComboBox(items);
//        comboBox.setSize(IMAGE_SIZE * 2, 26);
//        comboBox.setLocation((Ranges.getSize().x - 3) * IMAGE_SIZE + IMAGE_SIZE / 2, 8);
//        comboBox.setFont(font);
//        ActionListener actionListener = e -> {
//            String item = (String)comboBox.getSelectedItem();
//            game.setComplexity(item);
//        };
//        comboBox.addActionListener(actionListener);
//        add(comboBox);
//    }

    private void initLabel() {
        Font font = new Font("Arial", Font.PLAIN, 18);
        label = new JLabel("Welcome to Minesweeper!");
        label.setFont(font);
        label.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 0));
        add(label, BorderLayout.NORTH);
    }

    private void initPanel() {
        panel = new JPanel()
        {
            @Override
            protected void paintComponent(Graphics g)
            {
                super.paintComponent(g);
                for(Coord coord : Ranges.getAllCoords())
                {
                    g.drawImage((Image) game.getBox(coord).image, coord.x * IMAGE_SIZE, coord.y * IMAGE_SIZE, this);
                }
            }
        };

        panel.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                int x = e.getX() / IMAGE_SIZE;
                int y = e.getY() / IMAGE_SIZE;
                Coord coord = new Coord(x, y);
                if(e.getButton() == MouseEvent.BUTTON1) {
                    game.pressLeftButton(coord);
                }
                if(e.getButton() == MouseEvent.BUTTON2) {
                    game.start();
                }
                if(e.getButton() == MouseEvent.BUTTON3) {
                    game.pressRightButton(coord);
                }
                label.setText(getMessage());
                panel.repaint();
            }
        });

        panel.setPreferredSize(new Dimension(
                Ranges.getSize().x * IMAGE_SIZE,
                Ranges.getSize().y * IMAGE_SIZE));
        add(panel);
    }

    private String getMessage() {
        switch (game.getState()) {
            case BOMBED : return "GAME OVER!";
            case WINNER : return "CONGRATULATIONS!";
            default : return " ";
        }
    }

    private void initFrame() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Java Sweeper");
        setResizable(true);
        setVisible(true);
        pack();
        setLocationRelativeTo(null);
        setIconImage(getImage("icon"));
    }

    private void setImages() {
        for(Box box : Box.values()){
            box.image = getImage(box.name().toLowerCase());
        }
    }

    private Image getImage(String name) {
        String filename = "img/" + name + ".png";
        ImageIcon icon = new ImageIcon(getClass().getResource(filename));
        return icon.getImage();
    }
}