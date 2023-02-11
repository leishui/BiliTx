package cc.leishui.bilitx.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.core.widget.NestedScrollView

class NestedFuckView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : NestedScrollView(context, attrs, defStyleAttr) {
    private val TAG = "123123"

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent): Boolean {
        //Log.d(TAG, "onTouchEvent: $ev")
        return super.onTouchEvent(ev)
    }


    @SuppressLint("RestrictedApi")
    override fun computeVerticalScrollOffset(): Int {
        //Log.d(TAG, "computeVerticalScrollOffset: "+super.computeVerticalScrollOffset())
        return super.computeVerticalScrollOffset()
    }
}