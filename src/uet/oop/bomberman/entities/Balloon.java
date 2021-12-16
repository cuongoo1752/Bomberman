package uet.oop.bomberman.entities;

import javafx.scene.image.Image;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.graphics.Sprite;

public class Balloon extends Enemy {
    public static int TIME_MOVE = 200;

    public Balloon(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
        this.timeCount = TIME_MOVE;
    }

    /**
     * Hàm cập nhật theo thời gian
     */
    @Override
    public void update() {
        if (timeCount > 0){
            timeCount--;
        }
        else {
            moving();
            timeCount = TIME_MOVE;
        }
    }

    /**
     * Kiểm tra xem có được không?
     * @param x
     * @param y
     * @return true - đi được, false - không đi được
     */
    private boolean checkMove(int x , int y ) {
        Entity entity = BombermanGame.getEntity(x , y);
        if (entity instanceof Enemy) 
            return false;
        if (entity == null) 
            return true;
        return this.isAMoveEntity(entity);
    }

    /**
     * Enemy di chuyen buoc tiep theo
     */
    private void moving() {
        dir = this.randomDir();
        
        // Di chuyển theo các hướng
        // Phải
        if (dir == 0) { 
            if (checkMove(x + DEFAULT_SPEED , y)) {
                image = Sprite.balloom_right1.getFxImage();
                x += DEFAULT_SPEED;
            }
        }
        // Trái
        else if (dir == 1) { 
            if (checkMove(x - DEFAULT_SPEED , y)) {
                image = Sprite.balloom_left1.getFxImage();
                x -= DEFAULT_SPEED;
            }
        }
        // Lên 
        else if (dir == 2) { 
            if (checkMove(x , y - DEFAULT_SPEED)) {
                image = Sprite.balloom_left1.getFxImage();
                y -= DEFAULT_SPEED;
            }
        }
        // Xuống
        else if (dir == 3) { 
            if (checkMove(x , y + DEFAULT_SPEED)) {
                image = Sprite.balloom_right1.getFxImage();
                y += DEFAULT_SPEED;
            }
        }
    }


}
