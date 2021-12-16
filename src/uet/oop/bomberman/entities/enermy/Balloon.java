package uet.oop.bomberman.entities.enermy;

import javafx.scene.image.Image;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.enermy.Enermy;
import uet.oop.bomberman.graphics.Sprite;

public class Balloon extends Enermy {
    public static int TIME_MOVE = 200;
    public Balloon(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
        this.timeCountDown = TIME_MOVE;
    }

    @Override
    public void update() {
        if (timeCountDown > 0){
            timeCountDown--;
        }else {
            moving();
            timeCountDown = TIME_MOVE;
        }
    }

    private void moving() {
        int speed = 32;
        direction = this.randomDirection();
        if (direction == 0) { // right
            if (isCanMove(x + speed , y)) {
                x += speed;
                img = Sprite.balloom_right1.getFxImage();
            }
        } else if (direction == 1) { // left
            if (isCanMove(x - speed , y)) {
                x -= speed;
                img = Sprite.balloom_left1.getFxImage();
            }
        } else if (direction == 2) { // up
            if (isCanMove(x , y - speed)) {
                y -= speed;
                img = Sprite.balloom_left1.getFxImage();
            }
        } else if (direction == 3) { // down
            if (isCanMove(x , y + speed)) {
                y += speed;
                img = Sprite.balloom_right1.getFxImage();
            }
        }
    }

    private boolean isCanMove( int x , int y ) {
        Entity entity = BombermanGame.getEntity(x , y);
        if (entity instanceof Enermy) return false;
        if (entity == null) return true;
        return this.collide(entity);
    }
}
