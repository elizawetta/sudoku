package com.example.sudoku

import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.view.animation.AnimationUtils
import android.widget.Chronometer
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class GameActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        // Chronometer
        var chronometer = findViewById<Chronometer>(R.id.chronometer)
        chronometer.setFormat("%m:%s")
        var startTime = SystemClock.elapsedRealtime()
        chronometer.setBase(startTime)
        chronometer.start()
        // Animation
        val animAlpha = AnimationUtils.loadAnimation(this, R.anim.alpha);
        // Buttons
        var home = findViewById<ImageButton>(R.id.home);
        var write = findViewById<ImageButton>(R.id.write);
        var delete = findViewById<ImageButton>(R.id.delete);
        var restart = findViewById<ImageButton>(R.id.restart);
        // OnClickListeners
        home.setOnClickListener{
            it.startAnimation(animAlpha)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        write.setOnClickListener{
            write.setImageResource(R.drawable.write_active)
            delete.setImageResource(R.drawable.delete)
        }
        delete.setOnClickListener{
            write.setImageResource(R.drawable.write)
            delete.setImageResource(R.drawable.delete_active)
        }
        restart.setOnClickListener{
            it.startAnimation(animAlpha)
            write.setImageResource(R.drawable.write)
            delete.setImageResource(R.drawable.delete)
            Toast.makeText(this, "restart", 3).show()
        }
    }
}