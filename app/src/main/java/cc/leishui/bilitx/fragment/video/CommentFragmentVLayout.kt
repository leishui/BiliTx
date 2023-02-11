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
package cc.leishui.bilitx.fragment.video

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.text.style.ImageSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.RecycledViewPool
import cc.leishui.bilitx.R
import cc.leishui.bilitx.activity.NormalVideoActivity
import cc.leishui.bilitx.adapter.delegate.SimpleDelegateAdapter
import cc.leishui.bilitx.adapter.delegate.SingleDelegateAdapter
import cc.leishui.bilitx.bean.biliBean.Reply
import cc.leishui.bilitx.network.bili.BiliRepo
import cc.leishui.bilitx.utils.CommentUtil
import cc.leishui.bilitx.utils.Utils
import cc.leishui.bilitx.utils.Utils.std
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.alibaba.android.vlayout.layout.StickyLayoutHelper
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog


/**
 * VLayout使用步骤
 * 1.设置VirtualLayoutManager
 * 2.设置RecycledViews复用池大小（可选）
 * 3.设置DelegateAdapter
 *
 * @author xuexiang
 * @since 2020/3/19 11:26 PM
 */
class CommentFragmentVLayout(private val oid: String) : Fragment() {
    private var page = 0
    private var mid = 0L
    private var hasTop = false
    private lateinit var recyclerView: RecyclerView
    private lateinit var refreshLayout: SmartRefreshLayout
    private lateinit var mNewsAdapter: SimpleDelegateAdapter<Reply>
    private var mDelegateAdapter: DelegateAdapter? = null
    private val mAdapters: MutableList<DelegateAdapter.Adapter<*>> =
        ArrayList()
    private var dialog: MaterialDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_v_comment, container, false)
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

        //资讯的标题
        val stickyLayoutHelper = StickyLayoutHelper()
        stickyLayoutHelper.setStickyStart(true)
        val titleAdapter: SingleDelegateAdapter = object :
            SingleDelegateAdapter(R.layout.adapter_vlayout_title_item, stickyLayoutHelper) {
            override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
                holder.text(R.id.tv_title, "热门评论")
                holder.text(R.id.tv_action, "按热度")
                holder.click(
                    R.id.tv_action
                ) { v: View? -> Utils.toast("按热度") }
            }
        }
        val bottomAdapter = object : SingleDelegateAdapter(R.layout.adapter_vlayout_bottom_item) {
            override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {

            }

        }

        //资讯，线性布局
        mNewsAdapter = object : SimpleDelegateAdapter<Reply>(
            R.layout.item_comment,
            LinearLayoutHelper()
        ) {
            @SuppressLint("SetTextI18n", "UseCompatLoadingForDrawables")
            override fun bindData(
                holder: RecyclerViewHolder,
                position: Int,
                model: Reply?
            ) {
                if (model != null) {
                    showTop(position, model, holder)
                    showLevelAndUp(model, holder)
                    showChildReplies(model, holder)
                    //getContent(model.map,model.mResult,model.content,holder.findViewById(R.id.tv_comment_content))
                    //holder.findViewById<ReadMoreTextView>(R.id.tv_comment_content).text = model.content
                    holder.text(R.id.tv_up_time_intro, model.time)
                    holder.image(R.id.ri_avatar_comment, model.avatar)
                    holder.text(R.id.tv_good_reply, model.like.std())
                }
            }
        }
        mDelegateAdapter = DelegateAdapter(virtualLayoutManager)
        //mAdapters.add(floatAdapter)
        //mAdapters.add(scrollFixAdapter)
        //mAdapters.add(bannerAdapter)
        //mAdapters.add(commonAdapter)
        //mAdapters.add(titleAdapter)
        mAdapters.add(mNewsAdapter)
        mAdapters.add(bottomAdapter)

        // 3.设置DelegateAdapter
        recyclerView.adapter = mDelegateAdapter
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun showLevelAndUp(
        model: Reply,
        holder: RecyclerViewHolder
    ) {
        val str = "${model.uname}    "
        val spanString = SpannableString(str)
        val lv = when (model.level) {
            0 -> resources.getDrawable(R.drawable.lv0, null)
            1 -> resources.getDrawable(R.drawable.lv1, null)
            2 -> resources.getDrawable(R.drawable.lv2, null)
            3 -> resources.getDrawable(R.drawable.lv3, null)
            4 -> resources.getDrawable(R.drawable.lv4, null)
            5 -> resources.getDrawable(R.drawable.lv5, null)
            6 -> {
                if (model.isSeniorMember == 1)
                    resources.getDrawable(R.drawable.lv6p, null)
                else
                    resources.getDrawable(R.drawable.lv6, null)
            }
            else -> resources.getDrawable(R.drawable.lv0, null)
        }
        if (model.mid == mid) {
            val up = resources.getDrawable(R.drawable.up_pink, null)
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
        holder.findViewById<TextView>(R.id.tv_up_name_comment).text = spanString
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
        val t = resources.getDrawable(R.drawable.top, null)
        tv.text = content
        mr.forEach {
            Glide.with(requireContext())
                .load(urlMap[it.value])
                .placeholder(t)
                .into(object : CustomTarget<Drawable>() {
                    override fun onResourceReady(
                        resource: Drawable,
                        transition: Transition<in Drawable>?
                    ) {
                        val size = sizeMap[it.value]!!
                        resource.setBounds(0, 0, 50 * size, 50 * size)
                        content.setSpan(
                            ImageSpan(resource, ImageSpan.ALIGN_BASELINE),
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

    @SuppressLint("SetTextI18n")
    private fun showChildReplies(model: Reply, holder: RecyclerViewHolder) {
        val linearLayout = holder.findViewById<LinearLayout>(R.id.ll_comment_reply)
        linearLayout.removeAllViews()

        val rls = model.replies
        rls.forEach {
            val tv = TextView(context).apply {
                maxLines = 2
                ellipsize = TextUtils.TruncateAt.END
            }
            val ss =
                SpannableString(it.uname + ":" + it.content)
            ss.setSpan(
                ForegroundColorSpan(Color.BLUE),
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
                setTextColor(Color.BLUE)
            })
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun showTop(
        position: Int,
        model: Reply,
        holder: RecyclerViewHolder
    ) {
        if (position == 0 && hasTop) {
            val param = model.content
            val str = "  $param"
            val spanString = SpannableString(str)
            val d = resources.getDrawable(R.drawable.top, null)
            d.setBounds(0, 0, 96, 48)
            val span = ImageSpan(d, ImageSpan.ALIGN_BOTTOM)
            spanString.setSpan(span, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            showContent(
                model.urlMap,
                model.sizeMap,
                model.mr,
                spanString,
                holder.findViewById<TextView>(R.id.tv_comment_content),
                2
            )
            //holder.findViewById<TextView>(R.id.tv_comment_content).text = spanString
        } else showContent(
            model.urlMap,
            model.sizeMap,
            model.mr,
            SpannableString(model.content),
            holder.findViewById<TextView>(R.id.tv_comment_content),
            0
        )
    }

    private fun dialog(father_id: Long) {
        if (dialog == null) {
            dialog =
                MaterialDialog.Builder(requireContext()).customView(R.layout.layout_comment, false)
                    .title("自定义对话框")
                    .positiveText("R.string.lab_submit")
                    .negativeText("R.string.lab_cancel").build()
            CommentUtil.initViews(requireContext(), father_id, dialog!!)
        }
        CommentUtil.initListeners(father_id)
        dialog?.show()
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

    fun initData(page: Int) {
        if (page == 0) this.page = 0
        this.page += 1
        BiliRepo.getReplyByPage(oid,
            page, 20, {
                if (page == 0) {
                    (activity as NormalVideoActivity).setAccount(it.acount)
                    this.hasTop = it.hasTop
                    this.mid = it.mid
                    mNewsAdapter.refresh(it.replies)
                    mDelegateAdapter!!.setAdapters(mAdapters)
                } else {
                    if (it.replies.isEmpty()) {
                        this@CommentFragmentVLayout.page -= 1
                        Utils.toast("已全部加载")
                        refreshLayout.finishLoadMoreWithNoMoreData()
                    } else mNewsAdapter.loadMore(it.replies)
                }
                if (page == 0)
                    refreshLayout.finishRefresh()
                else
                    refreshLayout.finishLoadMore()

            }, {
                Utils.toast("获取评论失败:请求异常")
                if (page == 0) {
                    mDelegateAdapter!!.setAdapters(mAdapters)
                    refreshLayout.finishRefresh()
                } else {
                    refreshLayout.finishLoadMore()
                }
            })
    }

}