package com.creativesource.memorygame

import android.graphics.Bitmap
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.action_bar.*
import kotlinx.android.synthetic.main.card.view.*

class GameActivity : AppCompatActivity(), ClickListener {
    private lateinit var timer: CountDownTimer
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private var isWaiting = false
    private var chosenCard = -1
    private var  matched = 0
    private var lastCardIndex = -1
    private var pairs = 0
    private var time = "30"
    private val imageIds = intArrayOf(R.drawable.bat, R.drawable.cat, R.drawable.cow, R.drawable.dragon, R.drawable.ghost,
                                     R.drawable.hen, R.drawable.horse, R.drawable.pig, R.drawable.man, R.drawable.spider)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        val drawable = resources.getDrawable(R.drawable.back)
        val bitmap = (drawable as BitmapDrawable).bitmap
        val newDrawable: Drawable = BitmapDrawable(resources, Bitmap.createScaledBitmap(bitmap, 185, 170, true))
        val actionBar: ActionBar? = this.supportActionBar
        actionBar?.setCustomView(R.layout.action_bar)
        actionBar?.setDisplayShowCustomEnabled(true)
        actionBar?.setHomeAsUpIndicator(newDrawable)
        actionBar?.show()

        val typeface = Typeface.createFromAsset(assets, "ColorTube.otf")
        tv_time.typeface = typeface
        tv_matched.typeface = typeface
        tv_divider.typeface = typeface
        tv_pairs.typeface = typeface

        val rowCount = intent.getIntExtra("Rows", 5)
        val columnCount = intent.getIntExtra("Columns", 2)
        pairs = intent.getIntExtra("Pairs", 5)

        tv_pairs.text = pairs.toString()

        val cardIds: MutableList<Int> = ArrayList()

        for (i in 1..pairs) {
            cardIds.add(imageIds[i - 1])
            cardIds.add(imageIds[i - 1])
        }

        cardIds.shuffle()

        viewManager = object : GridLayoutManager(this, columnCount) {
            override fun checkLayoutParams(lp: RecyclerView.LayoutParams): Boolean {
                lp.height = (height / rowCount) - 85
                return true
            }
        }
        viewAdapter = CardAdapter(this, cardIds)

        recyclerView = findViewById<RecyclerView>(R.id.recyclerView).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter

        }

        timer = object: CountDownTimer(33000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                time = (millisUntilFinished / 1000).toString()

                if(time <= "30") {
                    if(time == "30") {
                        flipCards()
                    }
                    tv_time.text = time
                }
            }
            override fun onFinish() {

                val dialog = AlertDialog.Builder(this@GameActivity)
                dialog.setCancelable(false)
                    .setMessage("Almost...")
                    .setNegativeButton("Exit") { _, _ -> finish() }
                    .setPositiveButton("Retry") { _, _ ->
                        run {
                            finish()
                            intent.putExtra("Pairs", pairs)
                            startActivity(intent)
                        }
                    }
                    .create()
                    .show()
            }
        }
        timer.start()
    }

    private fun flipCards() {
        for (i in 0..recyclerView.childCount) {
            recyclerView.findViewHolderForAdapterPosition(i)?.itemView?.iv_card?.visibility = View.VISIBLE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
    }

    override fun onClickListener(cardIndex: Int, cardId: Int) {
        if(!isWaiting) {
            revealCard(cardIndex, cardId)
        }
    }

    private fun revealCard(cardIndex: Int, cardId: Int) {
        if (recyclerView.findViewHolderForAdapterPosition(cardIndex)?.itemView?.iv_card?.visibility == View.VISIBLE) {
            recyclerView.findViewHolderForAdapterPosition(cardIndex)?.itemView?.iv_card?.visibility = View.INVISIBLE
            checkMatch(cardId, cardIndex)
        }
    }

    private fun checkMatch(cardId: Int, cardIndex: Int) {
        if (chosenCard != -1) {
            if (chosenCard == cardId) {
                matched++
                tv_matched.text = matched.toString()
                checkSuccess()
            } else {
                hidePair(cardIndex)
            }
            chosenCard = -1
        } else {
            chosenCard = cardId
            lastCardIndex = cardIndex
        }
    }

    private fun checkSuccess() {
        if(matched == pairs) {
            timer.cancel()
            createSuccessDialog()
        }
    }

    private fun createSuccessDialog() {
        val dialog = AlertDialog.Builder(this@GameActivity)
        dialog.setCancelable(false)
            .setMessage("You Did It!!!")
            .setNegativeButton("Exit") { _, _ -> finish() }
            .create()
            .show()
    }

    private fun hidePair(cardIndex: Int) {
        isWaiting = true
        Handler(Looper.getMainLooper()).postDelayed({
            recyclerView.findViewHolderForAdapterPosition(lastCardIndex)?.itemView?.iv_card?.visibility = View.VISIBLE
            recyclerView.findViewHolderForAdapterPosition(cardIndex)?.itemView?.iv_card?.visibility = View.VISIBLE
            isWaiting = false
        }, 1000)
    }
}

interface ClickListener {
    fun onClickListener(cardIndex: Int, cardId: Int)
}
