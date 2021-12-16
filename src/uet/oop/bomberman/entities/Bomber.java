package uet.oop.bomberman.entities;

import javafx.scene.image.Image;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.graphics.Sprite;

public class Bomber extends MovingEntity {

    public Bomber(int x, int y, Image img) {
        super( x, y, img);
    }
    public static int SPEED_MOVING = Sprite.SCALED_SIZE;
    
    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    private Direction direction = Direction.EMPTY;

    @Override
    public void update() {
        if(direction != Direction.EMPTY){
            moving();
        }

        checkAlive();
    }

    public boolean checkMove(int x, int y){
        Entity entity = BombermanGame.getEntity(x , y);
        if (entity == null) return true;
        return this.collide(entity);
    }

    public void moving(){
        
        if (direction == Direction.RIGHT) {
            if (checkMove(x + SPEED_MOVING , y)) {
                x += SPEED_MOVING;
                img = Sprite.player_right.getFxImage();
            }
        }else if (direction == Direction.UP) {
            if (checkMove(x , y - SPEED_MOVING)) {
                y -= SPEED_MOVING;
                img = Sprite.player_up.getFxImage();
            }
        } else if (direction == Direction.DOWN) {
            if (checkMove(x , y + SPEED_MOVING)) {
                y += SPEED_MOVING;
                img = Sprite.player_down.getFxImage();
            }
        }
        else if (direction == Direction.LEFT) {
            if (checkMove(x - SPEED_MOVING , y)) {
                x -= SPEED_MOVING;
                img = Sprite.player_left.getFxImage();
            }
        }
        direction = Direction.EMPTY;
    }

    private void checkAlive() {
        Entity enemy = BombermanGame.getEnemy(x , y);
        if (enemy != null) {
            remove();
        }
    }
}
