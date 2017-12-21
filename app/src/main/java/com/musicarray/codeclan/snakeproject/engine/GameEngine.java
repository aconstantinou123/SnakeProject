package com.musicarray.codeclan.snakeproject.engine;

import com.musicarray.codeclan.snakeproject.Classes.Coordinate;
import com.musicarray.codeclan.snakeproject.enums.Direction;
import com.musicarray.codeclan.snakeproject.enums.GameState;
import com.musicarray.codeclan.snakeproject.enums.TileType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by user on 12/21/17.
 */

public class GameEngine {
    public static final int gameWith = 28;
    public static final int gameHeight = 42;
    private List<Coordinate> walls = new ArrayList<>();
    private List<Coordinate> snake = new ArrayList<>();
    private List<Coordinate> apples = new ArrayList<>();
    private Direction currentDirection = Direction.EAST;
    private GameState currentGameState = GameState.RUNNING;
    private Random random = new Random();
    private boolean increaseTail = false;


    private Coordinate getSnakeHead(){
       return snake.get(0);
    }


    public GameEngine(){


    }

    public void initGame(){
        AddWalls();
        AddSnake();
        AddApples();

    }


    public void updateDirection(Direction newDirection){
        if(Math.abs(newDirection.ordinal() - currentDirection.ordinal()) %2 == 1){
            currentDirection = newDirection;
        }
    }

    public void update(){
        switch (currentDirection){
            case NORTH:
                updateSnake(0,-1);
                break;
            case EAST:
                updateSnake(1, 0);
                break;
            case SOUTH:
                updateSnake(0,1);
                break;
            case WEST:
                updateSnake(-1, 0);
                break;
        }

        for(Coordinate w : walls){
            if(snake.get(0).equals(w)){
                currentGameState = GameState.LOST;
            }
        }

        for(int i = 1; i < snake.size(); i++ ) {
            if (getSnakeHead().equals(i)) {
                currentGameState = GameState.LOST;
                return;
            }
        }

        Coordinate applesToRemove = null;
        for (Coordinate apple : apples) {
            if (getSnakeHead().equals(apple)) {
                applesToRemove = apple;
                increaseTail = true;
                }
            }
            if(applesToRemove != null){
            apples.remove(applesToRemove);
            AddApples();
            }
        }

    public TileType[][] getMap(){
        TileType[][] map = new TileType[gameWith][gameHeight];

        for(int x = 0; x < gameWith; x ++){
            for(int y = 0; y < gameHeight; y ++){
                map[x][y] = TileType.NOTHING;
            }
        }

        for(Coordinate wall : walls){
            map[wall.getX()][wall.getY()] = TileType.WALL;
        }

        for(Coordinate s : snake){
            map[s.getX()][s.getY()] = TileType.SNAKETAIL;
        }

        for(Coordinate a : apples){
            map[a.getY()][a.getY()] = TileType.APPLE;
        }
        map[snake.get(0).getX()][snake.get(0).getY()] = TileType.SNAKEHEAD;

        return map;


    }

    private void updateSnake(int x, int y){
        int newX = snake.get(snake.size() - 1).getX();
        int newY = snake.get(snake.size() - 1).getY();

        for(int i = snake.size() -1; i > 0; i--){
            snake.get(i).setX(snake.get(i-1).getX());
            snake.get(i).setY(snake.get(i-1).getY());
        }

        if(increaseTail){
            snake.add(new Coordinate(newX, newY));
            increaseTail = false;
        }

        snake.get(0).setX(snake.get(0).getX() + x);
        snake.get(0).setY(snake.get(0).getY() + y);

    }

    private void AddSnake() {
        snake.clear();
        snake.add(new Coordinate(7,7));
        snake.add(new Coordinate(6,7));
        snake.add(new Coordinate(5,7));
        snake.add(new Coordinate(4,7));
        snake.add(new Coordinate(3,7));
        snake.add(new Coordinate(2,7));
    }


    public void AddWalls(){
        for (int x = 0; x < gameWith; x++){
            walls.add(new Coordinate(x, 0));
            walls.add(new Coordinate(x, gameHeight -1));
        }

        for (int y = 0; y <gameHeight; y++){
            walls.add(new Coordinate(0, y));
            walls.add(new Coordinate(gameWith - 1, y));
        }
    }

    private void AddApples() {
        Coordinate coordinate = null;
        boolean added = false;
        while (!added){
            int x = 1 + random.nextInt(gameWith-2);
            int y = 1 + random.nextInt(gameHeight-2);

            coordinate = new Coordinate(x, y);
            boolean collision = false;
            for(Coordinate s : snake){
                if(s.equals(coordinate)){
                    collision = true;
                    break;
                }
            }

                for(Coordinate a : apples){
                    if(a.equals(coordinate)){
                        collision = true;
                        break;
                    }
                }
            added = !collision;
        }
        apples.add(coordinate);
    }


    public GameState getCurrentGameState() {
        return currentGameState;
    }
}
