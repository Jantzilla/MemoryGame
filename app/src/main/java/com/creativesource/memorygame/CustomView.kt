package com.creativesource.memorygame

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import android.view.animation.GridLayoutAnimationController
import androidx.recyclerview.widget.RecyclerView

class CustomView : RecyclerView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle)

    override fun attachLayoutAnimationParameters(child: View, params: ViewGroup.LayoutParams, index: Int, count: Int) {
        val adapter = adapter; val layoutManager = layoutManager

        if (adapter != null && layoutManager is GridLayoutManager) {

            val animParams = params.layoutAnimationParameters as? GridLayoutAnimationController.AnimationParameters ?: GridLayoutAnimationController.AnimationParameters()
            params.layoutAnimationParameters = animParams

            val columns = layoutManager.spanCount
            val reverseIndex = count - 1 - index
            animParams.column = columns - 1 - (reverseIndex % columns)
            animParams.row = animParams.rowsCount - 1 - reverseIndex / columns

            animParams.index = index
            animParams.count = count
            animParams.columnsCount = columns
            animParams.rowsCount = count / columns

        } else
            super.attachLayoutAnimationParameters(child, params, index, count)
    }
}