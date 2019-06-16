package pwr.swim.snake.engine

import pwr.swim.snake.Enums.Direction
import pwr.swim.snake.Enums.TileType
import java.util.*

class Snake(val snakeDirectioner:SnakeDirectioner) {

    val body = LinkedList<Tile>()
    var foodTile = Tile(2,2)
    var currDirection = Direction.UP
    var collectedFood = false
    var collisionOccurred = false
    var score = 0

    fun checkCollision(newHead: Tile,map:Array<Array<TileType>>){
        if (map[newHead.posX][newHead.posY] == TileType.SNAKE_BODY
                    ||
            map[newHead.posX][newHead.posY] == TileType.OBSTACLE){
            collisionOccurred = true
        }
    }

    fun checkFoodCollect(newHead: Tile, foodTile: Tile){
        if (newHead == foodTile){
            grow()
        }
    }

    fun initSnake(startX:Int, startY:Int){
        body.clear()
        body.add(Tile(startX,startY))
        body.add(Tile(startX,startY+1))
        body.add(Tile(startX,startY+2))

        collectedFood = false
        collisionOccurred = false
        currDirection = Direction.UP
    }
    fun initFood(map:Array<Array<TileType>>){
        var isTaken = false
        var x:Int
        var y:Int
        do {
            x = (2 until GameEngine.ARENA_WIDTH -2).random()
            y = (2 until GameEngine.ARENA_HEIGHT -2).random()
            if (map[x][y]!=TileType.FREE_SPACE)isTaken = true
        }while(isTaken)
        foodTile = Tile(x,y)
        map[foodTile.posX][foodTile.posY] = TileType.FOOD

    }

    fun updateSnake(head:Tile, newHead: Tile,map:Array<Array<TileType>>){
        body.addFirst(newHead)
        map[newHead.posX][newHead.posY]=TileType.SNAKE_HEAD
        map[head.posX][head.posY]=TileType.SNAKE_BODY
    }
    fun updateScore(){
        score = body.size
    }
    fun grow(){
        collectedFood = true
    }

    fun move(map:Array<Array<TileType>>){
        val newDirection = snakeDirectioner.getDirection()
        if(!isOppositeToCurrDirection(newDirection)){
            currDirection = newDirection
        }
        val head = body.first
        var newHead = when(currDirection){
            Direction.UP -> {
                Tile(head.posX,head.posY-1)
            }
            Direction.DOWN -> {
                 Tile(head.posX,head.posY+1)
            }
            Direction.LEFT -> {
                 Tile(head.posX-1,head.posY)
            }
            Direction.RIGHT -> {
                 Tile(head.posX+1,head.posY)
            }
        }
        checkCollision(newHead,map)
        updateSnake(head,newHead,map)
        checkFoodCollect(newHead,foodTile)
        if(collectedFood){
            initFood(map)
            collectedFood = false
        }else{
            var tail = body.removeLast()
            map[tail.posX][tail.posY]=TileType.FREE_SPACE
        }
        updateScore()
    }
    private fun isOppositeToCurrDirection(direction: Direction):Boolean{
        return when(currDirection){
            Direction.DOWN -> direction == Direction.UP
            Direction.UP -> direction == Direction.DOWN
            Direction.LEFT -> direction == Direction.RIGHT
            Direction.RIGHT -> direction == Direction.LEFT
        }
    }
}