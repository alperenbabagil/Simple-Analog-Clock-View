package com.alperenbabagil.simpleanalogclockview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AnalogClock
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.RandomAccess

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val random=Random()
        Timer().scheduleAtFixedRate(object : TimerTask(){
            override fun run() {
                clockview.updateTime(Date(random.nextLong()))
            }

        },0,1000)
    }
}
