package uet.oop.bomberman.entities;

import javafx.scene.image.Image;
import uet.oop.bomberman.graphics.Sprite;

public abstract class MoveEntity extends Entity {
    public static int DEFAULT_SPEED = Sprite.SCALED_SIZE;
    public MoveEntity(int x , int y , Image img ) {
        super(x , y , img);
    }

    /**
     * Kiểm tra xem đối tượng hiện đại có di chuyển được không
     * @param entity
     * @return true - có di chuyển được, false - không di chuyển được
     */
    protected boolean isAMoveEntity(Entity entity){
        return (entity instanceof Grass || entity instanceof MoveEntity);
    }
}
