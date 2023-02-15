package cc.leishui.bilitx.adapter.video

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
import cc.leishui.bilitx.R
import cc.leishui.bilitx.activity.NormalVideoActivity
import cc.leishui.bilitx.bean.biliBean.BVideo
import cc.leishui.bilitx.bean.biliBean.Detail
import cc.leishui.bilitx.network.bili.BiliRepo
import cc.leishui.bilitx.utils.Utils
import cc.leishui.bilitx.utils.Utils.std
import cc.leishui.bilitx.utils.Utils.time
import cc.leishui.bilitx.widget.ExpandTextView
import cc.leishui.bilitx.widget.ExpandView
import com.bumptech.glide.Glide
import com.rishabhharit.roundedimageview.RoundedImageView


class IntroAdapter(private val mContext: Context) :
    RecyclerView.Adapter<IntroAdapter.IntroViewHolder>() {

    private var tjList: ArrayList<BVideo>? = null
    private var detail:Detail = Detail()

    fun setDetail(detail: Detail){
        this.detail=detail
        this.tjList=detail.related
    }

    inner class IntroViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val riAvatar: RoundedImageView? = itemView.findViewById(R.id.ri_avatar_video)
        val tvUpName: TextView? = itemView.findViewById(R.id.tv_up_name_video)
        val tvName: ExpandTextView? = itemView.findViewById(R.id.tv_name_video)
        val tvDanmaku: TextView? = itemView.findViewById(R.id.tv_danmaku_intro)
        val ev: ExpandView? = itemView.findViewById(R.id.ev_intro)
        val tvDes: TextView? = itemView.findViewById(R.id.tv_des_intro)
        val tvUpDes: TextView? = itemView.findViewById(R.id.tv_up_time_intro)
        val tvGood: TextView? = itemView.findViewById(R.id.tv_good_intro)
        val tvCoin: TextView? = itemView.findViewById(R.id.tv_coin_intro)
        val tvCollection: TextView? = itemView.findViewById(R.id.tv_collection_intro)
        val tvShare: TextView? = itemView.findViewById(R.id.tv_share_intro)
        val ll:LinearLayout? = itemView.findViewById(R.id.ll_intro)

        val ivT: ImageView? = itemView.findViewById(R.id.iv_tj)
        val tvT: TextView? = itemView.findViewById(R.id.tv_t_tj)
        val llT: LinearLayout? = itemView.findViewById(R.id.ll_tj)
        val tvUp: TextView? = itemView.findViewById(R.id.tv_up_tj)
        val tvViewTj: TextView? = itemView.findViewById(R.id.tv_view_tj)
        val tvDanmakuTj: TextView? = itemView.findViewById(R.id.tv_danmaku_tj)
        val tvBvid: TextView? = itemView.findViewById(R.id.tv_bvid_intro)
        val tvView: TextView? = itemView.findViewById(R.id.tv_view_intro)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IntroViewHolder {
        val view: View = if (viewType == 0)
            LayoutInflater.from(mContext).inflate(R.layout.item_intro, parent, false)
        else
            LayoutInflater.from(mContext).inflate(R.layout.item_tuijian, parent, false)
        return IntroViewHolder(view)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) 0 else 1
    }

    override fun getItemCount(): Int {
        return tjList?.size ?: 2
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: IntroViewHolder, position: Int) {
        if (position == 0) {
            holder.riAvatar?.let {
                Glide.with(holder.itemView).load(detail.uface).placeholder(R.drawable.ic_gank)
                    .error(R.drawable.ic_error).into(it)
            }
            holder.ll?.setOnClickListener {
                holder.ev?.toggle()
                holder.tvName?.toggle()
            }
            holder.tvName?.setOnClickListener {
                (it as ExpandTextView).toggle()
                holder.ev?.toggle()
            }
            holder.tvBvid?.setOnLongClickListener {
                Utils.textCopyThenPost((it as TextView).text.toString())
                true
            }
            holder.tvName?.text = detail.title
            holder.tvDes?.text = detail.desc
            BiliRepo.getDesc(detail.bvid, {
                holder.tvDes?.text = it.data
            })
            holder.tvView?.text = detail.view.std()
            holder.tvDanmaku?.text = detail.danmaku.std()+"   " + detail.pubDate.time()
            holder.tvBvid?.text = detail.bvid
            holder.tvUpName?.text = detail.uname
            holder.tvUpDes?.text = detail.follower.std()+"粉丝  "+detail.archiveCount.std()+"视频"
            holder.tvGood?.text = detail.like.std()
            holder.tvCoin?.text = detail.coin.std()
            holder.tvCollection?.text = detail.collection.std()
            holder.tvShare?.text = detail.share.std()
            return
        }
        val l = tjList?.get(position)
        if (l != null) {
            holder.llT?.setOnClickListener {
                mContext.startActivity(Intent(mContext,NormalVideoActivity::class.java).apply {
                    this.putExtra("BVideo", l)
                })
            }
            Glide.with(holder.itemView).load(l.pic).placeholder(R.drawable.loading)
                .error(R.drawable.loading).into(holder.ivT!!)
            holder.tvT?.text = l.title
            holder.tvUp?.text = l.uname
            holder.tvViewTj?.text = l.view.std()
            holder.tvDanmakuTj?.text = l.danmaku.std()
        }
    }
}