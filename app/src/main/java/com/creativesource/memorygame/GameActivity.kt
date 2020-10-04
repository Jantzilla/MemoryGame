package com.creativesource.memorygame

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
    private var chosenCard = -1
    private var  matched = 0
    private var lastCardIndex = -1
    private var pairs = 0
    private var time = "30"
    private lateinit var timer: CountDownTimer
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        val actionBar: ActionBar? = this.supportActionBar
        actionBar?.setCustomView(R.layout.action_bar)
        actionBar?.setDisplayShowCustomEnabled(true)
        actionBar?.setHomeAsUpIndicator(R.mipmap.ic_launcher)
        actionBar?.show()

        val columnCount = intent.getIntExtra("Columns", 2)
        pairs = intent.getIntExtra("Pairs", 5)

        tv_pairs.text = pairs.toString()

        val cardIds: MutableList<Int> = ArrayList()

        for (i in 1..pairs) {
            cardIds.add(i)
            cardIds.add(i)
        }

        cardIds.shuffle()

        viewManager = GridLayoutManager(this, columnCount)
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
        revealCard(cardIndex, cardId)
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
        Handler(Looper.getMainLooper()).postDelayed({
            recyclerView.findViewHolderForAdapterPosition(lastCardIndex)?.itemView?.iv_card?.visibility = View.VISIBLE
            recyclerView.findViewHolderForAdapterPosition(cardIndex)?.itemView?.iv_card?.visibility = View.VISIBLE
        }, 1000)
    }
}

interface ClickListener {
    fun onClickListener(cardIndex: Int, cardId: Int)
}
