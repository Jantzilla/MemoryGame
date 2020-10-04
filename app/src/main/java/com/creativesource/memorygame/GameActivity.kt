package com.creativesource.memorygame

import android.graphics.Typeface
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
import kotlinx.android.synthetic.main.activity_game.*
import kotlinx.android.synthetic.main.card.view.*

class GameActivity : AppCompatActivity(), View.OnClickListener, ClickListener {
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

        iv_back.setOnClickListener(this)
        iv_dialog_back.setOnClickListener(this)
        tv_retry.setOnClickListener(this)

        this.setSupportActionBar(toolbar)
        val actionBar: ActionBar? = this.supportActionBar
        actionBar?.setDisplayShowTitleEnabled(false)
        actionBar?.show()

        val typeface = Typeface.createFromAsset(assets, "ColorTube.otf")
        tv_time.typeface = typeface
        tv_matched.typeface = typeface
        tv_divider.typeface = typeface
        tv_pairs.typeface = typeface
        tv_dialog.typeface = typeface
        tv_retry.typeface = typeface

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

                createFailDialog()
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
        rl_dialog.visibility = View.VISIBLE
    }

    private fun createFailDialog() {
        rl_dialog.visibility = View.VISIBLE
        tv_dialog.text = "Almost..."
        tv_retry.visibility = View.VISIBLE
    }

    private fun hidePair(cardIndex: Int) {
        isWaiting = true
        Handler(Looper.getMainLooper()).postDelayed({
            recyclerView.findViewHolderForAdapterPosition(lastCardIndex)?.itemView?.iv_card?.visibility = View.VISIBLE
            recyclerView.findViewHolderForAdapterPosition(cardIndex)?.itemView?.iv_card?.visibility = View.VISIBLE
            isWaiting = false
        }, 1000)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_back -> {
                onBackPressed()
            }
            R.id.iv_dialog_back -> {
                onBackPressed()
            }
            R.id.tv_retry -> {
                finish()
                intent.putExtra("Pairs", pairs)
                startActivity(intent)
            }
        }
    }
}

interface ClickListener {
    fun onClickListener(cardIndex: Int, cardId: Int)
}
