package uet.oop.bomberman.entities;

import javafx.scene.image.Image;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.graphics.Sprite;

public class Oneal extends Enemy {

    private int speed = DEFAULT_SPEED;

    public Oneal(int x, int y, Image img) {
        super(x, y, img);
        timeCount = 200;
    }

    /**
     * Hàm cập nhật liên tục
     */
    @Override
    public void update() {
        if (timeCount > 0) {
            timeCount--;
        } else {
            moving();
            timeCount = 200;
        }
    }

    /**
     * Kiểm tra phía trước có di chuyển được không
     *
     * @param x
     * @param y
     * @return
     */
    private boolean checkMove(int x, int y) {
        Entity entity = BombermanGame.getEntity(x, y);
        if (entity instanceof Enemy)
            return false;
        if (entity == null)
            return true;
        return this.isAMoveEntity(entity);
    }

    /**
     * Hàm di chuyển của đối Oneal
     */
    private void moving() {
        dir = findDirBomber();

        // Phải
        if (dir == 0) {
            if (checkMove(x + speed, y)) {
                image = Sprite.oneal_right1.getFxImage();
                x += speed;
            }
        }
        // Trái
        else if (dir == 1) {
            if (checkMove(x - speed, y)) {
                image = Sprite.oneal_left1.getFxImage();
                x -= speed;
            }
        }
        // Lên
        else if (dir == 2) {
            if (checkMove(x, y - speed)) {
                image = Sprite.oneal_left1.getFxImage();
                y -= speed;
            }
        }
        // Xuống
        else if (dir == 3) {
            if (checkMove(x, y + speed)) {
                image = Sprite.oneal_right1.getFxImage();
                y += speed;

            }
        }
    }

    /**
     * Tìm đường đi ngắn nhất theo độ dài đến bomberman
     * @return
     */
    public int findDirBomber() {

        Bomber bomber;
        if (BombermanGame.getBomber() != null) {
            int dirFindBomber = -1;
            int minDistance = 10000000;
            boolean isMove = false;
            bomber = (Bomber) BombermanGame.getBomber();

            // Kiểm tra xem đi được không
            // Phải
            if (checkMove(x + speed, y)) {
                isMove = true;
                int distance = distanceTwoPoint(bomber.getX(), bomber.getY(), x + speed, y);
                if(distance < minDistance ){
                    minDistance = distance;
                    dirFindBomber = 0;
                }
            }
            // Trái
            if (checkMove(x - speed, y)) {
                isMove = true;
                int distance = distanceTwoPoint(bomber.getX(), bomber.getY(), x - speed, y);
                if(distance < minDistance ){

                    minDistance = distance;
                    dirFindBomber = 1;
                }
            }
            // Lên
            if (checkMove(x, y - speed)) {
                isMove = true;
                int distance = distanceTwoPoint(bomber.getX(), bomber.getY(), x, y - speed);
                if(distance < minDistance ){
                    minDistance = distance;
                    dirFindBomber = 2;
                }
            }
            // Xuống
            if (checkMove(x, y + speed)) {
                isMove = true;
                int distance = distanceTwoPoint(bomber.getX(), bomber.getY(), x,y + speed);
                if(distance < minDistance ){
                    minDistance = distance;
                    dirFindBomber = 3;
                }

            }

            if (isMove) {
                return dirFindBomber;
            }


        }

        return this.randomDir();
    }

    public int distanceTwoPoint(int x1, int y1, int x2, int y2) {
        x1 = x1 / 32;
        x2 = x2 / 32;
        y1 = y1 / 32;
        y2 = y2 / 32;

        return (int) (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2);
    }


}
