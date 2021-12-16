package uet.oop.bomberman;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import uet.oop.bomberman.entities.*;
import uet.oop.bomberman.entities.Bomb;
import uet.oop.bomberman.entities.Balloon;
import uet.oop.bomberman.entities.Enemy;
import uet.oop.bomberman.graphics.Sprite;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BombermanGame extends Application {

    // Số ô
    public static final int WIDTH = 31;
    public static final int HEIGHT = 13;

    // Các đối tượng hiển thị
    private GraphicsContext gc;
    private Canvas canvas;

    // Đối tượng game
    private Bomber bomberman;
    private static Bomb bomb;
    private Portal portal;
    private Button buttonState;


    private static int level = 5;

    // Entity
    private static List<Entity> entities = new ArrayList<>();
    private static List<Entity> staticEntities = new ArrayList<>();
    private static List<Entity> explosions = new ArrayList<>();

    public static void main(String[] args) {
        Application.launch(BombermanGame.class);
    }

    /**
     * Hàm bắt đầu game
     * @param stage
     * @throws IOException
     */
    @Override
    public void start(Stage stage) throws IOException {
        // Tao Canvas, đặt kích thước
        canvas = new Canvas(Sprite.SCALED_SIZE * WIDTH, Sprite.SCALED_SIZE * HEIGHT);
        canvas.setLayoutX(0);
        canvas.setLayoutY(40);
        gc = canvas.getGraphicsContext2D();

        // Tạo button
        createButton();

        // Tao root container
        Group root = new Group();
        root.getChildren().addAll(canvas, buttonState);

        // Tạo scene
        Scene scene = new Scene(root);

        // Them scene vao stage
        stage.setScene(scene);
        stage.show();

        // Animation
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                render();
                try {
                    update();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        // Nhận các nút bấm
        scene.setOnKeyPressed(
                createEventGame()
        );
        timer.start();

        // Tạo map
        createMap();
    }

    /**
     * Tạo nút trạng thái và hiệu ứng cho nút
     */
    public void createButton(){
        buttonState = new Button();
        buttonState.setPrefHeight(40);
        buttonState.setPrefWidth(BombermanGame.WIDTH * Sprite.SCALED_SIZE);
        buttonState.setDisable(true);
        buttonState.setStyle("-fx-font: 16 arial;" + "-fx-focus-color: transparent;" + "-fx-faint-focus-color: transparent;"
                + "-fx-font-weight: bold;" + "-fx-opacity: 1.0;");

        buttonState.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle( MouseEvent event ) {
                try {
                    buttonState.setDisable(true);
                    createMap();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Tạo Map cho tương ứng với level
     * @throws IOException
     */
    public void createMap() throws IOException {
        // Reset lại các phần tử
        entities.clear();
        canvas.setDisable(false);
        staticEntities.clear();

        buttonState.setText("Level " + level);

        // Đọc dữ liệu từ file
        FileReader fileReader = new FileReader("res/levels/Level" + level + ".txt");
        BufferedReader buf = new BufferedReader(fileReader);

        int row = 0;
        String lineFile;
        while ((lineFile = buf.readLine()) != null) {
            // Thêm lớp Entity tĩnh số 1
            for (int i = 0; i < lineFile.length() ; i++) {
                if (lineFile.charAt(i) == '#') {
                    staticEntities.add(new Wall(i , row , Sprite.wall.getFxImage()));
                }
                else {
                    staticEntities.add(new Grass(i , row , Sprite.grass.getFxImage()));
                }
            }
            // Thêm lớp Entiry tĩnh số 2
            for (int i = 0; i < lineFile.length() ; i++) {
                if(lineFile.charAt(i) == '*'){
                    entities.add(new Brick(i , row , Sprite.brick.getFxImage()));
                }
                else if(lineFile.charAt(i) == 'x'){
                    portal = new Portal(i , row , Sprite.portal.getFxImage());
                    entities.add(new Brick(i , row , Sprite.brick.getFxImage()));

                }
                else if(lineFile.charAt(i) == 'p'){
                    bomberman = new Bomber(i , row , Sprite.player_right.getFxImage());
                    entities.add(bomberman);

                }
                else if(lineFile.charAt(i) == '1'){
                    entities.add(new Balloon(i , row , Sprite.balloom_left1.getFxImage()));
                }

            }
            row++;
        }
    }

    /**
     * Hàm cập nhật đối tượng mỗi second
     * @throws IOException
     */
    public void update() throws IOException {
        entities.forEach(Entity::update);
        Sprite.movingSprite(Sprite.balloom_left1,
                Sprite.balloom_left2, Sprite.balloom_left3,
                1000, 3);

        // Thua game
        if (bomberman.isRemoved()) {
            buttonState.setDisable(false);
            buttonState.setText("Game Over! Click here to play again");

        }

        if (isWin()) {
            if(level == 5){
                buttonState.setText("Win Game!");
            }
            else{
                // Nên Level
                level++;
                createMap();
                canvas.setDisable(true);
            }

        }

        // Cập nhật Entity
        for (int i = 0; i < entities.size(); i++) {
            Entity entity = entities.get(i);
            entity.update();

            // Xóa phần tử
            if (entity.isRemoved()) {
                entities.remove(i);
                canvas.setDisable(true);
            }
        }

        // Cập nhật bomb
        if (bomb != null) {
            bomb.update();
        }

        // Cập nhật vụ nổ
        if (!explosions.isEmpty()) {
            for (int i = 0; i < explosions.size(); i++) {
                Entity entity = explosions.get(i);
                entity.update();
                if (entity.isRemoved()) {
                    explosions.remove(i);
                }
            }
        }
    }

    /**
     * Hiển thị các đối tượng
     */
    public void render() {
        // Hiển thị các đối tượng
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        staticEntities.forEach(g -> g.render(gc));
        portal.render(gc);
        entities.forEach(g -> g.render(gc));

        // Hiển thị vụ nổ
        if (!explosions.isEmpty()) {
            explosions.forEach(g -> g.render(gc));
        }

        // Hiện thị boom
        if (bomb != null) {
            bomb.render(gc);
        }
    }

    /**
     * Kiểm tra điều kiện chiến thắng
     * @return true - nếu thắng, false - nếu thua
     */
    private boolean isWin() {
        for (Entity entity : entities) {
            if (entity instanceof Enemy) {
                return false;
            }
        }
        return (bomberman.getX() == portal.getX() && bomberman.getY() == portal.getY());
    }

    /**
     * Tạo các Event cho game
     * @return KeyEvent
     */
    public EventHandler<KeyEvent> createEventGame() {
        return new EventHandler<KeyEvent>() {
            public void handle(KeyEvent e) {
                // Điều chỉnh các hướng
                if(e.getCode() == KeyCode.LEFT ){
                    bomberman.setDir(Entity.Dir.LEFT);
                }
                else if (e.getCode() == KeyCode.RIGHT){
                    bomberman.setDir(Entity.Dir.RIGHT);

                }
                else if(e.getCode() == KeyCode.DOWN){
                    bomberman.setDir(Entity.Dir.DOWN);

                }
                else if (e.getCode() == KeyCode.UP){
                    bomberman.setDir(Entity.Dir.UP);

                }

                // Đặt boom
                if (e.getCode() == KeyCode.SPACE && bomb == null) {
                    bomb = new Bomb(bomberman.getX() / Sprite.SCALED_SIZE ,
                            bomberman.getY() / Sprite.SCALED_SIZE ,
                            Sprite.bomb.getFxImage());

                }


            }
        };
    }

    /**
     * Lấy ra đối tượng tương ứng với tọa độ
     * @param x
     * @param y
     * @return Đối tượng
     */
    public static Entity getEntity( int x , int y ) {
        for (Entity entity : entities) {
            if (entity.compareLocation(x , y))
                return entity;
        }
        if (bomb != null) {
            if (bomb.compareLocation(x , y))
                return bomb;
        }
        for (Entity entity : staticEntities) {
            if (entity.compareLocation(x , y))
                return entity;
        }
        return null;
    }

    /**
     * Thêm các đối tượng vụ lổ
     * @param explos vụ nổ thêm vào
     */
    public static void explosiveBomb(List<Entity> explos ) {
        explosions = explos;
        bomb = null;

    }

    /**
     * Lấy ra đối tượng là quan địch
     * @param x
     * @param y
     * @return
     */
    public static Entity getEnemy( int x , int y ) {
        for (Entity entity : entities) {
            if (entity.compareLocation(x , y)
                    && !(entity instanceof Bomber))
                return entity;
        }
        return null;
    }
}
