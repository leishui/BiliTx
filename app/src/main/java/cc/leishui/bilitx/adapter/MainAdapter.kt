package cc.leishui.bilitx.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import cc.leishui.bilitx.R
import cc.leishui.bilitx.activity.NormalVideoActivity
import cc.leishui.bilitx.bean.biliBean.BVideo
import cc.leishui.bilitx.bean.biliBean.Rcmd.DataEntity.ItemEntity
import cc.leishui.bilitx.utils.Utils.dur
import cc.leishui.bilitx.utils.Utils.load
import cc.leishui.bilitx.utils.Utils.std

class MainAdapter(private val context: Context) : RecyclerView.Adapter<MainAdapter.MainHolder>() {
    private var data: List<ItemEntity> = ArrayList()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<ItemEntity>) {
        this.data = data
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun loadMore(data: List<ItemEntity>) {
        this.data += data
        notifyDataSetChanged()
    }

    inner class MainHolder(itemView: View) : ViewHolder(itemView) {
        val cv1: CardView = itemView.findViewById(R.id.cv_video_main_1)
        val cv2: CardView = itemView.findViewById(R.id.cv_video_main_2)
        val tvView1: TextView = itemView.findViewById(R.id.tv_view_count1)
        val tvView2: TextView = itemView.findViewById(R.id.tv_view_count2)
        val tvDur1: TextView = itemView.findViewById(R.id.tv_len_1)
        val tvDur2: TextView = itemView.findViewById(R.id.tv_len_2)
        val tvTitle1: TextView = itemView.findViewById(R.id.tv_title_main_1)
        val tvTitle2: TextView = itemView.findViewById(R.id.tv_title_main_2)
        val tvUp1: TextView = itemView.findViewById(R.id.tv_up_main_1)
        val tvUp2: TextView = itemView.findViewById(R.id.tv_up_main_2)
        val ivCover1: ImageView = itemView.findViewById(R.id.iv_cover_main_1)
        val ivCover2: ImageView = itemView.findViewById(R.id.iv_cover_main_2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        return MainHolder(
            LayoutInflater.from(context).inflate(R.layout.item_main, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return data.size / 2
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val i = position * 2
        val ii = i + 1
        val data1 = data[i]
        val data2 = data[ii]
        holder.cv1.setOnClickListener {
            context.startActivity(
                Intent(
                    context,
                    NormalVideoActivity::class.java
                ).also {
                    it.putExtra("BVideo", BVideo(data1))
                })
        }
        holder.cv2.setOnClickListener {
            context.startActivity(
                Intent(
                    context,
                    NormalVideoActivity::class.java
                ).also {
                    it.putExtra("BVideo", BVideo(data2))
                })
        }
        holder.tvView1.text = data1.stat.view.std()
        holder.tvDur1.text = data1.duration.dur()
        holder.tvTitle1.text = data1.title
        holder.tvUp1.text = "[UP]" + data1.owner.name
        holder.ivCover1.load(data1.pic)
        holder.tvView2.text = data2.stat.view.std()
        holder.tvDur2.text = data2.duration.dur()
        holder.tvTitle2.text = data2.title
        holder.tvUp2.text = "[UP]" + data2.owner.name
        holder.ivCover2.load(data2.pic)
    }
}