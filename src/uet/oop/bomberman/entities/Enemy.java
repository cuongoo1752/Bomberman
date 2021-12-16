package uet.oop.bomberman.entities;

import javafx.scene.image.Image;

import java.util.Random;

public class Enemy extends MoveEntity {
    protected int timeCount;
    protected int dir = -1;
    protected Random random = new Random();

    public Enemy(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
    }

    /**
     * Chọn ngẫu nhiên hướng để di chuyển cho quan địch
     * @return
     */
    protected int randomDir(){
        return random.nextInt(4);
    }

    @Override
    public void update() {

    }




}
