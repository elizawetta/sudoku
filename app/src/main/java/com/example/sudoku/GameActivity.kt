package com.example.sudoku

import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.Chronometer
import android.widget.GridLayout
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity


class GameActivity : AppCompatActivity() {
    var timeWhenStopped: Long? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        // Layout Parameters
        val LinLayoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        val ButtonLayoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 10f)
        var grid = findViewById<GridLayout>(R.id.grid)
        // creating grid
        for (i in 0..8){
            var linlay = LinearLayout(this)
            linlay.layoutParams = LinLayoutParams
            linlay.weightSum = 90f
            for (j in 0..8){
                var btn = Button(this)
                btn.text = i.toString() + j.toString()
                btn.layoutParams = ButtonLayoutParams
                btn.setBackgroundColor(getColor(R.color.white))
                linlay.addView(btn)
            }
            grid.addView(linlay)
        }
        // Chronometer
        var chronometer = findViewById<Chronometer>(R.id.chronometer)
        chronometer.setFormat("%m:%s")
        var startTime = SystemClock.elapsedRealtime()
        chronometer.setBase(startTime)
        chronometer.start()
        // Animation
        val animAlpha = AnimationUtils.loadAnimation(this, R.anim.alpha);
        // Find main buttons
        var delete_pressed = false
        var home = findViewById<ImageButton>(R.id.home);
        var delete = findViewById<ImageButton>(R.id.delete);
        var restart = findViewById<ImageButton>(R.id.restart);
        // Find Num Buttons
        var num_buttons = findViewById<LinearLayout>(R.id.num_buttons)
        var pressed: ImageButton? = null;
        var drawPressed: Int? = null;
        var num_1 = findViewById<ImageButton>(R.id.num_1);
        var num_2 = findViewById<ImageButton>(R.id.num_2);
        var num_3 = findViewById<ImageButton>(R.id.num_3);
        var num_4 = findViewById<ImageButton>(R.id.num_4);
        var num_5 = findViewById<ImageButton>(R.id.num_5);
        var num_6 = findViewById<ImageButton>(R.id.num_6);
        var num_7 = findViewById<ImageButton>(R.id.num_7);
        var num_8 = findViewById<ImageButton>(R.id.num_8);
        var num_9 = findViewById<ImageButton>(R.id.num_9);
        var nums = listOf(num_1, num_2, num_3, num_4, num_5, num_6, num_7, num_8, num_9)
        var draw_buttons = listOf(R.drawable.button_1,
            R.drawable.button_2,
            R.drawable.button_3,
            R.drawable.button_4,
            R.drawable.button_5,
            R.drawable.button_6,
            R.drawable.button_7,
            R.drawable.button_8,
            R.drawable.button_9)
        var draw_active_buttons = listOf(R.drawable.active_button_1,
            R.drawable.active_button_2,
            R.drawable.active_button_3,
            R.drawable.active_button_4,
            R.drawable.active_button_5,
            R.drawable.active_button_6,
            R.drawable.active_button_7,
            R.drawable.active_button_8,
            R.drawable.active_button_9)
        // OnClickListeners for Main Buttons
        home.setOnClickListener{
            it.startAnimation(animAlpha)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        delete.setOnClickListener{
            for (num in nums){
                num.setClickable(delete_pressed)
            }
            if (delete_pressed == false) {
                pressed?.setImageResource(drawPressed!!)
                drawPressed = null
                pressed = null
                delete.setImageResource(R.drawable.delete_active)
            }
            else {
                delete.setImageResource(R.drawable.delete)
            }
            delete_pressed = !delete_pressed
        }
        restart.setOnClickListener{
            for (num in nums){
                num.setClickable(true)
            }
            pressed?.setImageResource(drawPressed!!)
            drawPressed = null
            pressed = null

            it.startAnimation(animAlpha)
            delete.setImageResource(R.drawable.delete)

            chronometer.stop()
            startTime = SystemClock.elapsedRealtime()
            chronometer.setBase(startTime)
            chronometer.start()
        }
        // OnClickListeners for Num Buttons
        val oclBtnOk = View.OnClickListener() {
            if(it.id == pressed?.id){
                pressed!!.setImageResource(drawPressed!!)
                drawPressed = null
                pressed = null
            }
            else{
                for (i in 0..8) {
                    if (nums[i].id == it.id){
                        pressed?.setImageResource(drawPressed!!)
                        pressed = findViewById(it.id)
                        drawPressed = draw_buttons[i]
                        pressed!!.setImageResource(draw_active_buttons[i])
                        break
                    }
                }
            }
        }
        for (num in nums){
            num.setOnClickListener(oclBtnOk)
        }
    }

    override fun onPause() {
        super.onPause();
        var chronometer = findViewById<Chronometer>(R.id.chronometer)
        timeWhenStopped = chronometer.getBase() - SystemClock.elapsedRealtime();
        chronometer.stop()
    }

    override fun onResume() {
        super.onResume()
        if (timeWhenStopped != null){
            var chronometer = findViewById<Chronometer>(R.id.chronometer)
            chronometer.setBase(SystemClock.elapsedRealtime() + timeWhenStopped!!);
            chronometer.start()
        }
    }
}