package cc.leishui.bilitx.fragment

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import cc.leishui.bilitx.R
import cc.leishui.bilitx.activity.LessonVideoActivity
import cc.leishui.bilitx.adapter.delegate.SimpleDelegateAdapter
import cc.leishui.bilitx.adapter.delegate.SingleDelegateAdapter
import cc.leishui.bilitx.bean.bean.LessonSet
import cc.leishui.bilitx.constant.StatusType
import cc.leishui.bilitx.network.Repo
import cc.leishui.bilitx.utils.Utils
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder
import com.xuexiang.xui.widget.banner.widget.banner.BannerItem
import com.xuexiang.xui.widget.banner.widget.banner.SimpleImageBanner

class LessonFragmentVLayout : MainFragmentVLayout() {
    private lateinit var mNewsAdapter: SimpleDelegateAdapter<LessonSet>
    override fun initData(page: Int) {
        if (page == 0) this.page = 0
        this.page += 1
        Repo.getLessonSetByPage(page, 10, {
            if (it.body() != null && it.body()!!.status == StatusType.SUCCESSFUL) {
                if (page == 0) {
                    mNewsAdapter.refresh(it.body()!!.content.content)
                    mDelegateAdapter!!.setAdapters(mAdapters)
                } else {
                    if (it.body()!!.content.empty) {
                        this@LessonFragmentVLayout.page -= 1
                        Utils.toast("已全部加载")
                        refreshLayout.finishLoadMoreWithNoMoreData()
                    } else mNewsAdapter.loadMore(it.body()!!.content.content)
                }
            } else {
                Utils.toast("获取课程失败")
            }
            if (page == 0)
                refreshLayout.finishRefresh()
            else
                refreshLayout.finishLoadMore()

        }, {
            Utils.toast("获取课程失败:请求异常")
            if (page == 0) {
                mDelegateAdapter!!.setAdapters(mAdapters)
                refreshLayout.finishRefresh()
            } else {
                refreshLayout.finishLoadMore()
            }
        })
    }

    override fun initViews(view: View) {
        recyclerView = view.findViewById(R.id.rv_v)
        refreshLayout = view.findViewById(R.id.rf_v)
        // 1.设置VirtualLayoutManager
        val virtualLayoutManager = VirtualLayoutManager(requireContext())
        recyclerView.layoutManager = virtualLayoutManager

        // 2.设置RecycledViews复用池大小（可选）
        val viewPool = RecyclerView.RecycledViewPool()
        viewPool.setMaxRecycledViews(0, 20)
        recyclerView.setRecycledViewPool(viewPool)

        //轮播条，单独布局
        val bannerAdapter: SingleDelegateAdapter =
            object : SingleDelegateAdapter(R.layout.include_head_view_banner) {
                override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
                    val banner =
                        holder.findViewById<SimpleImageBanner>(R.id.sib_simple_usage)
                    banner.setSource(Utils.getBannerListLesson())
                        .setOnItemClickListener { _: View?, _: BannerItem?, position1: Int ->
                            Utils.toast(
                                "headBanner position--->$position1"
                            )
                        }.startScroll()
                }
            }
        //资讯，线性布局
        mNewsAdapter = object : SimpleDelegateAdapter<LessonSet>(
            R.layout.item_main,
            LinearLayoutHelper()
        ) {
            override fun bindData(
                holder: RecyclerViewHolder,
                position: Int,
                model: LessonSet?
            ) {
                if (model != null) {
                    val i = position * 2
                    val ii = i + 1
                    holder.click(R.id.cv_video_main_1) {
                        startActivity(
                            Intent(
                                requireContext(),
                                LessonVideoActivity::class.java
                            ).also {
                                it.putExtra("lessonSet", mData[i])
                            })
                    }
                    holder.click(R.id.cv_video_main_2) {
                        startActivity(
                            Intent(
                                requireContext(),
                                LessonVideoActivity::class.java
                            ).also {
                                it.putExtra("lessonSet", mData[ii])
                            })
                    }
                    holder.text(R.id.tv_view_count1, mData[i].view_count.toString())
                    holder.text(R.id.tv_view_count2, mData[ii].view_count.toString())
                    holder.text(R.id.tv_len_1, mData[i].description)
                    holder.text(R.id.tv_len_2, mData[ii].description)
                    holder.text(R.id.tv_title_main_1, mData[i].title)
                    holder.text(R.id.tv_title_main_2, mData[ii].title)
                    holder.image(R.id.iv_cover_main_1, mData[i].cover_url)
                    holder.image(R.id.iv_cover_main_2, mData[ii].cover_url)
                }
            }


            override fun getItemCount(): Int {
                return mData.size / 2
            }
        }
        mDelegateAdapter = DelegateAdapter(virtualLayoutManager)
        mAdapters.add(bannerAdapter)
        mAdapters.add(mNewsAdapter)

        // 3.设置DelegateAdapter
        recyclerView.adapter = mDelegateAdapter
    }
}