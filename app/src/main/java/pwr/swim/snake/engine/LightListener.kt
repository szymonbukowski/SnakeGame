package pwr.swim.snake.engine

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import pwr.swim.snake.Enums.LightEnum

class LightListener():SensorEventListener{
    var light = 0f
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event!=null) {
            light = event.values[0]
        }
    }
    fun getLight():LightEnum{
        return if (light < MIN_LIGHT){
             LightEnum.DARK
        }else LightEnum.LIGHT
    }


    companion object{
        val MIN_LIGHT = 300
    }
}