package uet.oop.bomberman.entities;

import javafx.scene.image.Image;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.graphics.Sprite;

public class Bomber extends MoveEntity {

    public Bomber(int x, int y, Image img) {
        super( x, y, img);
    }
    private Dir dir = Dir.EMPTY;
    public static int SPEED_MOVING = Sprite.SCALED_SIZE;


    public void setDir(Dir dir) {
        this.dir = dir;
    }

    /**
     * Hàm cập nhật liên tục
     */
    @Override
    public void update() {
        if(dir != Dir.EMPTY){
            moving();
        }

        checkAlive();
    }

    /**
     * Kiểm tra di chuyển được không
     * @param x
     * @param y
     * @return true - di chuyển được, false - không di chuyển được
     */
    public boolean checkMove(int x, int y){
        Entity entity = BombermanGame.getEntity(x , y);
        if (entity == null) return true;
        return this.isAMoveEntity(entity);
    }

    /**
     * Nếu đã chạm phải quân địch -> thua game
     */
    private void checkAlive() {
        Entity enemy = BombermanGame.getEnemy(x , y);
        if (enemy != null) {
            remove();
        }
    }

    /**
     * Di chuyển bomber theo các hướng
     */
    public void moving(){
        if (dir == Dir.RIGHT) {
            if (checkMove(x + SPEED_MOVING , y)) {
                x += SPEED_MOVING;
                image = Sprite.player_right.getFxImage();
            }
        }else if (dir == Dir.UP) {
            if (checkMove(x , y - SPEED_MOVING)) {
                y -= SPEED_MOVING;
                image = Sprite.player_up.getFxImage();
            }
        } else if (dir == Dir.DOWN) {
            if (checkMove(x , y + SPEED_MOVING)) {
                y += SPEED_MOVING;
                image = Sprite.player_down.getFxImage();
            }
        }
        else if (dir == Dir.LEFT) {
            if (checkMove(x - SPEED_MOVING , y)) {
                x -= SPEED_MOVING;
                image = Sprite.player_left.getFxImage();
            }
        }
        dir = Dir.EMPTY;
    }


}
