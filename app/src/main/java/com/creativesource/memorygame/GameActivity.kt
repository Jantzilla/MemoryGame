package com.creativesource.memorygame

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
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
        val pairs = intent.getIntExtra("Pairs", 5)

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

        timer = object: CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                tv_time.setText((millisUntilFinished / 1000).toString())

                if(tv_time.text == "28") {
                    flipCards()
                }
            }
            override fun onFinish() {

                val dialog = AlertDialog.Builder(this@GameActivity)
                dialog.setCancelable(false)

                if(matched == pairs) {

                    dialog.setMessage("You Did It!!!")
                        .setNegativeButton("Exit") { _, _ -> finish() }
                        .create()
                        .show()

                } else {

                    dialog.setMessage("Almost...")
                        .setPositiveButton("Retry") { _, _ ->
                            run {
                                finish()
                                intent.putExtra("Pairs", pairs)
                                startActivity(intent)
                            }
                        }
                        .setNegativeButton("Exit") { _, _ -> finish() }
                        .create()
                        .show()

                }
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
        checkMatch(cardIndex, cardId)
    }

    private fun checkMatch(cardIndex: Int, cardId: Int) {
        if (recyclerView.findViewHolderForAdapterPosition(cardIndex)?.itemView?.iv_card?.visibility == View.VISIBLE) {
            recyclerView.findViewHolderForAdapterPosition(cardIndex)?.itemView?.iv_card?.visibility = View.INVISIBLE

            if (chosenCard != -1) {
                if (chosenCard == cardId) {
                    matched++
                    tv_matched.text = matched.toString()
                } else {
                    hidePair(cardIndex)
                }
                chosenCard = -1
            } else {
                chosenCard = cardId
                lastCardIndex = cardIndex
            }
        }
    }

    private fun hidePair(cardIndex: Int) {
        recyclerView.findViewHolderForAdapterPosition(lastCardIndex)?.itemView?.iv_card?.visibility =
            View.VISIBLE
        recyclerView.findViewHolderForAdapterPosition(cardIndex)?.itemView?.iv_card?.visibility =
            View.VISIBLE
    }
}

interface ClickListener {
    fun onClickListener(cardIndex: Int, cardId: Int)
}
