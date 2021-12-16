package uet.oop.bomberman.entities;
import javafx.scene.image.Image;

public class Explosion extends Entity {
    private int timeDisappear = 32;
    public Explosion( int x , int y , Image img ) {
        super(x , y , img);
    }

    /**
     * Hàm cập nhật liên tục
     */
    @Override
    public void update() {
        if (timeDisappear > 0) {
            timeDisappear--;
        }
        else {
            this.remove();
        }
    }
}
