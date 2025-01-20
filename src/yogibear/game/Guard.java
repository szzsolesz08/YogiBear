package yogibear.game;

import java.awt.*;

public class Guard extends Sprite {
    private char direction;

    public Guard(int x, int y, int width, int height, Image image, int speed) {
        super(x, y, width, height, image, speed);
    }


    public void move() {
        if (direction == '1') { //Horizontal movement
            setX(x + speed);
            if (x + width >= 800 || x <= 0) {
                invertSpeed();
            }
        } else if (direction == '2') { //Vertical movement
            setY(y + speed);
            if (y + height >= 600 || y <= 0) {
                invertSpeed();
            }
        }
    }

    private void invertSpeed() {
        speed = -speed;
    }

    public boolean isPlayerInSight(Player player) {
        Rectangle guardHitBox = new Rectangle(this.x, this.y, this.width, this.height);
        Rectangle playerHitBox = new Rectangle(player.getX(), player.getY(), player.getWidth(), player.getHeight());

        return  guardHitBox.intersects(playerHitBox);
    }

    public void setDirection(char direction) {
        this.direction = direction;
    }
}
