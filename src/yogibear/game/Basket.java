package yogibear.game;

import java.awt.*;

public class Basket extends Sprite{
    public Basket(int x, int y, int width, int height, Image image) {
        super(x, y, width, height, image, 0);
    }

    public boolean collides(Player player) {
        Rectangle obstacleHitBox = new Rectangle(this.x, this.y, this.width, this.height);
        Rectangle playerHitBox = new Rectangle(player.getX(), player.getY(), player.getWidth(), player.getHeight());

        return  obstacleHitBox.intersects(playerHitBox);
    }
}
