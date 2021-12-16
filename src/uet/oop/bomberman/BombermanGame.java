package uet.oop.bomberman;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import uet.oop.bomberman.entities.*;
import uet.oop.bomberman.entities.boom.Bom;
import uet.oop.bomberman.entities.enermy.Balloon;
import uet.oop.bomberman.entities.enermy.Enermy;
import uet.oop.bomberman.graphics.Sprite;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BombermanGame extends Application {

    public static final int WIDTH = 31;
    public static final int HEIGHT = 13;

    private GraphicsContext gc;
    private Canvas canvas;
    private static List<Entity> entities = new ArrayList<>();
    private List<Entity> stillObjects = new ArrayList<>();
    private static List<Entity> fixedEntities = new ArrayList<>();
    private static List<Entity> explosions = new ArrayList<>();

    private Label caption;
    private Button buttonState;

    private Bomber bomberman;
    private static Bom bomb;
    private Portal portal;

    private int level = 5;


    public static void main(String[] args) {
        Application.launch(BombermanGame.class);
    }

    @Override
    public void start(Stage stage) throws IOException {
        // Tao Canvas
        canvas = new Canvas(Sprite.SCALED_SIZE * WIDTH, Sprite.SCALED_SIZE * HEIGHT);
        canvas.setLayoutX(0);
        canvas.setLayoutY(40);
        gc = canvas.getGraphicsContext2D();

        // Tạo laber, button
        caption = new Label();
        buttonState = new Button("Play Again");
        buttonState.setPrefHeight(40);
        buttonState.setPrefWidth(BombermanGame.WIDTH * Sprite.SCALED_SIZE);
        buttonState.setDisable(true);
        buttonState.setStyle("-fx-font: 16 arial;" + "-fx-focus-color: transparent;" + "-fx-faint-focus-color: transparent;"
        + "-fx-font-weight: bold;" + "-fx-opacity: 1.0;");

        buttonState.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle( MouseEvent event ) {
                try {
                    createMap();
                    buttonState.setDisable(true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        // Tao root container
        Group root = new Group();
        root.getChildren().addAll(canvas, caption, buttonState);

        // Tao scene
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

        scene.setOnKeyPressed(
                createEventGame()
        );


        timer.start();

        // Tạo map
        createMap();


    }

    public void createMap() throws IOException {
        // reset
        fixedEntities.clear();
        entities.clear();
        canvas.setDisable(false);

        buttonState.setText("Level " + level);

        // read file
        FileReader fileReader = new FileReader("res/levels/Level" + level + ".txt");
        BufferedReader buf = new BufferedReader(fileReader);
        int rowCount = 0;
        String line;
        while ((line = buf.readLine()) != null) {

            for (int i = 0; i < line.length() ; i++) {
                if (line.charAt(i) == '#') {
                    fixedEntities.add(new Wall(i , rowCount , Sprite.wall.getFxImage()));
                } else {
                    fixedEntities.add(new Grass(i , rowCount , Sprite.grass.getFxImage()));
                }
            }
            for (int i = 0; i < line.length() ; i++) {
                switch (line.charAt(i)) {
                    case '*':
                        entities.add(new Brick(i , rowCount , Sprite.brick.getFxImage()));
                        break;
                    case 'x':
                        portal = new Portal(i , rowCount , Sprite.portal.getFxImage());
                        entities.add(new Brick(i , rowCount , Sprite.brick.getFxImage()));
                        break;
                    case 'p':
                        bomberman = new Bomber(i , rowCount , Sprite.player_right.getFxImage());
                        entities.add(bomberman);
                        break;
                    case '1':
                        entities.add(new Balloon(i , rowCount , Sprite.balloom_left1.getFxImage()));
                        break;
//
                }
            }
            rowCount++;
        }
    }

    public void update() throws IOException {
        entities.forEach(Entity::update);
        Sprite.movingSprite(Sprite.balloom_left1, Sprite.balloom_left2, Sprite.balloom_left3, 1000, 3);
        if (bomberman.isRemoved()) {
            buttonState.setText("Game Over! Click here to play again");
            buttonState.setDisable(false);
        }


        // update entities
        for (int i = 0; i < entities.size(); i++) {
            Entity entity = entities.get(i);
            entity.update();
            if (entity.isRemoved()) {
                entities.remove(i);
                canvas.setDisable(true);
            }
        }

        if (isWinGame()) {
            if(level == 5){
                buttonState.setText("Win Game!");
            }
            else{
                level++;
                createMap();
                canvas.setDisable(true);
            }

        }


        // update bom
        if (bomb != null) {
            bomb.update();
        }
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

    public void render() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
//        stillObjects.forEach(g -> g.render(gc));
        fixedEntities.forEach(g -> g.render(gc));
        portal.render(gc);

        entities.forEach(g -> g.render(gc));


        // bom
        if (bomb != null) {
            bomb.render(gc);
        }
        if (!explosions.isEmpty()) {
            explosions.forEach(g -> g.render(gc));
        }

    }

    private boolean isWinGame() {
        // extirpated full enemy && found portal
        for (Entity entity : entities) {
            if (entity instanceof Enermy) {
                return false;
            }
        }
        return (bomberman.getX() == portal.getX() && bomberman.getY() == portal.getY());
    }


    public EventHandler<KeyEvent> createEventGame() {
        return new EventHandler<KeyEvent>() {
            public void handle(KeyEvent e) {
                // Điều chỉnh các hướng
                if(e.getCode() == KeyCode.LEFT ){
                    bomberman.setDirection(Entity.Direction.LEFT);
                }
                else if (e.getCode() == KeyCode.RIGHT){
                    bomberman.setDirection(Entity.Direction.RIGHT);

                }
                else if(e.getCode() == KeyCode.DOWN){
                    bomberman.setDirection(Entity.Direction.DOWN);

                }
                else if (e.getCode() == KeyCode.UP){
                    bomberman.setDirection(Entity.Direction.UP);

                }

                // Đặt boom
                if (e.getCode() == KeyCode.SPACE && bomb == null) { //  && bomberman.isAlive()
                    bomb = new Bom(bomberman.getX() / Sprite.SCALED_SIZE ,
                            bomberman.getY() / Sprite.SCALED_SIZE ,
                            Sprite.bomb.getFxImage());

                }


            }
        };
    }

    public static Entity getEntity( int x , int y ) {
        for (Entity e : entities) {
            if (e.compareCoordinate(x , y)) return e;
        }
        if (bomb != null) {
            if (bomb.compareCoordinate(x , y)) return bomb;
        }
        for (Entity e : fixedEntities) {
            if (e.compareCoordinate(x , y)) return e;
        }
        return null;
    }

    public static void bombExplode( List<Entity> exs ) {
        bomb = null;
        explosions = exs;
    }

    public static Entity getEnemy( int x , int y ) {
        for (Entity e : entities) {
            if (e.compareCoordinate(x , y) && !(e instanceof Bomber)) return e;
        }
        return null;
    }
}
