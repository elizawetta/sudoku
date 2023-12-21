package com.example.sudoku

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ImageButton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val startGame = this.findViewById<ImageButton>(R.id.startGame)
        val animAlpha = AnimationUtils.loadAnimation(this, R.anim.alpha);
        startGame.setOnClickListener{
            it.startAnimation(animAlpha)
            val start = Intent(this, GameActivity::class.java)
            startActivity(start)
        }
    }
}