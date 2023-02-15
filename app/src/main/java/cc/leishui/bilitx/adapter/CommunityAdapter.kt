package cc.leishui.bilitx.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import cc.leishui.bilitx.R
import cc.leishui.bilitx.activity.PostActivity
import cc.leishui.bilitx.bean.bean.Post
import cc.leishui.bilitx.constant.ResourceType
import cc.leishui.bilitx.network.Repo
import cc.leishui.bilitx.utils.Utils
import com.bumptech.glide.Glide
import com.xuexiang.xui.widget.layout.XUIFrameLayout

class CommunityAdapter(private val context: Context) : RecyclerView.Adapter<CommunityAdapter.CommunityHolder>() {
    private var data: List<Post> = ArrayList()

    fun setData(data: ArrayList<Post>) {
        this.data = data
    }

    fun loadMore(data: ArrayList<Post>) {
        this.data += data
    }

    inner class CommunityHolder(itemView: View) : ViewHolder(itemView) {
        val tvCount: TextView = itemView.findViewById(R.id.tv_comment_count_post)
        val tvLike: TextView = itemView.findViewById(R.id.tv_like_count_post)
        val tvView: TextView = itemView.findViewById(R.id.tv_view_count_post)
        val ivAvatar: ImageView = itemView.findViewById(R.id.ri_avatar_community)
        val tvUpName: TextView = itemView.findViewById(R.id.tv_community_up_name)
        val tvTitle: TextView = itemView.findViewById(R.id.tv_community_post_title)
        val tvDes: TextView = itemView.findViewById(R.id.tv_community_post_des)
        val card: XUIFrameLayout = itemView.findViewById(R.id.card_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommunityHolder {
        return CommunityHolder(
            LayoutInflater.from(context).inflate(R.layout.adapter_news_xuilayout_list_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CommunityHolder, position: Int) {
        val model = data[position]
        val linearLayout = holder.itemView.findViewById<LinearLayout>(R.id.l_post_item)
        val postResourcesList = model.postResourcesList
        linearLayout.removeAllViews()
        if (postResourcesList != null) {
            var i = 0
            for (s in postResourcesList) {
                if (i == 3)
                    break
                val iv = ImageView(context)
                val layoutParams =
                    LinearLayout.LayoutParams(0, Utils.dip2px(150f))
                layoutParams.weight = 1f
                iv.layoutParams = layoutParams
                iv.setPadding(5, 5, 5, 5)
                iv.scaleType = ImageView.ScaleType.CENTER_CROP
                Glide.with(context).load(s).placeholder(R.drawable.ic_gank)
                    .error(R.drawable.ic_error).into(iv)
                //holder.image(i,s)
                linearLayout.addView(iv)
                i++
            }
        }
        holder.tvCount.text = model.comment_count.toString()
        holder.tvLike.text = model.like_count.toString()
        holder.tvView.text = "${model.view_count}阅读"
        Glide.with(context).load(model.user.avatar_url).into(holder.ivAvatar)
        holder.tvUpName.text = model.user?.user_name
        holder.tvTitle.text = model.post_name
        holder.tvDes.text = model.post_content
        holder.card.setOnClickListener {
            context.startActivity(
                Intent(
                    context,
                    PostActivity::class.java
                ).also { it.putExtra("post", model) }
            )
            model.postId.let { Repo.view(it, ResourceType.POST) }
        }
    }
}