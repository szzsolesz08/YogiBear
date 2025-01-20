package yogibear.game;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Map {
    private char[][] layout;
    private List<Guard> guards;
    private List<Obstacle> obstacles;
    private List<Basket> baskets;
    public final int CHARACTER_WIDTH = 50;
    public final int CHARACTER_HEIGHT = 100;
    public final int CHARACTER_SPEED = 1;

    public Map(char[][] layout) {

        this.layout = layout;
        guards = new ArrayList<>();
        obstacles = new ArrayList<>();
        baskets = new ArrayList<>();
    }

    public void draw(Graphics g) {
        for (Guard guard: guards) {
            guard.draw(g);
        }
        for (Obstacle obstacle: obstacles) {
            obstacle.draw(g);
        }
        for (Basket basket: baskets) {
            basket.draw(g);
        }
    }

    public List<Guard> loadGuards() {
        Image obstacleImage = new ImageIcon("files/images/obstacle.png").getImage();
        Image guardImage = new ImageIcon("files/images/guard.png").getImage();
        for (int i = 0; i < layout.length; i++) {
            for (int j = 0; j < layout[i].length; j++) {
                char cell = getCell(j, i);
                if (cell == '1' || cell == '2') {
                    Guard guard = new Guard(j * 100, i * 100, CHARACTER_WIDTH, CHARACTER_HEIGHT, guardImage, CHARACTER_SPEED);
                    guard.setDirection(cell);
                    guards.add(guard);
                }
            }
        }

        return guards;
    }

    public List<Obstacle> loadObstacles() {
        Image obstacleImage = new ImageIcon("files/images/obstacle.png").getImage();
        for (int i = 0; i < layout.length; i++) {
            for (int j = 0; j < layout[i].length; j++) {
                char cell = getCell(j, i);
                if (cell == '#') {
                    Obstacle obstacle = new Obstacle(j * 100, i * 100, CHARACTER_WIDTH, CHARACTER_HEIGHT, obstacleImage);
                    obstacles.add(obstacle);
                }
            }
        }

        return obstacles;
    }

    public List<Basket> loadBaskets() {
        Image basketImage = new ImageIcon("files/images/basket.png").getImage();
        for (int i = 0; i < layout.length; i++) {
            for (int j = 0; j < layout[i].length; j++) {
                char cell = getCell(j, i);
                if (cell == '+') {
                    Basket basket = new Basket(j * 100, i * 100, CHARACTER_WIDTH / 2, CHARACTER_HEIGHT / 2, basketImage);
                    baskets.add(basket);
                }
            }
        }

        return baskets;
    }

    public void catchBasket(Basket basket) {
        baskets.remove(basket);
    }

    public char getCell(int x, int y) {
        return layout[y][x];
    }
}
