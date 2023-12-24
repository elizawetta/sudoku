package com.example.sudoku

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.Chronometer
import android.widget.GridLayout
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Space
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class GameActivity : AppCompatActivity() {
    var timeWhenStopped: Long? = null;
    var gameClass = sudoku()
    var pressed: ImageButton? = null;
    var drawPressed: Int? = null;
    var delete_pressed = false
    var n = 81 - 42;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        // Chronometer
        var chronometer = findViewById<Chronometer>(R.id.chronometer)
        chronometer.setFormat("%m:%s")
        var startTime = SystemClock.elapsedRealtime()
        chronometer.setBase(startTime)
        chronometer.start()
        // Mappers for ligtning
        var map_to = listOf(
            listOf(R.drawable.tho, R.drawable.tho_gray),
            listOf(R.drawable.tw, R.drawable.tw_gray),
                listOf(R.drawable.th, R.drawable.th_gray),
                    listOf(R.drawable.one, R.drawable.one_gray))
        // Nums
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
        var sudokuGrid = gameClass.generateSudoku(42)
        // Layout Parameters
        val LinLayoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        val ButtonLayoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 10f)
        val scale = this.getResources().getDisplayMetrics().density;
        val layoutParams_3 = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            (3f * scale).toInt())
        val layoutParams_2 = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            (2f * scale).toInt())
        val layoutParams_1 = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            (1f * scale).toInt())
        var grid = findViewById<GridLayout>(R.id.grid)
        // Grid Click Listener
        val gridClickListener = View.OnClickListener() {
            if (!delete_pressed){
                if(pressed != null) {
                    for (i in 0..8) {
                        if (nums[i].id == pressed!!.id) {
                            if ((it as Button).text == "") {
                                n -= 1
                            }
                            it.text = (i + 1).toString()
                            for (i in map_to){
                                if(i[0] == it.getTag()){
                                    it.setBackgroundResource(i[1])
                                    it.setTag(i[1])
                                }
                            }
                            break
                        }
                    }
                }
            }
            else if((it as Button).text != ""){
                it.text = ""
                n += 1
            }
            Log.d("Meow", n.toString())
            if(n == 0){
                var soltuion = mutableListOf<Array<Int>>()
                for (i in 0..18){
                    var line = mutableListOf<Int>()
                    if(i % 2 == 0) continue
                    var lin = grid.getChildAt(i)
                    for(j in 0..8){
                        var bt = (lin as LinearLayout).getChildAt(j)
                        bt = (bt as Button)
                        line.add(Integer.parseInt(bt.text as String))
                    }
                    soltuion.add(line.toTypedArray())
                }
                if(gameClass.checkSolution(soltuion.toTypedArray())){
                    chronometer.stop()
                    Toast.makeText(this, "WINNER!!!", 3).show()
                }
                else{
                    Toast.makeText(this, "ERROW!!!", 3).show()
                }
            }
        }
        // creating grid
        for (i in 0..18){
            if (i % 2 == 0){
                var sp = Space(this)
                if (i == 0 || i == 18){
                    sp.layoutParams = layoutParams_3
                }
                else if(i % 3 == 0){
                    sp.layoutParams = layoutParams_2
                }
                else{
                    sp.layoutParams = layoutParams_1
                }
                grid.addView(sp)
                continue
            }
            var linlay = LinearLayout(this)
            linlay.layoutParams = LinLayoutParams
            linlay.weightSum = 90f
            for (j in 0..8){
                var btn = Button(this)
                if(sudokuGrid[i / 2][j] == 0){
                    btn.text = ""
                    btn.setOnClickListener(gridClickListener)
                    btn.setTextColor(this.getColor(R.color.light_color))
                }
                else{
                    btn.text = sudokuGrid[i / 2][j].toString()
                    btn.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD))
                    btn.setTextColor(this.getColor(R.color.text))
                    btn.isClickable = false
                }
                btn.layoutParams = ButtonLayoutParams
                btn.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20f)
                if (j == 0){
                    btn.background = getDrawable(R.drawable.tho)
                    btn.setTag(R.drawable.tho)
                }
                else if(j == 8){
                    btn.background = getDrawable(R.drawable.th)
                    btn.setTag(R.drawable.th)
                }
                else if(j == 2 || j == 5){
                    btn.background = getDrawable(R.drawable.tw)
                    btn.setTag(R.drawable.tw)
                }
                else{
                    btn.background = getDrawable(R.drawable.one)
                    btn.setTag(R.drawable.one)
                }
                linlay.addView(btn)
            }
            grid.addView(linlay)
        }
        // Animation
        val animAlpha = AnimationUtils.loadAnimation(this, R.anim.alpha);
        // Find main buttons
        var home = findViewById<ImageButton>(R.id.home);
        var delete = findViewById<ImageButton>(R.id.delete);
        var restart = findViewById<ImageButton>(R.id.restart);
        // Find Num Buttons
        var num_buttons = findViewById<LinearLayout>(R.id.num_buttons)
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
            for (i in 0..18){
                if(i % 2 == 0) continue
                var lin = grid.getChildAt(i)
                for(j in 0..8){
                    var btn = (lin as LinearLayout).getChildAt(j)
                    btn = (btn as Button)
                    if (j == 0){
                        btn.background = getDrawable(R.drawable.tho)
                        btn.setTag(R.drawable.tho)
                    }
                    else if(j == 8){
                        btn.background = getDrawable(R.drawable.th)
                        btn.setTag(R.drawable.th)
                    }
                    else if(j == 2 || j == 5){
                        btn.background = getDrawable(R.drawable.tw)
                        btn.setTag(R.drawable.tw)
                    }
                    else{
                        btn.background = getDrawable(R.drawable.one)
                        btn.setTag(R.drawable.one)
                    }
                }
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
            for (i in 0..18){
                if(i % 2 == 0) continue
                var lin = grid.getChildAt(i)
                for(j in 0..8){
                    var btn = (lin as LinearLayout).getChildAt(j)
                    btn = (btn as Button)
                    if (j == 0){
                        btn.background = getDrawable(R.drawable.tho)
                        btn.setTag(R.drawable.tho)
                    }
                    else if(j == 8){
                        btn.background = getDrawable(R.drawable.th)
                        btn.setTag(R.drawable.th)
                    }
                    else if(j == 2 || j == 5){
                        btn.background = getDrawable(R.drawable.tw)
                        btn.setTag(R.drawable.tw)
                    }
                    else{
                        btn.background = getDrawable(R.drawable.one)
                        btn.setTag(R.drawable.one)
                    }
                }
            }
            pressed?.setImageResource(drawPressed!!)
            drawPressed = null
            pressed = null
            n = 81 - 42;
            for (i in 0..18){
                if(i % 2 == 0) continue
                var lin = grid.getChildAt(i)
                for(j in 0..8){
                    var b = (lin as LinearLayout).getChildAt(j)
                    b = (b as Button)
                    if(sudokuGrid[i / 2][j] == 0){
                        b.text = ""
                    }
                    else{
                        b.text = sudokuGrid[i / 2][j].toString()
                    }
                }
            }

            it.startAnimation(animAlpha)
            delete.setImageResource(R.drawable.delete)
            delete_pressed = false
            chronometer.stop()
            startTime = SystemClock.elapsedRealtime()
            chronometer.setBase(startTime)
            chronometer.start()
        }
        // OnClickListeners for Num Buttons
        val oclBtnOk = View.OnClickListener() {
            if(delete_pressed){
                delete.setImageResource(R.drawable.delete)
                delete_pressed = false
            }
            if(it.id == pressed?.id){
                var pressedIndex = 0
                for (i in 0..8){
                    if(it == nums[i]){
                        pressedIndex = i + 1
                        break
                    }
                }
                for (i in 0..18){
                    if(i % 2 == 0) continue
                    var lin = grid.getChildAt(i)
                    for(j in 0..8){
                        var bttn = (lin as LinearLayout).getChildAt(j)
                        bttn = (bttn as Button)
                        if(bttn.text.toString() != "" && Integer.parseInt(bttn.text.toString()) == pressedIndex){
                            for (i in map_to){
                                if(i[1] == bttn.getTag()){
                                    bttn.setBackgroundResource(i[0])
                                    bttn.setTag(i[0])
                                }
                            }
                        }
                    }
                }

                pressed!!.setImageResource(drawPressed!!)
                drawPressed = null
                pressed = null
            }
            else{
                var pressedIndex = 0
                for (i in 0..8){
                    if(it == nums[i]){
                        pressedIndex = i + 1
                        break
                    }
                }
                for (i in 0..18){
                    if(i % 2 == 0) continue
                    var lin = grid.getChildAt(i)
                    for(j in 0..8){
                        var bttn = (lin as LinearLayout).getChildAt(j)
                        bttn = (bttn as Button)
                        if(bttn.text.toString() != "" && Integer.parseInt(bttn.text.toString()) == pressedIndex){
                            for (i in map_to){
                                if(i[0] == bttn.getTag()){
                                    bttn.setBackgroundResource(i[1])
                                    bttn.setTag(i[1])
                                }
                            }
                        }
                        else if(bttn.text.toString() != "" && Integer.parseInt(bttn.text.toString()) != pressedIndex){
                            for (i in map_to){
                                if(i[1] == bttn.getTag()){
                                    bttn.setBackgroundResource(i[0])
                                    bttn.setTag(i[0])
                                }
                            }
                        }
                    }
                }

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
        if (timeWhenStopped != null && n != 0){
            var chronometer = findViewById<Chronometer>(R.id.chronometer)
            chronometer.setBase(SystemClock.elapsedRealtime() + timeWhenStopped!!);
            chronometer.start()
        }
    }
}