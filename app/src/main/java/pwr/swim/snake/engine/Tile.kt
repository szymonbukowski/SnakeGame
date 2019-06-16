package pwr.swim.snake.engine

class Tile(var posX: Int, var posY:Int){

    override fun equals(other: Any?): Boolean {
        val otherTile = other as Tile
        return (this.posX==otherTile.posX && this.posY == otherTile.posY)
    }

    override fun toString():String{
        return "($posX x $posX)"
    }
}