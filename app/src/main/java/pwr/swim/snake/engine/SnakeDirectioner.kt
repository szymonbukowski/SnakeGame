package pwr.swim.snake.engine

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.util.Log
import pwr.swim.snake.Enums.Direction

class SnakeDirectioner(val accelerometer: Sensor):SensorEventListener{

    //axis X positive = LEFT negative = Right values[0]
    //axis Y positive = DOWN negative = UP    values[1]

    var gravityX = 0.0f
    var gravityY = 0.0f
    var gravityZ = 0.0f

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null) {
            gravityX = event.values[0]
            gravityY = event.values[1]
            gravityZ = event.values[2]
        }
    }

    fun getDirection():Direction{
        Log.d("Engine","getting direction from sensor")
        return if(Math.abs(gravityX) > Math.abs(gravityY)){
            if(gravityX>0){
                Direction.LEFT
            }else{
                Direction.RIGHT
            }
        }else{
            if(gravityY>0){
                Direction.DOWN
            }else{
                Direction.UP
            }
        }
    }
}