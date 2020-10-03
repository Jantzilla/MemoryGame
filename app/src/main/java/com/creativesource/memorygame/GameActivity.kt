package com.creativesource.memorygame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar

class GameActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        val actionBar: ActionBar? = this.supportActionBar
        actionBar?.setCustomView(R.layout.action_bar)
        actionBar?.setDisplayShowCustomEnabled(true)
        actionBar?.setHomeAsUpIndicator(R.mipmap.ic_launcher)
        actionBar?.show()
    }
}