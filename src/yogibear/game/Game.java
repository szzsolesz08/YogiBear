package yogibear.game;

import yogibear.data.HighScore;
import yogibear.data.ScoreBoard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Game extends JPanel {
    private final int FPS = 240;
    private final int PLAYER_X = 100;
    private final int PLAYER_Y = 100;
    public final int CHARACTER_WIDTH = 50;
    public final int CHARACTER_HEIGHT = 90;
    public final int CHARACTER_SPEED = 5;
    private Player player;
    private List<Guard> guards;
    private List<Obstacle> obstacles;
    private List<Basket> baskets;
    private Map currentMap;
    private int mapNumber = 1;
    private int score = 0;
    private Timer newFrameTimer;
    private Image background;
    private Image life;

    public Game() throws IOException {
        super();
        background = new ImageIcon("files/images/background.jpg").getImage();
        life = new ImageIcon("files/images/heart.png").getImage();
        Image playerImage = new ImageIcon("files/images/yogibear.png").getImage();
        player = new Player(PLAYER_X, PLAYER_Y, CHARACTER_WIDTH, CHARACTER_HEIGHT, playerImage, CHARACTER_SPEED);

        addEvents();

        restart();
        newFrameTimer = new Timer(1000 / FPS, new NewFrameListener());
        newFrameTimer.start();
    }

    public void restart() {
        try {
            currentMap = new Map(loadMap("files/maps/map" + mapNumber + ".txt"));
            player.setX(PLAYER_X);
            player.setY(PLAYER_Y);
            guards = new ArrayList<>(currentMap.loadGuards());
            obstacles = new ArrayList<>(currentMap.loadObstacles());
            baskets = new ArrayList<>(currentMap.loadBaskets());
        } catch (IOException ex) {
            Logger.getLogger(Game.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background, 0, 0, 800, 600, null);
        currentMap.draw(g);
        player.draw(g);
        for (int i = 0; i < player.getLifePoints(); i++) {
            g.drawImage(life, i * 20, 0, 20, 20, null);
        }

    }

    private static char[][] loadMap(String filePath) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        char[][] map = new char[lines.size()][];
        for (int i = 0; i < lines.size(); i++) {
            map[i] = lines.get(i).toCharArray();
        }
        return map;
    }

    class NewFrameListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent ae) {
            for (Guard guard: guards) {
                guard.move();

                if (guard.isPlayerInSight(player)) {
                    player.loseLife();
                    if (player.getLifePoints() == 0) {
                        try {
                            handleGameOver();
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
            baskets.removeIf(basket -> {
                if (basket.collides(player)) {
                    currentMap.catchBasket(basket);
                    score++;
                    return true;
                }
                return false;
            });
            if (!baskets.isEmpty()) {
                repaint();
            }
            else {
                mapNumber = mapNumber < 10 ? (mapNumber + 1) : 1;
                restart();
            }
        }
    }

    public void handleGameOver() throws SQLException {
        newFrameTimer.stop();
        String playerName = JOptionPane.showInputDialog(
                null,
                "Game Over!\nAdd meg a neved:",
                "Játék vége",
                JOptionPane.QUESTION_MESSAGE
        );

        if (playerName != null || !playerName.trim().isEmpty()) {
            ScoreBoard scoreBoard = new ScoreBoard();
            scoreBoard.putHighScore(playerName, score);

            StringBuilder highScores = dbToString(scoreBoard.getHighScores());

            JOptionPane.showMessageDialog(
                    null,
                    highScores,
                    "Top 10 Játékos",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
        else {
            JOptionPane.showMessageDialog(
                    null,
                    "Nem adtál meg nevet. Az eredményed nem került mentésre.",
                    "Figyelmeztetés",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    private StringBuilder dbToString(ArrayList<HighScore> db) {
        StringBuilder str = new StringBuilder();
        for (HighScore highScore: db) {
            str.append(highScore.getName()).append(": ").append(highScore.getScore()).append("\n");
        }
        return str;
    }

    private void addEvents() {
            this.getInputMap().put(KeyStroke.getKeyStroke("W"), "pressed w");
            this.getActionMap().put("pressed w", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    player.moveUp();
                    for (Obstacle obstacle: obstacles) {
                        if (obstacle.collides(player)){
                            player.moveDown();
                        }
                    }
                }
            });
            this.getInputMap().put(KeyStroke.getKeyStroke("S"), "pressed s");
            this.getActionMap().put("pressed s", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    player.moveDown();
                    for (Obstacle obstacle: obstacles) {
                        if (obstacle.collides(player)){
                            player.moveUp();
                        }
                    }
                }

            });
            this.getInputMap().put(KeyStroke.getKeyStroke("A"), "pressed a");
            this.getActionMap().put("pressed a", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    player.moveLeft();
                    for (Obstacle obstacle: obstacles) {
                        if (obstacle.collides(player)){
                            player.moveRight();
                        }
                    }
                }
            });
            this.getInputMap().put(KeyStroke.getKeyStroke("D"), "pressed d");
            this.getActionMap().put("pressed d", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    player.moveRight();
                    for (Obstacle obstacle: obstacles) {
                        if (obstacle.collides(player)){
                            player.moveLeft();
                        }
                    }
                }
            });
    }

    public void setScore(int score) {
        this.score = score;
    }
}
