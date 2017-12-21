package com.musicarray.codeclan.snakeproject;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.musicarray.codeclan.snakeproject.R;
import com.musicarray.codeclan.snakeproject.engine.GameEngine;
import com.musicarray.codeclan.snakeproject.enums.Direction;
import com.musicarray.codeclan.snakeproject.enums.GameState;
import com.musicarray.codeclan.snakeproject.views.SnakeView;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

    private GameEngine gameEngine;
    private SnakeView snakeView;
    private final Handler handler = new Handler();
    private final long updateDelay = 500;
    private float prevX, prevY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gameEngine = new GameEngine();
        gameEngine.initGame();
        snakeView = (SnakeView) findViewById(R.id.snake_view);
        snakeView.setOnTouchListener(this);
        snakeView.setSnakeViewMap(gameEngine.getMap());
        snakeView.invalidate();
        startUpdateHandler();


    }

    private void startUpdateHandler(){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                gameEngine.update();

                if(gameEngine.getCurrentGameState() == GameState.RUNNING){
                    handler.postDelayed(this, updateDelay);
                }

                if(gameEngine.getCurrentGameState() == GameState.LOST){
                    OnGameLost();
                }

                snakeView.setSnakeViewMap(gameEngine.getMap());
                snakeView.invalidate();
            }
        }, updateDelay);
    }

    private void OnGameLost() {
        Toast.makeText(this, "You Lost", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onTouch(View v,MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                prevX = event.getX();
                prevX = event.getY();

            break;
            case MotionEvent.ACTION_UP:
                float newX = event.getX();
                float newY = event.getY();

                if(Math.abs(newX - prevX) > Math.abs(newY - prevY)){
                    if(newX > prevX){
                        gameEngine.updateDirection(Direction.EAST);
                    }else {
                        gameEngine.updateDirection(Direction.WEST);
                    }
                } else {
                    if(newY > prevY){
                        gameEngine.updateDirection(Direction.SOUTH);

                    }else {
                        gameEngine.updateDirection(Direction.NORTH);

                    }

            }
            break;
        }
        return true;
    }
}
