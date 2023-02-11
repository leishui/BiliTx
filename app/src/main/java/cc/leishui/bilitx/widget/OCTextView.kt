package cc.leishui.bilitx.widget

import android.content.Context
import android.text.Layout
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.StaticLayout
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatTextView


class OCTextView : AppCompatTextView {
    private var type: BufferType? = null
    private var isOpen = false
    private var sequence: CharSequence = ""


    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun setText(text: CharSequence, type: BufferType) {
        //super.setText(text, type)
        this.sequence = text
        this.type = type
        setText()
    }

    private fun setText() {
        val s: SpannableStringBuilder
        val layout =
            staticLayout()
        val count = layout.lineCount
        val end = layout.getLineEnd(1)
        if (count > 2) {
            val txt: String
            var i = 1
            if (isOpen) {
                if (sequence.toString().endsWith("\n")) {
                    txt = "收起"
                    i = 0
                } else txt = "\n收起"
                s = SpannableStringBuilder(sequence).append(txt)
                s.setSpan(object : ClickableSpan() {
                    override fun onClick(view: View) {
                        isOpen = false
                        setText()
                    }
                }, sequence.length + i, sequence.length + i + 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            } else {
                if (sequence.toString().substring(0, end).endsWith("\n")) {
                    txt = "展开"
                    i = 0
                } else txt = "\n展开"
                s = SpannableStringBuilder(sequence, 0, end).append(txt)
                s.setSpan(object : ClickableSpan() {
                    override fun onClick(view: View) {
                        isOpen = true
                        setText()
                    }
                }, end + i, end + i + 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
            super.setText(s, type)
        } else super.setText(sequence, type)
        movementMethod = LinkMovementMethod.getInstance()
    }

    private fun staticLayout() =
        StaticLayout.Builder.obtain(sequence, 0, sequence.length, paint, getReplyTextWidth())
            .setAlignment(Layout.Alignment.ALIGN_NORMAL).setLineSpacing(1f, 0f)
            .setIncludePad(false).build()

    private fun getReplyTextWidth():Int{
        val scale = context.resources.displayMetrics.density
        return resources.displayMetrics.widthPixels-(70 * scale + 0.5f).toInt() // 四舍五入取整
    }
}