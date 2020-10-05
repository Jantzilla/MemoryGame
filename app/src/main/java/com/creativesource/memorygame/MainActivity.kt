package com.creativesource.memorygame

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val typeface = Typeface.createFromAsset(assets, "ColorTube.otf")

        val bounce = AnimationUtils.loadAnimation(this, R.anim.bounce)
        bounce.repeatMode = Animation.REVERSE
        bounce.duration = (1000..2000).random().toLong()

        val bob = AnimationUtils.loadAnimation(this, R.anim.zoom)
        bob.duration = (1000..2000).random().toLong()

        tv_title.startAnimation(bob)

        btn_1.setOnClickListener(this)
        tv_1.typeface = typeface
        btn_1.startAnimation(bounce)
        btn_2.setOnClickListener(this)
        tv_2.typeface = typeface
        btn_2.startAnimation(bounce)
        btn_3.setOnClickListener(this)
        tv_3.typeface = typeface
        btn_3.startAnimation(bounce)
        btn_4.setOnClickListener(this)
        tv_4.typeface = typeface
        btn_4.startAnimation(bounce)
    }

    override fun onClick(v: View?) {
        val intent = Intent(this,GameActivity::class.java)
        when (v?.id) {
            R.id.btn_1 -> {
                intent.putExtra("Pairs",5)
                intent.putExtra("Rows",5)
                intent.putExtra("Columns",2)
                startActivity(intent)
            }
            R.id.btn_2 -> {
                intent.putExtra("Pairs",6)
                intent.putExtra("Rows",4)
                intent.putExtra("Columns",3)
                startActivity(intent)
            }
            R.id.btn_3 -> {
                intent.putExtra("Pairs",8)
                intent.putExtra("Rows",4)
                intent.putExtra("Columns",4)
                startActivity(intent)
            }
            R.id.btn_4 -> {
                intent.putExtra("Pairs",10)
                intent.putExtra("Rows",5)
                intent.putExtra("Columns",4)
                startActivity(intent)
            }
        }
    }
}