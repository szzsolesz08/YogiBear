package yogibear.ui;

import yogibear.game.Game;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class UI {
    private JFrame frame;
    private Game game;

    public UI() throws IOException {
        frame = new JFrame("Adventures of Yogi Bear");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JMenuBar menuBar = new JMenuBar();
        JMenu gameMenu = new JMenu("Játék");
        JMenuItem newGame = new JMenuItem("Új játék");
        newGame.addActionListener(e -> {
            game.restart();
            game.setScore(0);
        });
        gameMenu.add(newGame);
        menuBar.add(gameMenu);
        frame.setJMenuBar(menuBar);

        game = new Game();
        frame.getContentPane().add(game);

        frame.setPreferredSize(new Dimension(800, 640));
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
    }
}
