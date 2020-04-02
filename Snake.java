import java.util.*;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.stage.*;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Snake extends Application {
    private static int speed = 10;
    private static int foodcolor = 0;
    private static int width = 30;
    private static int height = 30;
    private static int foodX = 0;
    private static int score = -1;
    private static int foodY = 0;
    private static int Positionsize = 25;
    private static List<Position> snake = new ArrayList<>();
    private static Dir direction = Dir.left;
    private static boolean gameOver = false;
    private static long lastTick = 0;
    private static Random rand = new Random();
    
    public enum Dir {
        left, right, up, down
    }

    public static class Position {
        int x;
        int y;

        private Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

    }

    public void start(Stage primaryStage) {
        try {
            newFood(); 

            VBox root = new VBox(); 
            Canvas c = new Canvas(width * Positionsize, height * Positionsize); 
            GraphicsContext gc = c.getGraphicsContext2D(); 
            root.getChildren().add(c);

            
            new AnimationTimer() {

                public void handle(long now) {
                   
                    if (lastTick == 0) {
                        lastTick = now;
                        tick(gc);
                        return;
                    }
                    if (now - lastTick > 1000000000 / speed) {
                        lastTick = now;
                        tick(gc);
                    }
                }
            }.start();
            Scene scene = new Scene(root, width * Positionsize, height * Positionsize);
            scene.addEventFilter(KeyEvent.KEY_PRESSED, key -> {
                if (key.getCode() == KeyCode.W || key.getCode() == KeyCode.UP) {
                    direction = Dir.up;
                }
                if (key.getCode() == KeyCode.A || key.getCode() == KeyCode.LEFT) {
                    direction = Dir.left;
                }
                if (key.getCode() == KeyCode.S || key.getCode() == KeyCode.DOWN) {
                    direction = Dir.down;
                }
                if (key.getCode() == KeyCode.D || key.getCode() == KeyCode.RIGHT) {
                    direction = Dir.right;
                }

            });
            snake.add(new Position(width / 2, height / 2));
            snake.add(new Position(width / 2, height / 2));
            snake.add(new Position(width / 2, height / 2));
            primaryStage.setScene(scene);
            primaryStage.setTitle("Snake");
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static void tick(GraphicsContext gc) {
        if (gameOver) {
            gc.setFill(Color.RED);
            gc.setFont(new Font("", 50));
            gc.fillText("GAME OVER", 250, 250);
            return;
        }
        for (int i = snake.size() - 1; i >= 1; i--) {
            snake.get(i).x = snake.get(i - 1).x;
            snake.get(i).y = snake.get(i - 1).y;
        }
        switch (direction) {
            case up:
                snake.get(0).y--;
                if (snake.get(0).y < 0) {
                    gameOver = true;
                }
                break;
            case down:
                snake.get(0).y++;
                if (snake.get(0).y > height) {
                    gameOver = true;
                }
                break;
            case left:
                snake.get(0).x--;
                if (snake.get(0).x < 0) {
                    gameOver = true;
                }
                break;
            case right:
                snake.get(0).x++;
                if (snake.get(0).x > width) {
                    gameOver = true;
                }
                break;
        }
        if (foodX == snake.get(0).x && foodY == snake.get(0).y) {
            snake.add(new Position(-1, -1));
            newFood();
        }
        for (int i = 1; i < snake.size(); i++) {
            if (snake.get(0).x == snake.get(i).x && snake.get(0).y == snake.get(i).y) {
                gameOver = true;
                break;
            }
        }
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, width * Positionsize, height * Positionsize);
        gc.setFill(Color.WHITE);
        gc.setFont(new Font("", 30));
        gc.fillText("Score: " + score, 10, 30);
        Color cc = Color.WHITE;

        switch (foodcolor) {
            case 0:
                cc = Color.PURPLE;
                break;
            case 1:
                cc = Color.LIGHTBLUE;
                break;
            case 2:
                cc = Color.YELLOW;
                break;
            case 3:
                cc = Color.PINK;
                break;
            case 4:
                cc = Color.ORANGE;
                break;
        }
        gc.setFill(cc);
        gc.fillOval(foodX * Positionsize, foodY * Positionsize, Positionsize, Positionsize);
        for (Position c : snake) {
            gc.setFill(Color.LIGHTGREEN);
            gc.fillRect(c.x * Positionsize, c.y * Positionsize, Positionsize - 1, Positionsize - 1);
            gc.setFill(Color.GREEN);
            gc.fillRect(c.x * Positionsize, c.y * Positionsize, Positionsize - 2, Positionsize - 2);
        }
    }
    private static void newFood() {
        start: while (true) {
            foodX = rand.nextInt(width);
            foodY = rand.nextInt(height);

            for (Position c : snake) {
                if (c.x == foodX && c.y == foodY) {
                    continue start;
                }
            }
            foodcolor = rand.nextInt(5);
            score++;
            break;
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
}