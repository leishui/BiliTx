package cc.leishui.bilitx.adapter.video

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.text.style.ImageSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import cc.leishui.bilitx.R
import cc.leishui.bilitx.bean.biliBean.Reply
import cc.leishui.bilitx.utils.Utils.getThemeColor
import cc.leishui.bilitx.utils.Utils.std
import cc.leishui.bilitx.widget.OCTextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

class CommentAdapter(private val context: Context) :
    RecyclerView.Adapter<CommentAdapter.CommentHolder>() {
    private var mid = 0L
    private var hasTop: Boolean = false
    private var data: MutableList<Reply> = ArrayList()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: MutableList<Reply>) {
        this.data = data
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun loadMore(data: MutableList<Reply>) {
        this.data+=data
        notifyDataSetChanged()
    }

    fun setMid(mid: Long) {
        this.mid = mid
    }

    fun setHasTop(hasTop: Boolean) {
        this.hasTop = hasTop
    }

    inner class CommentHolder(itemView: View) : ViewHolder(itemView) {
        val tvUpName: TextView = itemView.findViewById(R.id.tv_up_name_comment)
        val tvTime: TextView = itemView.findViewById(R.id.tv_up_time_comment)
        val tvContent: OCTextView = itemView.findViewById(R.id.tv_comment_content)
        val tvGood: TextView = itemView.findViewById(R.id.tv_good_reply)
        val ll: LinearLayout = itemView.findViewById(R.id.ll_comment_reply)
        val ivUpFace: ImageView = itemView.findViewById(R.id.ri_avatar_comment)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentHolder {
        return CommentHolder(
            LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false)
        )
    }

    override fun getItemCount(): Int {
        //Log.d("count",data.size.toString())
        return data.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CommentHolder, position: Int) {
        val model = data[position]
        showTop(position, model, holder)
        showLevelAndUp(model, holder)
        showChildReplies(model, holder)
        holder.tvTime.text = model.time
        Glide.with(context).load(model.avatar).error(R.drawable.loading)
            .placeholder(R.drawable.loading).into(holder.ivUpFace)
        if (model.like != 0)
            holder.tvGood.text = model.like.std()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun showLevelAndUp(
        model: Reply,
        holder: CommentHolder
    ) {
        val str = "${model.uname}    "
        val spanString = SpannableString(str)
        val lv = when (model.level) {
            0 -> context.resources.getDrawable(R.drawable.lv0, null)
            1 -> context.resources.getDrawable(R.drawable.lv1, null)
            2 -> context.resources.getDrawable(R.drawable.lv2, null)
            3 -> context.resources.getDrawable(R.drawable.lv3, null)
            4 -> context.resources.getDrawable(R.drawable.lv4, null)
            5 -> context.resources.getDrawable(R.drawable.lv5, null)
            6 -> {
                if (model.isSeniorMember == 1)
                    context.resources.getDrawable(R.drawable.lv6p, null)
                else
                    context.resources.getDrawable(R.drawable.lv6, null)
            }
            else -> context.resources.getDrawable(R.drawable.lv0, null)
        }
        if (model.mid == mid) {
            val up = context.resources.getDrawable(R.drawable.up_pink, null)
            up.setBounds(0, 0, 54, 30)
            val upSpan = ImageSpan(up, ImageSpan.ALIGN_BASELINE)
            spanString.setSpan(
                upSpan,
                str.length - 1,
                str.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        lv.setBounds(0, 0, 56, 28)
        if (model.isSeniorMember == 1)
            lv.setBounds(0, 0, 75, 28)
        val span = ImageSpan(lv, ImageSpan.ALIGN_BASELINE)
        spanString.setSpan(
            span,
            str.length - 3,
            str.length - 2,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        holder.tvUpName.text = spanString
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun showContent(
        urlMap: HashMap<String, String>,
        sizeMap: HashMap<String, Int>,
        mr: List<MatchResult>,
        content: SpannableString,
        tv: TextView,
        offset: Int
    ) {
        val t = context.resources.getDrawable(R.drawable.top, null)
        tv.text = content
        mr.forEach {
            Glide.with(context)
                .load(urlMap[it.value])
                .placeholder(t)
                .into(object : CustomTarget<Drawable>() {
                    override fun onResourceReady(
                        resource: Drawable,
                        transition: Transition<in Drawable>?
                    ) {
                        val size = sizeMap[it.value]!!
                        resource.setBounds(0, 0, 60 * size, 60 * size)
                        content.setSpan(
                            ImageSpan(resource, ImageSpan.ALIGN_BOTTOM),
                            it.range.first + offset,
                            it.range.last + offset + 1,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                        tv.text = content
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                    }
                })
        }

    }

    @SuppressLint("SetTextI18n", "ResourceType")
    private fun showChildReplies(model: Reply, holder: CommentHolder) {
        val linearLayout = holder.ll
        linearLayout.removeAllViews()
        val linkColor = context.getThemeColor(R.attr.t_comment_link)
        val rls = model.replies
        if (rls.isEmpty()) linearLayout.isGone = true
        rls.forEach {
            val tv = TextView(context).apply {
                maxLines = 2
                ellipsize = TextUtils.TruncateAt.END
                setTextColor(context.getThemeColor(R.attr.app_text))
            }
            val ss =
                SpannableString(it.uname + ":" + it.content)
            ss.setSpan(
                ForegroundColorSpan(linkColor),
                0,
                it.uname.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            linearLayout.addView(tv)
            showContent(it.urlMap, it.sizeMap, it.mr, ss, tv, it.uname.length + 1)
        }
        if (model.entryTxt.isNotEmpty() && model.rcount > 3) {
            linearLayout.addView(TextView(context).apply {
                text = model.entryTxt
                setTextColor(linkColor)
            })
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun showTop(
        position: Int,
        model: Reply,
        holder: CommentHolder
    ) {
        if (position == 0 && hasTop) {
            val param = model.content
            val str = "  $param"
            val spanString = SpannableString(str)
            val d = context.resources.getDrawable(R.drawable.top, null)
            d.setBounds(0, 0, 96, 48)
            val span = ImageSpan(d, ImageSpan.ALIGN_BOTTOM)
            spanString.setSpan(span, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            showContent(
                model.urlMap,
                model.sizeMap,
                model.mr,
                spanString,
                holder.tvContent,
                2
            )
        } else showContent(
            model.urlMap,
            model.sizeMap,
            model.mr,
            SpannableString(model.content),
            holder.tvContent,
            0
        )
    }
}