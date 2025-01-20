package yogibear.game;

import java.awt.*;
import java.awt.event.KeyAdapter;

public class Player extends Sprite {
    private int lifePoints;
    private final int startX;
    private final int startY;

    public Player(int x, int y, int width, int height, Image image, int speed) {
        super(x, y, width, height, image, speed);
        lifePoints = 3;
        startX = x;
        startY = y;
    }


    public void loseLife() {
        lifePoints--;
        if (lifePoints > 0) {
            resetPosition();
        }
    }

    private void resetPosition() {
        x = startX;
        y = startY;
    }

    public void moveUp() {
        if (y-speed > 0) y-=speed;
    }

    public void moveDown() {
        if (y+speed < 550) y+=speed;
    }

    public void moveLeft() {
        if (x-speed > 0) x-=speed;
    }

    public void moveRight() {
        if (x+speed < 750) x+=speed;
    }

    public int getLifePoints() {
        return lifePoints;
    }
}
