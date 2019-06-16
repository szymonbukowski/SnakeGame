package pwr.swim.snake.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.util.AttributeSet
import android.util.Log
import android.view.SurfaceView
import android.view.View
import pwr.swim.snake.Enums.LightEnum
import pwr.swim.snake.Enums.TileType
import pwr.swim.snake.engine.LightListener

class SnakeView(context: Context?, val light: LightListener) : View(context){



    private var mPaint = Paint()
    private lateinit var snakeViewMap: Array<Array<TileType>>

    fun setViewMap(map: Array<Array<TileType>>){
        snakeViewMap = map
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        var tileSizeX = width  / (snakeViewMap.size-1)
        var tileSizeY = height / (snakeViewMap[0].size-1)
        var score = 0

        for(x in 0 until  snakeViewMap.size){
            val theme = light.getLight()

            for(y in 0 until  snakeViewMap[0].size){
                var tile = snakeViewMap[x][y]
                //Log.d("canvas","printing tile $tile  $x/$y")
                when(theme){
                    LightEnum.LIGHT ->
                        when(tile){
                            TileType.FREE_SPACE -> {
                                mPaint.color = Color.WHITE
                            }
                            TileType.OBSTACLE -> {
                                mPaint.color = Color.BLACK
                            }
                            TileType.SNAKE_HEAD -> {
                                mPaint.color = Color.RED
                                score++
                            }
                            TileType.SNAKE_BODY -> {
                                mPaint.color = Color.GREEN
                                score++
                            }
                            TileType.FOOD -> {
                                mPaint.color = Color.YELLOW
                            }
                        }
                    LightEnum.DARK ->
                        when(tile){
                            TileType.FREE_SPACE -> {
                                mPaint.color = Color.BLACK
                            }
                            TileType.OBSTACLE -> {
                                mPaint.color = Color.WHITE
                            }
                            TileType.SNAKE_HEAD -> {
                                mPaint.color = Color.RED
                                score++
                            }
                            TileType.SNAKE_BODY -> {
                                mPaint.color = Color.GRAY
                                score++
                            }
                            TileType.FOOD -> {
                                mPaint.color = Color.YELLOW
                            }
                        }

                }
                canvas?.drawRect(x* tileSizeX *1f, y * tileSizeY *1f ,x*tileSizeX + tileSizeX*1f,y*tileSizeY*1f+tileSizeY, mPaint)
            }
//            mPaint.color = Color.GRAY
//            mPaint.textSize = 250f
//            canvas?.drawText(score.toString(),width/2f,height/2f,mPaint)
        }

    }

}