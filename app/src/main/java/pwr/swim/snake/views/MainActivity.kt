package pwr.swim.snake.views

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.PersistableBundle
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import pwr.swim.snake.Enums.GameState
import pwr.swim.snake.engine.GameEngine
import pwr.swim.snake.engine.LightListener
import pwr.swim.snake.engine.SnakeDirectioner

class MainActivity : AppCompatActivity() {

    private lateinit var gameEngine :GameEngine
    private lateinit var gameSnakeView: SnakeView
    private lateinit var loseDialog: AlertDialog
    private val handler = Handler()
    private var highScore = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        this.window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        getHighScore()
        initLoseAlertDialog()

        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val accelerometer= sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        val lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
        val snakeDirectioner = SnakeDirectioner(accelerometer)
        val lightListener = LightListener()
        sensorManager.registerListener(snakeDirectioner,accelerometer,SensorManager.SENSOR_DELAY_GAME)
        sensorManager.registerListener(lightListener,lightSensor,SensorManager.SENSOR_DELAY_NORMAL)

        gameSnakeView = SnakeView(this,lightListener)

        gameEngine = GameEngine(snakeDirectioner)
        gameEngine.initGame()
        gameSnakeView.setViewMap(gameEngine.map)
        setContentView(gameSnakeView)
        startUpdateHandler()

    }

    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState!!.putInt(HIGH_SCORE_KEY,highScore)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        highScore = savedInstanceState!!.getInt(HIGH_SCORE_KEY,0)
    }

/*    override fun onPause() {
        super.onPause()
        gameEngine.gameState = GameState.PAUSED
        Log.d("Pause", "czy gra sie zatrzymuje")
    }

    override fun onResume() {
        super.onResume()
        gameEngine.gameState = GameState.RUNNING
        Log.d("Pause", "czy gra startuje")
    }*/
    override fun onDestroy() {
        super.onDestroy()
        saveHighScore()
    }

    private fun saveHighScore(){
        val sh = getSharedPreferences(SH_PREFS,Context.MODE_PRIVATE).edit()
        sh.putInt(HIGH_SCORE_KEY, highScore)
    }
    private fun getHighScore(){
        val sh = getSharedPreferences(SH_PREFS,Context.MODE_PRIVATE)
        highScore = sh.getInt(HIGH_SCORE_KEY,0)
    }

    private fun newGame(){
        gameEngine.initGame()
        startUpdateHandler()
    }

    private fun startUpdateHandler(){
        handler.postDelayed(object : Runnable {
            override fun run() {
                if (gameEngine.gameState == GameState.READY){
                    gameEngine.gameState = GameState.RUNNING
                    handler.postDelayed(this, UPDATE_DELAY)
                }
                if (gameEngine.gameState == GameState.PAUSED){
                    handler.postDelayed(this, UPDATE_DELAY)
                }
                if (gameEngine.gameState == GameState.RUNNING){
                    gameEngine.update()
                    handler.postDelayed(this, UPDATE_DELAY)
                }
                if (gameEngine.gameState == GameState.LOST){
                    onGameLost()
                }
                gameSnakeView.setViewMap(gameEngine.map)
                gameSnakeView.invalidate()

            }
        }, UPDATE_DELAY)
    }

    fun initLoseAlertDialog(){
        val builder = AlertDialog.Builder(this@MainActivity)

        builder.setTitle("LOST")

        builder.setPositiveButton("YES"){dialog, which ->
            newGame()
            hideDialog(dialog as AlertDialog)
        }

        builder.setNegativeButton("No"){dialog,which ->
            hideDialog(dialog as AlertDialog)
        }

        loseDialog = builder.create()
    }
    fun onGameLost(){
        val score = gameEngine.score
        if(highScore<score) highScore = score

        loseDialog.setMessage("Your score: $score \nHigh Score: $highScore \n Do you want replay?")
        loseDialog.show()
    }
    private fun hideDialog(dialog: AlertDialog){
        dialog.hide()
    }
    companion object{
        private val UPDATE_DELAY: Long = 125
        private val SH_PREFS = "SystemPrefs"
        private val HIGH_SCORE_KEY = "TOP_WYNIK"
    }
}