package uet.oop.bomberman.entities.enermy;

import javafx.scene.image.Image;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.MovingEntity;

import java.util.Random;

public class Enermy extends MovingEntity {
    protected Random random = new Random();
    protected int direction = -1;
    protected int timeCountDown;

    public Enermy(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
    }

    @Override
    public void update() {

    }

    protected int randomDirection(){
        return random.nextInt(4);
    }


}
