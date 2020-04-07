package com.alperenbabagil.simpleanalogclockview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AnalogClock
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.RandomAccess

class MainActivity : AppCompatActivity() {

    var changeByDate=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val random=Random()
        Timer().scheduleAtFixedRate(object : TimerTask(){
            override fun run() {
                if(changeByDate){
                    clockview.updateTime(Date(random.nextLong()))
                }
                else{
                    clockview.hour=16
                    clockview.minute=20
                }
                changeByDate=!changeByDate
            }
        },0,1000)
    }
}
