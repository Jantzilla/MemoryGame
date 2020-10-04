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
        val intent = Intent(this,GameActivity::class.java)
        when (v?.id) {
            R.id.btn_1 -> {
                intent.putExtra("Pairs",5)
                intent.putExtra("Columns",2)
                startActivity(intent)
            }
            R.id.btn_2 -> {
                intent.putExtra("Pairs",6)
                intent.putExtra("Columns",3)
                startActivity(intent)
            }
            R.id.btn_3 -> {
                intent.putExtra("Pairs",8)
                intent.putExtra("Columns",4)
                startActivity(intent)
            }
            R.id.btn_4 -> {
                intent.putExtra("Pairs",10)
                intent.putExtra("Columns",4)
                startActivity(intent)
            }
        }
    }
}