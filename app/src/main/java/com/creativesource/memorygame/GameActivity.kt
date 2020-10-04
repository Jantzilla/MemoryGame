package com.creativesource.memorygame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.action_bar.*

class GameActivity : AppCompatActivity() {
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

        val matched = 0
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
        viewAdapter = CardAdapter(cardIds)

        recyclerView = findViewById<RecyclerView>(R.id.recyclerView).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter

        }

        timer = object: CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                tv_time.setText((millisUntilFinished / 1000).toString())
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

    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
    }
}