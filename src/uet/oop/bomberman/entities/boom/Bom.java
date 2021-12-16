package uet.oop.bomberman.entities.boom;

import javafx.scene.image.Image;
import uet.oop.bomberman.entities.Brick;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.MovingEntity;
import uet.oop.bomberman.entities.Wall;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.BombermanGame;

import java.util.ArrayList;
import java.util.List;

public class Bom extends Entity {
    private int timeToExplodes = 100; // 100 seconds
    private int xLeft;
    private int xRight;
    private int yTop;
    private int yBottom;
    Entity entity_left;
    Entity entity_right;
    Entity entity_up;
    Entity entity_down;
    Entity entity_center;

    List<Entity> explosions;

    public Bom( int x , int y , Image img ) {
        super(x , y , img);
        explosions = new ArrayList<>();
    }

    @Override
    public void update() {
        xLeft = x - Sprite.SCALED_SIZE;
        xRight = x + Sprite.SCALED_SIZE;
        yTop = y - Sprite.SCALED_SIZE;
        yBottom = y + Sprite.SCALED_SIZE;
        entity_left = BombermanGame.getEntity(xLeft , y);
        entity_right = BombermanGame.getEntity(xRight , y);
        entity_up = BombermanGame.getEntity(x , yTop);
        entity_down = BombermanGame.getEntity(x , yBottom);
        entity_center = BombermanGame.getEntity(x , y);
        if (timeToExplodes > 0) {
            timeToExplodes--;
        } else {
            addExplosion();
            BombermanGame.bombExplode(explosions);
            bomExplodes();
        }
        if (timeToExplodes < 20) {
            img = Sprite.bomb_2.getFxImage();
        } else if (timeToExplodes < 70) {
            img = Sprite.bomb_1.getFxImage();
        }
    }

    private void bomExplodes() {


        if (entity_left instanceof Brick || entity_left instanceof MovingEntity) {
            entity_left.remove();
        }
        if (entity_right instanceof Brick || entity_right instanceof MovingEntity) {
            entity_right.remove();
        }
        if (entity_up instanceof Brick || entity_up instanceof MovingEntity) {
            entity_up.remove();
        }
        if (entity_down instanceof Brick || entity_down instanceof MovingEntity) {
            entity_down.remove();
        }
        if (entity_center instanceof Brick || entity_center instanceof MovingEntity) {
            entity_center.remove();
        }
    }

    private void addExplosion() {
        if (!(entity_left instanceof Wall)) {
            explosions.add(new Explosion(xLeft / 32 , y / 32 , Sprite.explosion_horizontal2.getFxImage()));
        }
        if (!(entity_right instanceof Wall)) {
            explosions.add(new Explosion(xRight / 32 , y / 32 , Sprite.explosion_horizontal2.getFxImage()));
        }
        if (!(entity_up instanceof Wall)) {
            explosions.add(new Explosion(x / 32 , yTop / 32 , Sprite.explosion_vertical2.getFxImage()));
        }
        if (!(entity_down instanceof Wall)) {
            explosions.add(new Explosion(x / 32 , yBottom / 32 , Sprite.explosion_vertical2.getFxImage()));
        }
        explosions.add(new Explosion(x / 32 , y / 32 , Sprite.bomb_exploded2.getFxImage()));
    }
}
