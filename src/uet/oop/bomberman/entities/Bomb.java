package uet.oop.bomberman.entities;

import javafx.scene.image.Image;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.BombermanGame;

import java.util.ArrayList;
import java.util.List;

public class Bomb extends Entity {
    Entity entityLeft;
    Entity entityRight;
    Entity entityUp;
    Entity entityDown;
    Entity entityCenter;

    private int timeCountExplode = 100;
    private int xLeft;
    private int xRight;
    private int yTop;
    private int yBottom;

    List<Entity> explosions;

    public Bomb(int x , int y , Image img ) {
        super(x , y , img);
        explosions = new ArrayList<>();
    }

    /**
     * Hàm cập nhật liên tục
     */
    @Override
    public void update() {
        // Đối tượng ở các hướng
        entityCenter = BombermanGame.getEntity(x , y);
        entityLeft = BombermanGame.getEntity(xLeft , y);
        entityRight = BombermanGame.getEntity(xRight , y);
        entityUp = BombermanGame.getEntity(x , yTop);
        entityDown = BombermanGame.getEntity(x , yBottom);

        // Ví trị dịch tương ứng
        xLeft = x - Sprite.SCALED_SIZE;
        xRight = x + Sprite.SCALED_SIZE;
        yTop = y - Sprite.SCALED_SIZE;
        yBottom = y + Sprite.SCALED_SIZE;

        // Hiện thị vụ nổ
        if (timeCountExplode > 0) {
            timeCountExplode--;
        }
        else {
            addExplosive();
            BombermanGame.explosiveBomb(explosions);
            explosiveBomb();
        }

        // Hiệu ứng boom
        if (timeCountExplode < 20) {
            image = Sprite.bomb_2.getFxImage();
        } 
        else if (timeCountExplode < 70) {
            image = Sprite.bomb_1.getFxImage();
        }
    }

    /**
     * Vụ nổ boom và xóa các Entity dính boom
     */
    private void explosiveBomb() {
        if (entityCenter instanceof Brick
                || entityCenter instanceof MoveEntity) {
            entityCenter.remove();
        }
        if (entityRight instanceof Brick
                || entityRight instanceof MoveEntity) {
            entityRight.remove();
        }
        if (entityUp instanceof Brick
                || entityUp instanceof MoveEntity) {
            entityUp.remove();
        }
        if (entityLeft instanceof Brick
                || entityLeft instanceof MoveEntity) {
            entityLeft.remove();
        }
        if (entityDown instanceof Brick
                || entityDown instanceof MoveEntity) {
            entityDown.remove();
        }

    }

    /**
     * Thêm vụ nổ bom
     */
    private void addExplosive() {
        explosions.add(new Explosion(x / Sprite.SCALED_SIZE ,
                y / Sprite.SCALED_SIZE ,
                Sprite.bomb_exploded2.getFxImage()));
        if (!(entityUp instanceof Wall)) {
            explosions.add(new Explosion(x / Sprite.SCALED_SIZE ,
                    yTop / Sprite.SCALED_SIZE ,
                    Sprite.explosion_vertical2.getFxImage()));
        }
        if (!(entityDown instanceof Wall)) {
            explosions.add(new Explosion(x / Sprite.SCALED_SIZE ,
                    yBottom / Sprite.SCALED_SIZE ,
                    Sprite.explosion_vertical2.getFxImage()));
        }
        if (!(entityLeft instanceof Wall)) {
            explosions.add(new Explosion(xLeft / Sprite.SCALED_SIZE ,
                    y / Sprite.SCALED_SIZE ,
                    Sprite.explosion_horizontal2.getFxImage()));
        }
        if (!(entityRight instanceof Wall)) {
            explosions.add(new Explosion(xRight / Sprite.SCALED_SIZE ,
                    y / Sprite.SCALED_SIZE ,
                    Sprite.explosion_horizontal2.getFxImage()));
        }


    }
}
