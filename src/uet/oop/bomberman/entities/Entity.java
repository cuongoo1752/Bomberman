package uet.oop.bomberman.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import uet.oop.bomberman.graphics.Sprite;

public abstract class Entity {
    //Tọa độ X tính từ góc trái trên trong Canvas
    protected int x;

    //Tọa độ Y tính từ góc trái trên trong Canvas
    protected int y;

    public enum Dir {
        LEFT, RIGHT, UP, DOWN, EMPTY
    }

    protected Image image;
    protected boolean isRemoved = false;

    //Khởi tạo đối tượng, chuyển từ tọa độ đơn vị sang tọa độ trong canvas
    public Entity( int xUnit, int yUnit, Image img) {
        this.x = xUnit * Sprite.SCALED_SIZE;
        this.y = yUnit * Sprite.SCALED_SIZE;
        this.image = img;
    }

    public void render(GraphicsContext gc) {
        gc.drawImage(image, x, y);
    }
    public abstract void update();

    public void setX( int x ) {
        this.x = x;
    }

    public void setY( int y ) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isRemoved() {
        return isRemoved;
    }

    /**
     * So sánh vị trí 2 đối tượng dựa vào tạo độ x, y
     * @param x
     * @param y
     * @return
     */
    public boolean compareLocation(int x, int y){
        return this.x == x && this.y == y;
    }

    /**
     * Xóa đối tượng
     */
    public void remove() {
        isRemoved = true;
    }


}
