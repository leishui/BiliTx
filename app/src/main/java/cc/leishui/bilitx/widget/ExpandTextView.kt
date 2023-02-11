package cc.leishui.bilitx.widget

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.text.Layout
import android.text.StaticLayout
import android.text.TextUtils
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class ExpandTextView : AppCompatTextView {
    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        isSingleLine = true
        ellipsize=TextUtils.TruncateAt.END
    }

    private var type: BufferType? = null
    private var sequence: CharSequence = ""

    //是否展开，默认展开
    private var isOpen = false

    /**
     * 动画值改变的时候 请求重新布局
     */
    private var animPercent: Float = 1f
        set(value) {
            field = value
            requestLayout()
        }

    fun toggle(): Boolean {
        isOpen = !isOpen
        startAnim()
        return isOpen
    }

    /**
     * 执行动画的时候 更改 animPercent 属性的值 即从0-1
     */
    @SuppressLint("AnimatorKeep", "ObjectAnimatorBinding")
    private fun startAnim() {
        //ofFloat，of xxxX 根据参数类型来确定
        //1，动画对象，即当前view。2.动画属性名。3，起始值。4，目标值。
        val animator = ObjectAnimator.ofFloat(this, "animPercent", 0f, 1f)
        animator.duration = 300
        animator.start()
    }

    override fun setText(text: CharSequence, type: BufferType) {
        super.setText(text, type)
        this.sequence = text
        this.type = type
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val lines = staticLayout().lineCount
        val height = staticLayout().height +10
        //Log.d("extv", "onMeasure: $lines : $height")
        if (isOpen) {
            isSingleLine = false
            setMeasuredDimension(
                widthMeasureSpec,
                (height + height * (lines - 1) * animPercent).toInt()
            )
        } else {
            if (animPercent == 1f)
                isSingleLine = true
            setMeasuredDimension(
                widthMeasureSpec,
                (height * lines - height * (lines - 1) * animPercent).toInt()
            )
        }
    }

    private fun staticLayout() =
        StaticLayout.Builder.obtain(sequence, 0, sequence.length, paint, getReplyTextWidth())
            .setAlignment(Layout.Alignment.ALIGN_NORMAL).setLineSpacing(1f, 0f)
            .setIncludePad(false).build()

    private fun getReplyTextWidth(): Int {
        val scale = context.resources.displayMetrics.density
        return resources.displayMetrics.widthPixels - (20 * scale + 0.5f).toInt() // 四舍五入取整
    }
}