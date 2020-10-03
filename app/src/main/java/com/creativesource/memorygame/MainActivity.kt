package com.creativesource.memorygame

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn_1.setOnClickListener(this)
        btn_2.setOnClickListener(this)
        btn_3.setOnClickListener(this)
        btn_4.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_1 -> {
                startActivity(Intent(this, GameActivity::class.java))
            }
            R.id.btn_2 -> {
                startActivity(Intent(this, GameActivity::class.java))
            }
            R.id.btn_3 -> {
                startActivity(Intent(this, GameActivity::class.java))
            }
            R.id.btn_4 -> {
                startActivity(Intent(this, GameActivity::class.java))
            }
        }
    }
}