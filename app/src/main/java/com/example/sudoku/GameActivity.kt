package com.example.sudoku

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.widget.Chronometer

class GameActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        var chronometer = findViewById<Chronometer>(R.id.chronometer)
        chronometer.setFormat("%m:%s")
        var startTime = SystemClock.elapsedRealtime()
        chronometer.setBase(startTime)
        chronometer.start()
    }
}