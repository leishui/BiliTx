/*
 * Copyright (C) 2020 xuexiangjys(xuexiangjys@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package cc.leishui.bilitx.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.RecycledViewPool
import cc.leishui.bilitx.R
import cc.leishui.bilitx.activity.PostActivity
import cc.leishui.bilitx.adapter.delegate.SimpleDelegateAdapter
import cc.leishui.bilitx.adapter.delegate.SingleDelegateAdapter
import cc.leishui.bilitx.bean.bean.Post
import cc.leishui.bilitx.constant.ResourceType
import cc.leishui.bilitx.constant.StatusType
import cc.leishui.bilitx.network.Repo
import cc.leishui.bilitx.utils.Utils
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.bumptech.glide.Glide
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder
import com.xuexiang.xui.widget.banner.widget.banner.BannerItem
import com.xuexiang.xui.widget.banner.widget.banner.SimpleImageBanner

/**
 * VLayout使用步骤
 * 1.设置VirtualLayoutManager
 * 2.设置RecycledViews复用池大小（可选）
 * 3.设置DelegateAdapter
 *
 * @author xuexiang
 * @since 2020/3/19 11:26 PM
 */
class CommunityFragmentVLayout : Fragment() {
    private var page = 0
    private lateinit var recyclerView: RecyclerView
    private lateinit var refreshLayout: SmartRefreshLayout
    private lateinit var mNewsAdapter: SimpleDelegateAdapter<Post>
    private var mDelegateAdapter: DelegateAdapter? = null
    private val mAdapters: MutableList<DelegateAdapter.Adapter<*>> =
        ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_v_community, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        initListeners()
    }

    private fun initViews(view: View) {
        recyclerView = view.findViewById(R.id.rv_v)
        refreshLayout = view.findViewById(R.id.rf_v)
        // 1.设置VirtualLayoutManager
        val virtualLayoutManager = VirtualLayoutManager(requireContext())
        recyclerView.layoutManager = virtualLayoutManager

        // 2.设置RecycledViews复用池大小（可选）
        val viewPool = RecycledViewPool()
        viewPool.setMaxRecycledViews(0, 20)
        recyclerView.setRecycledViewPool(viewPool)

        //轮播条，单独布局
        val bannerAdapter: SingleDelegateAdapter =
            object : SingleDelegateAdapter(R.layout.include_head_view_banner) {
                override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
                    val banner =
                        holder.findViewById<SimpleImageBanner>(R.id.sib_simple_usage)
                    banner.setSource(Utils.getBannerListPost())
                        .setOnItemClickListener { _: View?, _: BannerItem?, position1: Int ->
                            Utils.toast(
                                "$position1"
                            )
                        }.startScroll()
                }
            }

        //资讯，线性布局
        mNewsAdapter = object : SimpleDelegateAdapter<Post>(
            R.layout.adapter_news_xuilayout_list_item,
            LinearLayoutHelper()
        ) {
            override fun bindData(
                holder: RecyclerViewHolder,
                position: Int,
                model: Post?
            ) {

                val linearLayout = holder.itemView.findViewById<LinearLayout>(R.id.l_post_item)
                val postResourcesList = model?.postResourcesList
                linearLayout.removeAllViews()
                if (postResourcesList != null) {
                    var i = 0
                    for (s in postResourcesList) {
                        if (i == 3)
                            break
                        val iv = ImageView(requireContext())
                        val layoutParams =
                            LinearLayout.LayoutParams(0, Utils.dip2px(150f))
                        layoutParams.weight = 1f
                        iv.layoutParams = layoutParams
                        iv.setPadding(5, 5, 5, 5)
                        iv.scaleType = ImageView.ScaleType.CENTER_CROP
                        Glide.with(requireContext()).load(s).placeholder(R.drawable.ic_gank)
                            .error(R.drawable.ic_error).into(iv)
                        //holder.image(i,s)
                        linearLayout.addView(iv)
                        i++
                    }
                }
                holder.text(R.id.tv_comment_count_post, model?.comment_count?.toString())
                holder.text(R.id.tv_like_count_post, model?.like_count?.toString())
                holder.text(R.id.tv_view_count_post, "${model?.view_count}阅读")
                holder.image(R.id.ri_avatar_community, model?.user?.avatar_url)
                holder.text(R.id.tv_community_up_name, model?.user?.user_name)
                holder.text(R.id.tv_community_post_title, model?.post_name)
                holder.text(R.id.tv_community_post_des, model?.post_content)
                holder.click(
                    R.id.card_view
                ) { v: View? ->
                    startActivity(
                        Intent(
                            requireContext(),
                            PostActivity::class.java
                        ).also { it.putExtra("post", model) }
                    )
                    model?.postId?.let { Repo.view(it, ResourceType.POST) }
                }
            }

        }
        mDelegateAdapter = DelegateAdapter(virtualLayoutManager)
        //mAdapters.add(floatAdapter)
        //mAdapters.add(scrollFixAdapter)
        mAdapters.add(bannerAdapter)
        //mAdapters.add(commonAdapter)
        //mAdapters.add(titleAdapter)
        mAdapters.add(mNewsAdapter)

        // 3.设置DelegateAdapter
        recyclerView.adapter = mDelegateAdapter
    }

    private fun initListeners() {
        //下拉刷新
        refreshLayout.setOnRefreshListener {
            initData(0)
        }
        //上拉加载
        refreshLayout.setOnLoadMoreListener {
            initData(page)
        }
        refreshLayout.autoRefresh() //第一次进入触发自动刷新，演示效果
        //initData(0)
    }

    private fun initData(page: Int) {
        if (page == 0) this.page = 0
        this.page += 1
        Repo.getPostsByPage(page, 10, {
            if (it.body() != null && it.body()!!.status == StatusType.SUCCESSFUL) {
                if (page == 0) {
                    mNewsAdapter.refresh(it.body()!!.content.content)
                    mDelegateAdapter!!.setAdapters(mAdapters)
                } else {
                    if (it.body()!!.content.empty) {
                        this@CommunityFragmentVLayout.page -= 1
                        Utils.toast("已全部加载")
                        refreshLayout.finishLoadMoreWithNoMoreData()
                    } else mNewsAdapter.loadMore(it.body()!!.content.content)
                }
            } else {
                Utils.toast("获取贴子失败")
            }
            if (page == 0)
                refreshLayout.finishRefresh()
            else
                refreshLayout.finishLoadMore()
        }, {
            Utils.toast("获取贴子失败:请求异常")
            if (page == 0) {
                mDelegateAdapter!!.setAdapters(mAdapters)
                refreshLayout.finishRefresh()
            } else {
                refreshLayout.finishLoadMore()
            }
        })
    }

}