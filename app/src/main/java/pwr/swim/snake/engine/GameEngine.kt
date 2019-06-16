package pwr.swim.snake.engine

import android.util.Log
import pwr.swim.snake.Enums.GameState
import pwr.swim.snake.Enums.TileType

class GameEngine(val snakeDirectioner: SnakeDirectioner) {

    var score: Int =0
    var gameState = GameState.PREPARING

    var map = Array(ARENA_WIDTH){Array(ARENA_HEIGHT){TileType.FREE_SPACE}}

    val snake = Snake(snakeDirectioner)


    fun initGame(){
        gameState = GameState.PREPARING
        clearMap()
        initWalls()
        snake.initSnake(ARENA_WIDTH/2, ARENA_HEIGHT* 3/4)
        snake.initFood(map)
        printSnake()
        gameState = GameState.READY
    }

    private fun clearMap(){
        for (x in 0 until map.size){
            for (y in 0 until map[0].size){
                map[x][y] = TileType.FREE_SPACE
            }
        }
    }
    private fun initWalls(){
        for(x in 0 until ARENA_WIDTH){
            map[x][0] = TileType.OBSTACLE
            map[x][ARENA_HEIGHT-1] = TileType.OBSTACLE
        }
        for(y in 0 until ARENA_HEIGHT){
            map[0][y] = TileType.OBSTACLE
            map[ARENA_WIDTH-1][y] = TileType.OBSTACLE
        }
    }

    fun printSnake(){
        for (tile in snake.body){
            map[tile.posX][tile.posY] = TileType.SNAKE_BODY
            Log.d("snake","body $tile")
        }
        val head =snake.body.first
        map[head.posX][head.posY] = TileType.SNAKE_HEAD
        Log.d("snake","head $head")
    }

    fun checkCollision(){

        if (snake.collisionOccurred){
            gameState = GameState.LOST
            score = snake.score
        }
    }


    fun update(){
        snake.move(map)

        checkCollision()

    }


    companion object{
        val ARENA_WIDTH  = 44
        val ARENA_HEIGHT = 88
    }


}