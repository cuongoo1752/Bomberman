package uet.oop.bomberman.entities.boom;
import javafx.scene.image.Image;
import uet.oop.bomberman.entities.Entity;

public class Explosion extends Entity {
    private int time = 20;
    public Explosion( int x , int y , Image img ) {
        super(x , y , img);
    }

    @Override
    public void update() {
        if (time > 0) {
            time--;
        } else {
            this.remove();
        }
    }
}
