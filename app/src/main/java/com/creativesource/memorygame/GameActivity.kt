package com.creativesource.memorygame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import kotlinx.android.synthetic.main.action_bar.*

class GameActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        val actionBar: ActionBar? = this.supportActionBar
        actionBar?.setCustomView(R.layout.action_bar)
        actionBar?.setDisplayShowCustomEnabled(true)
        actionBar?.setHomeAsUpIndicator(R.mipmap.ic_launcher)
        actionBar?.show()

        val pairs = intent.getIntExtra("Pairs", 5)
        tv_pairs.text = pairs.toString()
    }
}