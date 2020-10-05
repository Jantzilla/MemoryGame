package com.creativesource.memorygame

import android.content.Intent
import android.graphics.Typeface
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var popSound: MediaPlayer
    private lateinit var bounce: Animation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val typeface = Typeface.createFromAsset(assets, "ColorTube.otf")

        popSound = MediaPlayer.create(this, R.raw.pop)

        val zoom = AnimationUtils.loadAnimation(this, R.anim.zoom)
        zoom.duration = (1000..2000).random().toLong()

        tv_title.startAnimation(zoom)

        btn_1.setOnClickListener(this)
        tv_1.typeface = typeface
        btn_1.startAnimation(createNewBounce(0))
        btn_2.setOnClickListener(this)
        tv_2.typeface = typeface
        btn_2.startAnimation(createNewBounce(100))
        btn_3.setOnClickListener(this)
        tv_3.typeface = typeface
        btn_3.startAnimation(createNewBounce(200))
        btn_4.setOnClickListener(this)
        tv_4.typeface = typeface
        btn_4.startAnimation(createNewBounce(300))
    }

    private fun createNewBounce(i: Long): Animation {
        bounce = AnimationUtils.loadAnimation(this, R.anim.bounce)
        bounce.repeatMode = Animation.REVERSE
        bounce.duration = (1000..2000).random().toLong()
        bounce.startOffset = i

        bounce.setAnimationListener(object : AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                popSound.start()
            }
            override fun onAnimationRepeat(animation: Animation) {}
        })
        return bounce
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